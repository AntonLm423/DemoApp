package com.example.demoapp.ui.catalog

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.ColorFilter
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import com.example.demoapp.R
import com.example.demoapp.data.model.Movie
import com.example.demoapp.databinding.ItemListMovieBinding
import com.example.demoapp.extensions.getColorCompat
import com.example.demoapp.extensions.inflateView
import com.example.demoapp.extensions.loadImage
import com.example.demoapp.extensions.setTextOrGone
import com.example.demoapp.ui.base.BaseViewHolder

class MoviesAdapter(
    private val onItemInteractionListener: OnItemInteractionListener
) : PagingDataAdapter<Movie, MoviesAdapter.MovieViewHolder>(Movie.diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(ItemListMovieBinding.bind(inflateView(R.layout.item_list_movie, parent)))
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        getItem(position)?.let { item -> holder.bind(item) }
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            payloads.forEach { payload ->
                when (payload) {
                    is Movie.Payloads.FavoriteState -> holder.bindFavoriteState(payload.inFavorite)
                    else -> onBindViewHolder(holder, position)
                }
            }
        }
    }

    /** ViewHolders */
    inner class MovieViewHolder(private val binding: ItemListMovieBinding) : BaseViewHolder<Movie>(binding.root) {

        override fun bind(item: Movie) = with(binding) {
            val imageUrl = item.logo?.url ?: item.poster?.previewUrl ?: item.poster?.url
            imageViewPreview.loadImage(url = imageUrl, placeholder = R.drawable.placeholder_movie)
            textViewTitle.setTextOrGone(item.name)
            textViewDescription.setTextOrGone(item.description)
            bindFavoriteState(item.inFavorite)
        }

        fun bindFavoriteState(inFavorite: Boolean) = with(binding.imageViewFavorite) {
            val currentItem = snapshot().items[absoluteAdapterPosition]
            setOnClickListener {
                val newStatus = !inFavorite
                currentItem.inFavorite = newStatus
                notifyItemChanged(absoluteAdapterPosition, Movie.Payloads.FavoriteState(newStatus))
                onItemInteractionListener.updateFavoriteStatus(id = currentItem.id, inFavorite = newStatus)
            }
            setImageResource(if(inFavorite) R.drawable.ic_favorite_selected else R.drawable.ic_favorite)
        }
    }

    interface OnItemInteractionListener {
        fun updateFavoriteStatus(id: Int, inFavorite: Boolean)
    }
}