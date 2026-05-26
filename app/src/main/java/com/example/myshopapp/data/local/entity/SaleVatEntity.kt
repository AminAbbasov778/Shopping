package com.example.myshopapp.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "sale_vat",
    indices = [Index("saleDocumentId")]
)
data class SaleVatEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val saleDocumentId: String,
    val vatPercent: Double?,
    val vatSum: Double
)