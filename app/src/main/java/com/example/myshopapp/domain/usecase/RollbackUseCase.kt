package com.example.myshopapp.domain.usecase

import com.example.myshopapp.domain.repository.SaleRepository
import com.example.myshopapp.data.remote.model.request.rollback.RollbackRequest
import com.example.shopapp.data.remote.model.response.rollback.RollbackResponse
import javax.inject.Inject

class RollbackUseCase @Inject constructor(
    private val repository: SaleRepository
) {

    suspend operator fun invoke(
        request: RollbackRequest
    ): Result<RollbackResponse> {

        return repository.rollback(request)
    }
}