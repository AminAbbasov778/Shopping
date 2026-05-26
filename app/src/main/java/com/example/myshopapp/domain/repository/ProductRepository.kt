package com.example.myshopapp.domain.repository

import com.example.myshopapp.data.local.entity.ProductEntity

interface ProductRepository {
    suspend fun addProduct(product: ProductEntity): Result<Unit>
    suspend fun getProducts(): Result<List<ProductEntity>>
    suspend fun delete(product: ProductEntity): Result<Unit>
}
