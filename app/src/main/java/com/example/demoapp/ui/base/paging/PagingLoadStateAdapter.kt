package com.example.demoapp.ui.base.paging

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.example.demoapp.R
import com.example.demoapp.databinding.ItemPagingFooterBinding
import com.example.demoapp.extensions.isInternetAvailable
import com.example.demoapp.ui.base.BindingViewHolder

class PagingLoadStateAdapter(
    private val onRetry: (() -> Unit),
) : LoadStateAdapter<PagingLoadStateViewHolder>() {

    companion object {
        const val FOOTER_TYPE = 99999
    }

    override fun getStateViewType(loadState: LoadState) = FOOTER_TYPE

    override fun onBindViewHolder(holder: PagingLoadStateViewHolder, loadState: LoadState) = holder.bind(loadState)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): PagingLoadStateViewHolder {
        return PagingLoadStateViewHolder(
            parent,
            onRetry
        )
    }
}

class PagingLoadStateViewHolder(parent: ViewGroup, onRetry: () -> Unit) :
    BindingViewHolder<ItemPagingFooterBinding>(parent, R.layout.item_paging_footer) {

    init {
        itemBinding.buttonPagingRetry.setOnClickListener { onRetry.invoke() }
    }

    fun bind(loadState: LoadState) = with(itemBinding) {
        when (loadState) {
            is LoadState.Loading -> {
                root.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            }

            is LoadState.Error -> {
                root.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                handleErrorState()
            }

            else -> {}
        }
    }

    private fun handleErrorState() = with(itemBinding) {
        if (!itemBinding.root.context.isInternetAvailable()) {
            textViewPagingErrorDescription.isVisible = true
            textViewPagingErrorTitle.text = itemView.resources.getString(R.string.common_no_internet)
            textViewPagingErrorDescription.text = itemView.resources.getString(R.string.common_no_internet)
        } else {
            textViewPagingErrorDescription.isVisible = false
            textViewPagingErrorTitle.text = itemView.resources.getString(R.string.common_something_went_wrong)
        }
    }

    override fun createBinding(view: View): ItemPagingFooterBinding {
        return ItemPagingFooterBinding.bind(view)
    }
}
