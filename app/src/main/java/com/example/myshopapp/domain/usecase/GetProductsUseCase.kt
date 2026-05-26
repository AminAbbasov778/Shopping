package com.example.myshopapp.domain.usecase

import com.example.myshopapp.domain.repository.ProductRepository
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val repo: ProductRepository
) {
    suspend operator fun invoke() =
        repo.getProducts()
}
