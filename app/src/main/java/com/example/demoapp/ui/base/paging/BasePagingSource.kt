package com.example.demoapp.ui.base.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState

abstract class BasePagingSource<T : Any> : PagingSource<Int, T>() {

    companion object {
        const val DEFAULT_PREFETCH_DISTANCE = 5
        const val DEFAULT_OFFSET = 0
        const val DEFAULT_LIMIT = 10
    }

    var stopLoading = false

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val offset = params.key ?: DEFAULT_OFFSET
        val limit = params.loadSize
        val pageNumber = (offset / limit) + 1

        return try {
            val result = loadPage(pageNumber, limit)
            val nextOffset = offset + result.items.orEmpty().size
            val nextKeyNull = (result.total ?: 0) < limit || result.items.orEmpty().isEmpty()

            LoadResult.Page(
                data = result.items.orEmpty(),
                prevKey = null,
                nextKey = if (stopLoading || nextKeyNull) {
                    null
                } else {
                    nextOffset
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
