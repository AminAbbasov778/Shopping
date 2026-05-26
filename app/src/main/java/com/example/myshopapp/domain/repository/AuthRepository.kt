package com.example.myshopapp.domain.repository

import com.example.myshopapp.data.remote.model.repsonse.login.LoginResponse

interface AuthRepository {

    suspend fun login(): Result<LoginResponse>

    suspend fun saveSession(
        token: String, cashierName: String,
    )
    suspend fun getCashierName(): String?

    suspend fun clearSession()

    suspend fun isLoggedIn(): Boolean
}