package com.example.demoapp.data.repository

import androidx.paging.PagingData
import com.example.demoapp.data.datasource.MoviesDataSource
import com.example.demoapp.data.model.Movie
import kotlinx.coroutines.flow.Flow
import com.example.demoapp.ui.base.paging.PagingListResponse

interface CatalogRepository {

    companion object {
        const val PAGE_SIZE = 20
        const val INITIAL_PAGE_SIZE = PAGE_SIZE
        const val PREFETCH_DISTANCE = 10
    }

    suspend fun getMovies(page: Int, limit: Int): PagingListResponse<Movie>

    fun getMoviesFlow(dataSource: MoviesDataSource): Flow<PagingData<Movie>>

}