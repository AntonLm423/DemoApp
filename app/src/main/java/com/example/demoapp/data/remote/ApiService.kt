package com.example.demoapp.data.remote

import com.example.demoapp.data.remote.response.MoviesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("TODO!")
    suspend fun getMovies(
        @Query("limit") limit: Int,
        @Query("page") page: Int
    ): MoviesResponse

}