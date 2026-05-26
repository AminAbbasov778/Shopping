package com.example.myshopapp.domain.usecase

import com.example.myshopapp.data.local.entity.ProductEntity
import com.example.myshopapp.domain.repository.ProductRepository
import javax.inject.Inject

class AddProductUseCase @Inject constructor(
    private val repo: ProductRepository
) {
    suspend operator fun invoke(product: ProductEntity) =
        repo.addProduct(product)
}
