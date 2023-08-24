package com.example.nattramn.core.storage.data

import android.content.Context
import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class PreferenceProperty<T>(
    private val key: String,
    private val defaultValue: T,
    private val getter: SharedPreferences.(String, T) -> T,
    private val setter: SharedPreferences.Editor.(String, T) -> SharedPreferences.Editor
) : ReadWriteProperty<Context, T> {

    companion object {
        const val APP_PREF_NAME = "MyApp"
        fun Context.getPreferences(): SharedPreferences =
            getSharedPreferences(APP_PREF_NAME, Context.MODE_PRIVATE)
    }

    override fun getValue(thisRef: Context, property: KProperty<*>): T =
        thisRef.getPreferences()
            .getter(key, defaultValue)

    override fun setValue(thisRef: Context, property: KProperty<*>, value: T) =
        thisRef.getPreferences()
            .edit()
            .setter(key, value)
            .apply()

}