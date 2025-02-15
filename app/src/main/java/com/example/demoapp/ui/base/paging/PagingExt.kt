package com.example.demoapp.ui.base.paging

import androidx.paging.LoadStateAdapter
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingDataAdapter
import androidx.paging.PagingSource
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView

inline fun <T : Any> createPager(
    pagingSource: PagingSource<Int, T>,
    pageSize: Int = BasePagingSource.DEFAULT_LIMIT,
    initialLoadSize: Int = BasePagingSource.DEFAULT_LIMIT,
    enablePlaceholders: Boolean = true,
    prefetchDistance: Int = BasePagingSource.DEFAULT_PREFETCH_DISTANCE,
): Pager<Int, T> {
    return Pager(
        config = PagingConfig(
            pageSize = pageSize,
            enablePlaceholders = enablePlaceholders,
            initialLoadSize = initialLoadSize,
            prefetchDistance = prefetchDistance,
        ),
        pagingSourceFactory = { pagingSource },
    )
}

/** для использования своих viewTypes + по умолчанию начальные состояние загрузки и ошибки не выводятся в LoadStateAdapter, для
 * этого нужно использовать [header] **/
fun <T : Any, V : RecyclerView.ViewHolder> PagingDataAdapter<T, V>.withLoadStateConfig(
    header: LoadStateAdapter<*>,
    footer: LoadStateAdapter<*>
): ConcatAdapter {
    addLoadStateListener { loadStates ->
        header.loadState = loadStates.refresh
        footer.loadState = loadStates.append
    }
    return ConcatAdapter(
        ConcatAdapter.Config.Builder()
            .setIsolateViewTypes(false)
            .build(),
        header,
        this,
        footer
    )
}
