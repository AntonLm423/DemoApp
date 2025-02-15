package com.example.demoapp.ui.base

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.demoapp.extensions.inflate

abstract class BindingViewHolder<VB : ViewBinding>(
    parent: ViewGroup,
    @LayoutRes layoutId: Int,
) : RecyclerView.ViewHolder(parent.inflate(layoutId)) {

    protected val itemBinding: VB by lazy { createBinding(itemView) }

    protected val context: Context
        get() = itemBinding.root.context

    abstract fun createBinding(view: View): VB
}