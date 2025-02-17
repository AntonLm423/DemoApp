package com.example.demoapp.extensions

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

fun ImageView.loadImage(
    url: String?,
    @DrawableRes placeholder: Int,
    onLoadFailed: () -> Unit = {},
    onLoadSuccess: () -> Unit = {},
) {
    val requestOptions = RequestOptions()
        .placeholder(placeholder)
        .error(placeholder)

    if (!url.isNullOrBlank()) {
        Glide.with(context)
            .applyDefaultRequestOptions(requestOptions)
            .load(url)
            .listener(
                object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        onLoadFailed.invoke()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        onLoadSuccess.invoke()
                        return false
                    }
                }
            )
            .into(this)
    } else {
        setImageResource(placeholder)
    }
}
