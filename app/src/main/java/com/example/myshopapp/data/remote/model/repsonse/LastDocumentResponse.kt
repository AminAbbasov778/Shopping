package com.example.myshopapp.data.remote.model.repsonse


import com.google.gson.annotations.SerializedName

data class LastDocumentResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String
)