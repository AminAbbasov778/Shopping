package com.example.myshopapp.domain.usecase

import com.example.myshopapp.domain.repository.AuthRepository
import javax.inject.Inject

class GetCashierNameUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke() = authRepository.getCashierName()
}