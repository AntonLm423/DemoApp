package com.example.demoapp.data.repository

import com.example.demoapp.data.remote.Response
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

abstract class BaseRepository {

    protected fun <T> doRequest(request: suspend () -> T) = flow {
        emit(Response.success(data = request()))
    }.flowOn(Dispatchers.IO).catch { exception ->
        if (exception is CancellationException) {
            emit(Response.cancel())
        } else {
            emit(Response.error(e = exception))
        }
    }
}
