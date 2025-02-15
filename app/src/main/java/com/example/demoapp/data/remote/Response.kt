package com.example.demoapp.data.remote

import com.example.demoapp.data.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

sealed class Response<T> {

    class Loading<T> : Response<T>()
    class Cancel<T> : Response<T>()
    data class Success<T>(val data: T) : Response<T>()
    data class Error<T>(val e: Throwable) : Response<T>()

    companion object {
        fun <T> loading(): Response<T> = Loading()
        fun <T> cancel(): Response<T> = Cancel()
        fun <T> success(data: T): Response<T> = Success(data)
        fun <T> error(e: Throwable): Response<T> = Error(e)
    }

    fun getDataIfSuccess(): T? {
        return if (this is Success) this.data else null
    }
}

/*fun <T> toResponseFlow(call: suspend () -> Response<T>?) : Flow<Response<T>?> {

    return flow {
        emit(Response.loading<T>())
        val c = call()  *//* have to initialize the call method first*//*
        c?.let {
            try {
                if (c.isSuccessful && c.body() != null) {
                    c.body()?.let {
                        emit(Response.Success(it))
                    }
                } else {
                    c.errorBody()?.let {
                        emit(Response.Error(it.string()))
                    }
                }
            }catch (e: Exception){
                emit(ApiResult.Error(e.toString()))
            }
        }
    }.flowOn(Dispatchers.IO)
}*/
