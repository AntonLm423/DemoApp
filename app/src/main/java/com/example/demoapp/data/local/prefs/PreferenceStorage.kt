package com.example.demoapp.data.local.prefs

import android.content.Context
import android.content.SharedPreferences
import com.example.demoapp.data.model.LocalFavoriteMovieItem
import com.google.gson.Gson
import java.util.Date
import javax.inject.Inject

class PreferenceStorage @Inject constructor(
    private val context: Context,
    private val prefs: SharedPreferences,
    private val gson: Gson
) {

    companion object {
        private const val MAX_FAVORITES_COUNT = 20

        private const val PREF_USER = "PREF_USER"
        private const val PREF_FAVORITE_MOVIES = "PREF_FAVORITE_MOVIES"
    }

    /**
     * Local favorite movies
     */
    fun getLocalFavoriteMovies(): MutableList<LocalFavoriteMovieItem>? {
        return prefs.getString(PREF_FAVORITE_MOVIES, null)?.let { jsonStr ->
            try {
                gson.fromJson(jsonStr, Array<LocalFavoriteMovieItem>::class.java).toMutableList()
            } catch (e: Exception) {
                prefs[PREF_FAVORITE_MOVIES] = null
                null
            }
        }
    }

    fun addToLocalFavoriteMovies(id: Int) {
        val currentList = getLocalFavoriteMovies() ?: mutableListOf()

        with(currentList) {
            removeIf { it.id == id }
            add(LocalFavoriteMovieItem(id = id, dateTime = Date().time))
            sortByDescending { it.dateTime }
        }

        prefs[PREF_FAVORITE_MOVIES] = gson.toJson(currentList.take(MAX_FAVORITES_COUNT))
    }

    fun removeFromLocalFavoriteMovies(id: Int) {
        val currentList = getLocalFavoriteMovies() ?: mutableListOf()
        if (currentList.removeIf { it.id == id }) {
            prefs[PREF_FAVORITE_MOVIES] = gson.toJson(currentList.take(MAX_FAVORITES_COUNT))
        }
    }

    fun isMovieInFavorite(id: Int) = getLocalFavoriteMovies()?.any { it.id == id } ?: false
}