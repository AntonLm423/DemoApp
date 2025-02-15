package com.example.demoapp.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demoapp.data.remote.Response
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseViewModel: ViewModel() {

    inner class ResponseLiveData<T> : MutableLiveData<Response<T>>() {

        private var job: Job? = null

        fun launch(
            isSilent: Boolean = true,
            onSuccess: ((T) -> Unit)? = null,
            afterSuccess: ((T) -> Unit)? = null,
            onComplete: (() -> Unit)? = null,
            onTerminate: (() -> Unit)? = null,
            onError: ((error: Throwable) -> Unit)? = null,
            delayBeforeExecute: Long = 0L,
            resultDelay: Long = 0,
            dispatcher: CoroutineDispatcher = Dispatchers.IO,
            executableMethod: suspend () -> T,
        ) {
            job?.cancel()
            job = viewModelScope.launch(dispatcher) {
                runCatching {
                    if (!isSilent) {
                        postValue(Response.Loading())
                    }
                    delay(delayBeforeExecute)
                    executableMethod()
                }.onSuccess { data ->
                    withContext(Dispatchers.Main) {
                        delay(resultDelay)
                        onTerminate?.invoke()
                        onSuccess?.invoke(data)
                        postValue(Response.Success(data))
                        afterSuccess?.invoke(data)
                    }
                }.onFailure { throwable ->
                    withContext(Dispatchers.Main) {
                        onTerminate?.invoke()
                        onError?.invoke(throwable)
                        postValue(Response.Error(throwable))
                    }
                }.also {
                    withContext(Dispatchers.Main) {
                        onComplete?.invoke()
                    }
                }
            }
        }

        fun cancel() {
            job?.cancel()
        }
    }
}
