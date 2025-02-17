package com.example.demoapp.extensions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

fun <T : RecyclerView.ViewHolder> RecyclerView.Adapter<T>.inflateView(@LayoutRes layoutRes: Int, parent: ViewGroup): View {
    return LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
}