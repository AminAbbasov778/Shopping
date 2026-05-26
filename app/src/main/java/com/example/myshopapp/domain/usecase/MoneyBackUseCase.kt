package com.example.myshopapp.domain.usecase

import com.example.myshopapp.data.remote.model.request.moneyback.MoneyBackRequest
import com.example.myshopapp.domain.repository.SaleRepository
import javax.inject.Inject

class MoneyBackUseCase @Inject constructor(
    private val repo: SaleRepository
) {
    suspend operator fun invoke(request: MoneyBackRequest)=
         repo.moneyBack(request)

}