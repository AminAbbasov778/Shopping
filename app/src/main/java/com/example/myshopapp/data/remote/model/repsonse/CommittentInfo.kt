package com.example.myshopapp.data.remote.model.repsonse


import com.google.gson.annotations.SerializedName

data class CommittentInfo(
    @SerializedName("agentCommission")
    val agentCommission: Int,
    @SerializedName("agentContract")
    val agentContract: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("tin")
    val tin: String
)