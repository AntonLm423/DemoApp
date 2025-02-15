package com.example.demoapp.data.remote.response

import com.example.demoapp.data.model.Movie
import com.google.gson.annotations.SerializedName

data class MoviesResponse(
    @SerializedName("docs") val movies: List<Movie>,
    val limit: Int,
    val page: Int,
    val pages: Int,
    val total: Int
)