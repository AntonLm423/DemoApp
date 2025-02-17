package com.example.demoapp.ui.base.paging

open class PagingListResponse<T>(
    val totalPages: Int?,
    val items: List<T>?
)
