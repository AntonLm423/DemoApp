package com.example.demoapp.ui.catalog

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.PagingData
import com.example.demoapp.data.model.Movie
import com.example.demoapp.data.remote.Response
import com.example.demoapp.data.repository.CatalogRepository
import com.example.demoapp.ui.base.BaseViewModel
import com.example.demoapp.ui.base.paging.Empty
import com.example.demoapp.ui.base.paging.PagingViewModel
import com.example.demoapp.ui.base.paging.PagingViewModelDelegate
import kotlinx.coroutines.launch
import javax.inject.Inject

class CatalogViewModel @Inject constructor(
    private val catalogRepository: CatalogRepository,
    private val pagingViewModelDelegate: PagingViewModelDelegate,
) : BaseViewModel(), PagingViewModel by pagingViewModelDelegate {

    private val movies = MutableLiveData<PagingData<Movie>>()
    private val state = MutableLiveData<Response<Empty>>()

    fun searchMovies(query: String) {
        viewModelScope.launch {
            catalogRepository.searchMovies(query, 1,10)
        }
        movies.launchPagingData { catalogRepository.getMoviesFlow(query) }
    }

    fun bindPagingState(loadState: CombinedLoadStates) {
        state.bindPagingState(loadState)
    }
}
