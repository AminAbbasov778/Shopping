package com.example.myshopapp.domain.usecase

import com.example.myshopapp.data.remote.model.repsonse.deposit.DepositResponse
import com.example.myshopapp.domain.repository.SaleRepository
import com.example.shopapp.data.remote.model.request.deposit.DepositRequest
import javax.inject.Inject

class DepositUseCase @Inject constructor(
    private val repository: SaleRepository
) {

    suspend operator fun invoke(
        request: DepositRequest
    ): Result<DepositResponse> {

        return repository.deposit(request)
    }
}