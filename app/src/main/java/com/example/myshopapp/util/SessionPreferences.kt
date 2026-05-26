package com.example.myshopapp.util

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object SessionPreferences {
    val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")


    val ACCESS_TOKEN =
        stringPreferencesKey("access_token")

    val CASHIER_NAME =
        stringPreferencesKey("cashier_name")
}