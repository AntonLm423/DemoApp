package com.example.demoapp.data.local.prefs

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import javax.inject.Inject

class PreferenceStorage @Inject constructor(
    private val context: Context,
    private val prefs: SharedPreferences,
    private val gson: Gson
) {

    companion object {
        private const val PREF_USER = "PREF_USER"
    }
}