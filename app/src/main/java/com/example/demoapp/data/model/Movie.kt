package com.example.demoapp.data.model

import androidx.recyclerview.widget.DiffUtil

data class Movie(
    val ageRating: Int?,
    val alternativeName: String?,
    val backdrop: Backdrop?,
    val countries: List<Country>?,
    val description: String?,
    val enName: String?,
    val externalId: ExternalId?,
    val genres: List<Genre>?,
    val id: Int,
    val isSeries: Boolean?,
    val logo: Logo?,
    val movieLength: Int?,
    val name: String?,
    val names: List<Name>?,
    val poster: Poster?,
    val rating: Rating?,
    val ratingMpaa: String?,
    val releaseYears: List<ReleaseYear>?,
    val seriesLength: Int?,
    val shortDescription: String?,
    val status: String?,
    val ticketsOnSale: Boolean?,
    val top10: Int?,
    val top250: Int?,
    val totalSeriesLength: Int?,
    val type: String?,
    val typeNumber: Int?,
    val votes: Votes?,
    val year: Int?,

    /* Local */
    var inFavorite: Boolean = false
) {

    sealed class Payloads {
        data class FavoriteState(val inFavorite: Boolean) : Payloads()
    }

    companion object {

        val diffCallback = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(
                oldItem: Movie,
                newItem: Movie
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: Movie,
                newItem: Movie
            ): Boolean = oldItem == newItem

            override fun getChangePayload(oldItem: Movie, newItem: Movie): Any? {
                return when {
                    oldItem.inFavorite != newItem.inFavorite -> {
                        Payloads.FavoriteState(newItem.inFavorite)
                    }

                    else -> super.getChangePayload(oldItem, newItem)
                }
            }
        }
    }
}