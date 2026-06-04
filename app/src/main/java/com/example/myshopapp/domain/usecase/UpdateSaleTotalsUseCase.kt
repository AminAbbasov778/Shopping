package com.example.myshopapp.domain.usecase

import com.example.myshopapp.domain.repository.SaleRepository
import javax.inject.Inject

class UpdateSaleTotalsUseCase @Inject constructor(
    private val repository: SaleRepository
) {
    suspend operator fun invoke(
        documentId: String,
        total: Double,
        cashSum: Double,
        cardSum: Double,
        bonusSum: Double,
        creditSum: Double,
        prepaymentSum: Double
    ): Result<Unit> = repository.updateSaleTotals(
        documentId    = documentId,
        total         = total,
        cashSum       = cashSum,
        cardSum       = cardSum,
        bonusSum      = bonusSum,
        creditSum     = creditSum,
        prepaymentSum = prepaymentSum
    )
}
