package com.example.demoapp.ui.catalog

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.PagingData
import androidx.paging.map
import com.example.demoapp.data.model.Movie
import com.example.demoapp.data.remote.Response
import com.example.demoapp.data.repository.CatalogRepository
import com.example.demoapp.ui.base.BaseViewModel
import com.example.demoapp.ui.base.paging.PagingViewModel
import com.example.demoapp.ui.base.paging.PagingViewModelDelegate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CatalogViewModel @Inject constructor(
    private val catalogRepository: CatalogRepository,
    private val pagingViewModelDelegate: PagingViewModelDelegate,
) : BaseViewModel(), PagingViewModel by pagingViewModelDelegate {

    val movies = MutableLiveData<PagingData<Movie>>()
    val state = MutableLiveData<Response<Boolean>>()

    var searchQuery: String? = null

    private var delayJob: Job? = null

    private companion object {
        const val MIN_QUERY_LENGTH = 3
        const val SEARCH_DELAY = 1000L
    }

    /**
     * @param isForce отменяет предыдущую попытку поиска
     */
    fun searchMovies(query: String, isForce: Boolean) {
        if (isForce) {
            delayJob?.cancel()
        }
        movies.launchPagingData { catalogRepository.getMoviesFlow(query) }
    }

    fun bindPagingState(loadState: CombinedLoadStates) {
        state.bindPagingState(loadState)
    }

    fun updateFavoriteStatus(id: Int) {
        val currentItems = movies.value ?: return

    }

    fun onSearchQueryChanged(query: String) {
        if (query.length >= MIN_QUERY_LENGTH) {
            searchQuery = query
            delayJob?.cancel()
            delayJob = viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    delay(SEARCH_DELAY)
                    searchMovies(query = query, isForce = false)
                }
            }
        } else {
            delayJob?.cancel()
            movies.value = PagingData.empty()
        }
    }
}
