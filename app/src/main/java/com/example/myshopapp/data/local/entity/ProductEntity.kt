package com.example.myshopapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,
    val code: String,
    val barcode: String,
    val salePrice: Double,
    val purchasePrice: Double,
    val vatPercent: Double,
    val isAgro: Boolean
)