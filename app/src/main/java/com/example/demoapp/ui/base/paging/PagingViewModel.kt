package com.example.demoapp.ui.base.paging

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.demoapp.data.remote.Response
import com.example.demoapp.ui.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

interface PagingViewModel {

    fun <T : Any> MutableLiveData<PagingData<T>>.launchPagingData(block: () -> Flow<PagingData<T>>): Job

    /**
    * Boolean - endOfPaginationReached
    */
    fun MutableLiveData<Response<Boolean>>.bindPagingState(loadState: CombinedLoadStates)
}

class PagingViewModelDelegate @Inject constructor() : BaseViewModel(), PagingViewModel {

    override fun <T : Any> MutableLiveData<PagingData<T>>.launchPagingData(block: () -> Flow<PagingData<T>>) =
        viewModelScope.launch {
            block().cachedIn(viewModelScope).collectLatest { this@launchPagingData.postValue(it) }
        }

    override fun MutableLiveData<Response<Boolean>>.bindPagingState(loadState: CombinedLoadStates) {
        when (loadState.source.refresh) {
            is LoadState.Loading -> this.postValue(Response.loading())

            is LoadState.Error -> {
                val error = (loadState.source.refresh as? LoadState.Error)?.error ?: Exception()
                this.postValue(Response.error(error))
            }

            is LoadState.NotLoading -> {
                this.postValue(Response.success(loadState.append.endOfPaginationReached))
            }
        }
    }
}
