package com.example.demoapp.data.repository

import androidx.paging.PagingData
import com.example.demoapp.data.model.Movie
import com.example.demoapp.ui.base.paging.PagingListResponse
import kotlinx.coroutines.flow.Flow

interface CatalogRepository {

    companion object {
        const val PAGE_SIZE = 20
        const val INITIAL_PAGE_SIZE = PAGE_SIZE
        const val PREFETCH_DISTANCE = 10
    }

    suspend fun searchMovies(query: String, page: Int, limit: Int): PagingListResponse<Movie>

    fun getMoviesFlow(query: String): Flow<PagingData<Movie>>
}
