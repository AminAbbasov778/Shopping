package com.example.shopapp.data.remote.model.response.rollback


import com.google.gson.annotations.SerializedName

data class RollbackResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("rrn")
    val rrn: String,
    @SerializedName("uuid")
    val uuid: String
)