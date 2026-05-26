package com.example.myshopapp.domain.usecase

import com.example.myshopapp.data.remote.model.repsonse.LastDocumentResponse
import com.example.myshopapp.domain.repository.SaleRepository
import javax.inject.Inject

class GetLastDocumentUseCase @Inject constructor(
    private val repository: SaleRepository
) {

    suspend operator fun invoke(

    ): Result<LastDocumentResponse> {
        return repository.getLastDocument()
    }
}