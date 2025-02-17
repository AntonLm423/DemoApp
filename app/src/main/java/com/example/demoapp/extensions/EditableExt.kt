package com.example.demoapp.extensions

import android.text.Editable

fun Editable?.stringOrEmpty() = this?.toString().orEmpty().trim()
