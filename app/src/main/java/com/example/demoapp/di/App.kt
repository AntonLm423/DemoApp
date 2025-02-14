package com.example.demoapp.di

import android.app.Application
import com.example.demoapp.data.local.prefs.PreferenceStorage
import com.example.demoapp.di.comonent.DaggerAppComponent
import javax.inject.Inject

const val TAG = "AppTag"

class App : Application() {

    @Inject
    lateinit var preferenceStorage: PreferenceStorage

    val appComponent = DaggerAppComponent.create()
}