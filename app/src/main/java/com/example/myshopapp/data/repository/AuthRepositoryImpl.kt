package com.example.myshopapp.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.myshopapp.util.SessionPreferences
import com.example.myshopapp.data.remote.model.repsonse.login.LoginResponse
import com.example.myshopapp.data.remote.service.ApiService
import com.example.myshopapp.domain.repository.AuthRepository
import com.example.myshopapp.data.util.safeApiCall
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val apiService: ApiService
) : AuthRepository {

    override suspend fun isLoggedIn(): Boolean {
        return dataStore.data.first()[SessionPreferences.IS_LOGGED_IN] ?: false
    }

    override suspend fun saveSession(token: String, cashierName: String) {
        dataStore.edit {
            it[SessionPreferences.IS_LOGGED_IN] = true
            it[SessionPreferences.ACCESS_TOKEN] = token
            it[SessionPreferences.CASHIER_NAME] = cashierName
        }
    }

    override suspend fun clearSession() {
        dataStore.edit { it.clear() }
    }

    override suspend fun getCashierName(): String? {
        return dataStore.data.first()[SessionPreferences.CASHIER_NAME]
    }

    override suspend fun login(): Result<LoginResponse> = safeApiCall {
        apiService.login()
    }
}