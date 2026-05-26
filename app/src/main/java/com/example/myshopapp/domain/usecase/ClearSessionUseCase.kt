package com.example.myshopapp.domain.usecase

import com.example.myshopapp.domain.repository.AuthRepository
import javax.inject.Inject

class ClearSessionUseCase @Inject constructor(
    private val repository: AuthRepository
) {

    suspend operator fun invoke() {
        repository.clearSession()
    }
}