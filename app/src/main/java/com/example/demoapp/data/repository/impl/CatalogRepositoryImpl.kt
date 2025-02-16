package com.example.demoapp.data.repository.impl

import android.util.Log
import androidx.paging.PagingData
import com.example.demoapp.data.datasource.MoviesDataSource
import com.example.demoapp.data.model.Movie
import com.example.demoapp.data.remote.ApiService
import com.example.demoapp.data.repository.BaseRepository
import com.example.demoapp.data.repository.CatalogRepository
import kotlinx.coroutines.flow.Flow
import com.example.demoapp.ui.base.paging.PagingListResponse
import com.example.demoapp.ui.base.paging.createPager
import javax.inject.Inject

class CatalogRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : BaseRepository(), CatalogRepository {

    override suspend fun searchMovies(query: String, page: Int, limit: Int): PagingListResponse<Movie> {
        Log.d("asdw", "123")
        val result = apiService.searchMovies(query, page, limit)
        return PagingListResponse(result.total, result.movies)
    }

    override fun getMoviesFlow(query: String): Flow<PagingData<Movie>> {
        return createPager(
            MoviesDataSource(query = query, repository = this),
            pageSize = CatalogRepository.PAGE_SIZE,
            initialLoadSize = CatalogRepository.INITIAL_PAGE_SIZE,
            prefetchDistance = CatalogRepository.PREFETCH_DISTANCE,
        ).flow
    }
}
