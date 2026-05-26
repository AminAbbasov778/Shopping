package com.example.myshopapp.data.remote.model.repsonse


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("doc")
    val doc: Doc,
    @SerializedName("doc_type")
    val docType: String,
    @SerializedName("document_id")
    val documentId: String
)