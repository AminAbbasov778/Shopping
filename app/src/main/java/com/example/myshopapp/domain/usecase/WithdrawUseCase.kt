package com.example.myshopapp.domain.usecase

import com.example.myshopapp.data.remote.model.repsonse.withdraw.WithDrawResponse
import com.example.myshopapp.data.remote.model.request.withdraw.WithDrawRequest
import com.example.myshopapp.domain.repository.SaleRepository
import javax.inject.Inject

class WithdrawUseCase @Inject constructor(
    private val repository: SaleRepository
) {

    suspend operator fun invoke(
        request: WithDrawRequest
    ): Result<WithDrawResponse> {

        return repository.withdraw(request)
    }
}