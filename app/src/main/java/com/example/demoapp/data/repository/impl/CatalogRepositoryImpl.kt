package com.example.demoapp.data.repository.impl

import androidx.paging.PagingData
import com.example.demoapp.data.datasource.MoviesDataSource
import com.example.demoapp.data.local.prefs.PreferenceStorage
import com.example.demoapp.data.model.Movie
import com.example.demoapp.data.remote.ApiService
import com.example.demoapp.data.repository.BaseRepository
import com.example.demoapp.data.repository.CatalogRepository
import com.example.demoapp.ui.base.paging.PagingListResponse
import com.example.demoapp.ui.base.paging.createPager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CatalogRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val preferenceStorage: PreferenceStorage,
) : BaseRepository(), CatalogRepository {

    override suspend fun searchMovies(query: String, page: Int, limit: Int): PagingListResponse<Movie> {
        val result = apiService.searchMovies(query, limit, page)
        val filteredMovies = result.movies
            .filterNot { it.name.isNullOrBlank() }
            .map { it.copy(inFavorite = preferenceStorage.isMovieInFavorite(it.id)) }

        return PagingListResponse(
            totalPages = result.pages,
            items = filteredMovies
        )
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
