package com.example.myshopapp.data.repository

import android.util.Log
import com.example.myshopapp.util.LogTags
import com.example.myshopapp.data.local.dao.ProductDao
import com.example.myshopapp.data.local.entity.ProductEntity
import com.example.myshopapp.domain.repository.ProductRepository
import com.example.myshopapp.data.util.safeDbCall
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val dao: ProductDao
) : ProductRepository {

    override suspend fun addProduct(product: ProductEntity): Result<Unit> = safeDbCall {
        dao.insert(product)
    }

    override suspend fun getProducts(): Result<List<ProductEntity>> = safeDbCall {
        val data = dao.getAll()
        data
    }

    override suspend fun delete(product: ProductEntity): Result<Unit> = safeDbCall {
        dao.delete(product)
    }
}