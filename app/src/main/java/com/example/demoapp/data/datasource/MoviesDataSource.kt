package com.example.demoapp.data.datasource

import com.example.demoapp.data.model.Movie
import com.example.demoapp.data.repository.CatalogRepository
import com.example.demoapp.ui.base.paging.BasePagingSource
import com.example.demoapp.ui.base.paging.PagingListResponse

class MoviesDataSource(
    private val repository: CatalogRepository
) : BasePagingSource<Movie>() {

    var total: Int? = null

    override suspend fun loadPage(page: Int, limit: Int): PagingListResponse<Movie> {
        val response = repository.getMovies(page = page, limit = limit)
        total = response.total
        return PagingListResponse(total, response.items)
    }
}
