package com.example.myshopapp.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class SaleFull(
    @Embedded val sale: SaleEntity,

    @Relation(
        parentColumn = "documentId",
        entityColumn = "saleDocumentId"
    )
    val items: List<SaleItemEntity>,

    @Relation(
        parentColumn = "documentId",
        entityColumn = "saleDocumentId"
    )
    val vat: List<SaleVatEntity>
)