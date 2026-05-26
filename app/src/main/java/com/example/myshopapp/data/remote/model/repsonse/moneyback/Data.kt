package com.example.myshopapp.data.remote.model.repsonse.moneyback


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("document_id")
    val documentId: String,
    @SerializedName("document_number")
    val documentNumber: Int,
    @SerializedName("shift_document_number")
    val shiftDocumentNumber: Int,
    @SerializedName("short_document_id")
    val shortDocumentId: String
)