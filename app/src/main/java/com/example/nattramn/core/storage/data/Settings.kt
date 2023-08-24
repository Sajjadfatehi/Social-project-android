package com.example.nattramn.core.storage.data

import android.content.SharedPreferences
import com.example.nattramn.core.storage.types.StringPreference

class Settings(sharedPreferences: SharedPreferences) {

    var authToken: String? by StringPreference(
        sharedPreferences,
        AUTH_TOKEN
    )

    var authUsername: String? by StringPreference(
        sharedPreferences,
        AUTH_USERNAME
    )

    var titleDraft: String? by StringPreference(
        sharedPreferences,
        TITLE_DRAFT
    )

    var bodyDraft: String? by StringPreference(
        sharedPreferences,
        BODY_DRAFT
    )

    companion object Key {
        const val AUTH_TOKEN = "AUTH_TOKEN"
        const val AUTH_USERNAME = "AUTH_USERNAME"
        const val TITLE_DRAFT = "TITLE_DRAFT"
        const val BODY_DRAFT = "BODY_DRAFT"
    }
}