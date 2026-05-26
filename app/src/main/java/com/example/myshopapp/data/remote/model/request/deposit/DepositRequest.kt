package com.example.shopapp.data.remote.model.request.deposit


import com.google.gson.annotations.SerializedName

data class DepositRequest(
    @SerializedName("cashier")
    val cashier: String,
    @SerializedName("currency")
    val currency: String,
    @SerializedName("prev_doc_number")
    val prevDocNumber: Int,
    @SerializedName("sum")
    val sum: Double
)