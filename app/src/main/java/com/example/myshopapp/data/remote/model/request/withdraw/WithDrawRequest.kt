package com.example.myshopapp.data.remote.model.request.withdraw


import com.google.gson.annotations.SerializedName

data class WithDrawRequest(
    @SerializedName("cashier")
    val cashier: String,
    @SerializedName("currency")
    val currency: String,
    @SerializedName("prev_doc_number")
    val prevDocNumber: Int,
    @SerializedName("sum")
    val sum: Double
)