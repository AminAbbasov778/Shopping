package com.example.myshopapp.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "sale_items",
    indices = [Index("saleDocumentId")]
)
data class SaleItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val saleDocumentId: String,
    val itemCode: String,
    val itemName: String,
    val quantity: Double,
    val price: Double,
    val sum: Double,
    val vatPercent: Double?
)