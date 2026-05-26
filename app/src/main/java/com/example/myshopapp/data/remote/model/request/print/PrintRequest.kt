package com.example.myshopapp.data.remote.model.request.print


import com.google.gson.annotations.SerializedName

data class PrintRequest(
    @SerializedName("fiscalId")
    val fiscalId: String
)