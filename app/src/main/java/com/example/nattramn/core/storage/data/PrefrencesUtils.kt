package com.example.nattramn.core.storage.data

import android.content.Context
import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty

fun intPreference(key: String, defaultValue: Int = 0): ReadWriteProperty<Context, Int> =
    PreferenceProperty(
        key = key,
        defaultValue = defaultValue,
        getter = SharedPreferences::getInt,
        setter = SharedPreferences.Editor::putInt
    )

fun stringPreference(key: String, defaultValue: String?): PreferenceProperty<String?> =
    PreferenceProperty(
        key = key,
        defaultValue = defaultValue,
        getter = SharedPreferences::getString,
        setter = SharedPreferences.Editor::putString
    )