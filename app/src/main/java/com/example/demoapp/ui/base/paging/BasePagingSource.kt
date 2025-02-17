package com.example.demoapp.ui.base.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState

abstract class BasePagingSource<T : Any> : PagingSource<Int, T>() {

    companion object {
        const val DEFAULT_PREFETCH_DISTANCE = 5
        const val DEFAULT_PAGE_NUMBER = 1
        const val DEFAULT_LIMIT = 10
    }

    var stopLoading = false

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val pageNumber = params.key ?: DEFAULT_PAGE_NUMBER
        val limit = params.loadSize

        return try {
            val result = loadPage(pageNumber, limit)
            val nextPage = pageNumber + 1
            val nextKeyNull = result.items.orEmpty().isEmpty()

            LoadResult.Page(
                data = result.items.orEmpty(),
                prevKey = null,
                nextKey = if (stopLoading || nextKeyNull) {
                    null
                } else {
                    nextPage
                },
            )
        } catch (ex: Exception) {
            ex.printStackTrace()
            LoadResult.Error(ex)
        }
    }

    abstract suspend fun loadPage(page: Int, limit: Int): PagingListResponse<T>

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
