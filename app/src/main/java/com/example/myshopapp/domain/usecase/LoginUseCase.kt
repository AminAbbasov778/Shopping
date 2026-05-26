package com.example.myshopapp.domain.usecase

import com.example.myshopapp.data.remote.model.repsonse.login.LoginResponse
import com.example.myshopapp.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {

    suspend operator fun invoke(): Result<LoginResponse> {
        return repository.login()
    }
}