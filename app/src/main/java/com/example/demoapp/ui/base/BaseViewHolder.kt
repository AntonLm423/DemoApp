package com.example.demoapp.ui.base

import android.content.Context
import android.content.res.Resources
import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T>(view: View) : RecyclerView.ViewHolder(view) {
    protected val resources: Resources = view.resources
    protected val context: Context = view.context

    protected fun getString(resId: Int) = resources.getString(resId)
    protected fun getString(resId: Int, vararg formatArgs: Any?) = resources.getString(resId, *formatArgs)

    abstract fun bind(item: T)
}