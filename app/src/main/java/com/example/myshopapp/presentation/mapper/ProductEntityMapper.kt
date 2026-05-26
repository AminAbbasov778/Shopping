package com.example.myshopapp.presentation.mapper

import com.example.myshopapp.data.local.entity.ProductEntity

fun mapToProductEntity(
    id: Long,
    name: String,
    code: String,
    barcode: String,
    salePrice: Double,
    purchasePrice: Double,
    vatPercent: Double,
    isAgro: Boolean
): ProductEntity {
    return ProductEntity(
        id = id,
        name = name,
        code = code,
        barcode = barcode,
        salePrice = salePrice,
        purchasePrice = purchasePrice,
        vatPercent = vatPercent,
        isAgro = isAgro
    )
}