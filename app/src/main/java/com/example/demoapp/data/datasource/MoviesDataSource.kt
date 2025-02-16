package com.example.demoapp.data.datasource

import com.example.demoapp.data.model.Movie
import com.example.demoapp.data.repository.CatalogRepository
import com.example.demoapp.ui.base.paging.BasePagingSource
import com.example.demoapp.ui.base.paging.PagingListResponse

class MoviesDataSource(
    private val query: String,
    private val repository: CatalogRepository
) : BasePagingSource<Movie>() {

    override suspend fun loadPage(page: Int, limit: Int): PagingListResponse<Movie> {
        if (query.isBlank()) {
            return PagingListResponse(0, emptyList())
        } else {
            val response = repository.searchMovies(query, page, limit)
            return PagingListResponse(response.total, response.items)
        }
    }
}
