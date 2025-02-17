package com.example.demoapp.extensions

import android.view.View
import android.widget.TextView

fun TextView.setTextOrGone(text: String?) {
    visibility = if (text.isNullOrBlank()) {
        View.GONE
    } else {
        setText(text)
        View.VISIBLE
    }
}
