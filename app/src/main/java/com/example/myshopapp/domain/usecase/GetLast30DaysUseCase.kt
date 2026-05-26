package com.example.myshopapp.domain.usecase

import com.example.myshopapp.data.local.entity.SaleFull
import com.example.myshopapp.domain.repository.SaleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLast30DaysUseCase @Inject constructor(
    private val repository: SaleRepository
) {
    operator fun invoke(from: Long): Flow<Result<List<SaleFull>>> {
        return repository.getLast30DaysFull(from)
    }
}