package com.example.demoapp.extensions

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.Px
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding

fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.addSystemWindowInsetToPadding(
    left: Boolean = false,
    top: Boolean = false,
    right: Boolean = false,
    bottom: Boolean = false,
) {
    val (initialLeft, initialTop, initialRight, initialBottom) =
        listOf(paddingLeft, paddingTop, paddingRight, paddingBottom)

    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insetsCompat ->
        val insets = insetsCompat.getInsets(WindowInsetsCompat.Type.systemBars())
        view.updatePadding(
            left = initialLeft + (if (left) insets.left else 0),
            top = initialTop + (if (top) insets.top else 0),
            right = initialRight + (if (right) insets.right else 0),
            bottom = initialBottom + (if (bottom) insets.bottom else 0)
        )

        insetsCompat
    }
}

fun View.addSystemWindowInsetToMargin(
    left: Boolean = false,
    top: Boolean = false,
    right: Boolean = false,
    bottom: Boolean = false
) {
    val (initialLeft, initialTop, initialRight, initialBottom) =
        listOf(marginLeft, marginTop, marginRight, marginBottom)

    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insetsCompat ->
        val insets = insetsCompat.getInsets(WindowInsetsCompat.Type.systemBars())
        view.updateLayoutParams {
            (this as? ViewGroup.MarginLayoutParams)?.let {
                updateMargin(
                    start = initialLeft + (if (left) insets.left else 0),
                    top = initialTop + (if (top) insets.top else 0),
                    end = initialRight + (if (right) insets.right else 0),
                    bottom = initialBottom + (if (bottom) insets.bottom else 0)
                )
            }
        }

        insetsCompat
    }
}

fun View.updateMargin(
    @Px top: Int? = null,
    @Px bottom: Int? = null,
    @Px start: Int? = null,
    @Px end: Int? = null
) = updateLayoutParams<ViewGroup.MarginLayoutParams> {
    if (top != null) {
        topMargin = top
    }
    if (bottom != null) {
        bottomMargin = bottom
    }
    if (start != null) {
        marginStart = start
    }
    if (end != null) {
        marginEnd = end
    }
}