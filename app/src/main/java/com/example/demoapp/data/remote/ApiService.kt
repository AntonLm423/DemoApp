package com.example.demoapp.data.remote

import com.example.demoapp.data.remote.response.MoviesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    private companion object {
        /** API VERSIONS */
        const val V1_1 = "v1.1"
        const val V1_2 = "v1.2"
        const val V1_3 = "v1.3"
        const val V1_4 = "v1.4"
    }

    @GET("$V1_4/movie/search")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("limit") limit: Int,
        @Query("page") page: Int
    ): MoviesResponse

}
