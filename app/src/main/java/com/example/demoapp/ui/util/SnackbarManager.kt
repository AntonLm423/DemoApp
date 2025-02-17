package com.example.demoapp.ui.util

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.LinearLayout.*
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.updateLayoutParams
import com.example.demoapp.R
import com.example.demoapp.databinding.ViewSnackbarBinding
import com.example.demoapp.extensions.dpToPx
import com.example.demoapp.extensions.getColorCompat
import com.example.demoapp.extensions.setTextOrGone
import com.google.android.material.snackbar.Snackbar

class SnackbarManager : Snackbar.Callback() {

    private val snackbarsWaiting: MutableList<Snackbar> = mutableListOf()
    private val snackbarsHolding: MutableList<Snackbar> = mutableListOf()

    /**
     * @param combineSame - объединять одинаковые по тексту и заголовку
     */
    fun addToQueue(snackbar: Snackbar, combineSame: Boolean = true) {
        if (combineSame && snackbarsWaiting.any { it.isSameContent(snackbar) } ||
            snackbarsHolding.any { it.isSameContent(snackbar) }
        ) {
            return
        }

        if (snackbar.duration == Snackbar.LENGTH_INDEFINITE)
            snackbar.duration = Snackbar.LENGTH_LONG
        snackbar.addCallback(this)
        if (snackbarsWaiting.isNotEmpty() && snackbarsWaiting[0].isShown) {
            snackbarsHolding.add(snackbar)
        } else {
            snackbarsWaiting.add(snackbar)
        }

        show()
    }

    fun show() {
        if (snackbarsWaiting.isNotEmpty() && !snackbarsWaiting[0].isShown) {
            snackbarsWaiting[0].show()
        }
    }

    override fun onDismissed(snackbar: Snackbar, event: Int) {
        super.onDismissed(snackbar, event)
        snackbarsWaiting.remove(snackbar)
        if (snackbarsHolding.isNotEmpty()) {
            snackbarsWaiting.addAll(snackbarsHolding)
            snackbarsHolding.clear()
        }
        if (snackbarsWaiting.isNotEmpty()) {
            snackbarsWaiting[0].show()
        }
    }

    private fun Snackbar.isSameContent(other: Snackbar): Boolean {
        return this.getMessage() == other.getMessage() && other.getTitle() == other.getTitle()
    }

    private fun Snackbar.getMessage(): String? {
        return this.view.findViewById<TextView>(R.id.textViewMessage)?.text?.toString()
    }

    private fun Snackbar.getTitle(): String? {
        return this.view.findViewById<TextView>(R.id.textViewTitle)?.text?.toString()
    }

    companion object {
        fun createSnackbar(
            view: View,
            @StringRes titleResId: Int? = null,
            @StringRes messageResId: Int? = null,
            @StringRes buttonTextResId: Int? = null,
            @DrawableRes iconResId: Int? = null,
            message: String? = null,
            action: (() -> Unit)? = null,
            snackbarShowLength: Int? = Snackbar.LENGTH_SHORT,
            aboveView: View? = null,
            bottomMarginInDp: Int? = 16,
            horizontalMarginInDp: Int? = 16,
            animationMode: Int? = null,
            iconBias: Float = 0.5f
        ): Snackbar {
            val snackbar = Snackbar.make(view, "", snackbarShowLength ?: Snackbar.LENGTH_SHORT)
            val snackbarView = (snackbar.view as Snackbar.SnackbarLayout)

            snackbarView.removeAllViews()
            snackbarView.setPadding(0, 0, 0, 0)

            val marginHorizontal = horizontalMarginInDp?.dpToPx() ?: 0
            val marginBottom = bottomMarginInDp?.dpToPx() ?: 0

            val params = snackbarView.layoutParams
            when (params) {
                is CoordinatorLayout.LayoutParams -> {
                    params.gravity = Gravity.BOTTOM
                    params.setMargins(marginHorizontal, 0, marginHorizontal, marginBottom)
                }

                is LayoutParams -> {
                    params.gravity = Gravity.BOTTOM
                    params.setMargins(marginHorizontal, 0, marginHorizontal, marginBottom)
                }

                is FrameLayout.LayoutParams -> {
                    params.gravity = Gravity.BOTTOM
                    params.setMargins(marginHorizontal, 0, marginHorizontal, marginBottom)
                }

                is RelativeLayout.LayoutParams -> {
                    params.setMargins(marginHorizontal, 0, marginHorizontal, marginBottom)
                }

                else -> {
                    snackbarView.setPadding(marginHorizontal, 0, marginHorizontal, marginBottom)
                }
            }
            snackbarView.layoutParams = params
            snackbarView.setBackgroundColor(view.context.getColorCompat(android.R.color.transparent))

            ViewCompat.setElevation(snackbarView, 6.dpToPx().toFloat())

            val binding = ViewSnackbarBinding.inflate(LayoutInflater.from(view.context), snackbarView, false)

            with(binding) {
                val messageText = messageResId?.let { view.context.getString(messageResId) } ?: message
                textViewMessage.setTextOrGone(messageText)
                textViewTitle.setTextOrGone(titleResId?.let { view.context.getString(titleResId) })

                imageViewIcon.apply {
                    updateLayoutParams<ConstraintLayout.LayoutParams> {
                        verticalBias = iconBias
                    }

                    if (iconResId != null) {
                        setImageResource(iconResId)
                        visibility = View.VISIBLE
                    } else {
                        visibility = View.GONE
                    }
                }

                buttonAction.apply {
                    setTextOrGone(buttonTextResId?.let { view.context.getString(buttonTextResId) })
                    setOnClickListener {
                        action?.invoke() ?: snackbar.dismiss()
                    }
                }
            }

            aboveView?.let { anchorView ->
                snackbar.anchorView = anchorView
            }

            animationMode?.let {
                snackbar.animationMode = animationMode
            }

            snackbarView.addView(binding.root)

            return snackbar
        }
    }
}