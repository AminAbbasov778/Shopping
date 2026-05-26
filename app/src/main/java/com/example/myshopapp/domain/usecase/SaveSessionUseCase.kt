package com.example.myshopapp.domain.usecase

import com.example.myshopapp.domain.repository.AuthRepository
import javax.inject.Inject

class SaveSessionUseCase @Inject constructor(
    private val repository: AuthRepository
) {

    suspend operator fun invoke(token: String, cashierName: String) {
        repository.saveSession(token, cashierName)
    }
}