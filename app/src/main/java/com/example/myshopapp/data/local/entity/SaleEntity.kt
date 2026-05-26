package com.example.myshopapp.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.myshopapp.presentation.util.SaleStatus
import kotlin.uuid.Uuid


@Entity(
    tableName = "sales",
    indices = [
        Index("documentId"),
        Index("shiftKey"),
        Index("createdAt")
    ]
)
data class SaleEntity(
    @PrimaryKey(autoGenerate = true)
    val localId: Long = 0,

    val documentId: String,
    val shortDocumentId: String,
    val fullDocumentId: String,

    val shiftId: String,
    val shiftKey: String,

    val cashier: String,
    val currency: String,
    val creditSum: Double,
    val prepaymentSum: Double,

    val incomingSum: Double,
    val changeSum: Double,
    val status: SaleStatus ,

    val total: Double,

    val cashSum: Double,
    val cardSum: Double,
    val bonusSum: Double,
    val rrn: String?,
    val uuid: String?,

    val createdAt: Long = System.currentTimeMillis(),
)
