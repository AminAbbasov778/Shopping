package com.example.myshopapp.data.remote.model.request.sale


import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("itemCode")
    val itemCode: String,
    @SerializedName("itemCodeType")
    val itemCodeType: Int,
    @SerializedName("itemDiscountPrice")
    val itemDiscountPrice: Double?,
    @SerializedName("itemName")
    val itemName: String,
    @SerializedName("itemPrice")
    val itemPrice: Double,
    @SerializedName("itemQuantity")
    val itemQuantity: Double,
    @SerializedName("itemQuantityType")
    val itemQuantityType: Int,
    @SerializedName("itemSum")
    val itemSum: Double,
    @SerializedName("itemVatPercent")
    val itemVatPercent: Double?,
    @SerializedName("itemMarginPrice")
    val itemMarginPrice: Double?,
    @SerializedName("itemMarginSum")
    val itemMarginSum: Double?,
    @SerializedName("itemCreditpaySum")
    val itemCreditpaySum: Double?,

    )