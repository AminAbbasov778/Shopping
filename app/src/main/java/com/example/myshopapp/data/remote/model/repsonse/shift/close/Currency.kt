package com.example.myshopapp.data.remote.model.repsonse.shift.close


import com.google.gson.annotations.SerializedName

data class Currency(
    @SerializedName("correctionBonusSum")
    val correctionBonusSum: Double,
    @SerializedName("correctionCashSum")
    val correctionCashSum: Double,
    @SerializedName("correctionCashlessSum")
    val correctionCashlessSum: Double,
    @SerializedName("correctionCount")
    val correctionCount: Int,
    @SerializedName("correctionCreditSum")
    val correctionCreditSum: Double,
    @SerializedName("correctionPrepaymentSum")
    val correctionPrepaymentSum: Double,
    @SerializedName("correctionSum")
    val correctionSum: Double,
    @SerializedName("correctionVatAmounts")
    val correctionVatAmounts: List<com.example.myshopapp.data.remote.model.repsonse.shift.close.CorrectionVatAmount>,
    @SerializedName("creditpayBonusSum")
    val creditpayBonusSum: Double,
    @SerializedName("creditpayCashSum")
    val creditpayCashSum: Double,
    @SerializedName("creditpayCashlessSum")
    val creditpayCashlessSum: Double,
    @SerializedName("creditpayCount")
    val creditpayCount: Int,
    @SerializedName("creditpayCreditSum")
    val creditpayCreditSum: Double,
    @SerializedName("creditpayPrepaymentSum")
    val creditpayPrepaymentSum: Double,
    @SerializedName("creditpaySum")
    val creditpaySum: Double,
    @SerializedName("creditpayVatAmounts")
    val creditpayVatAmounts: List<com.example.myshopapp.data.remote.model.repsonse.shift.close.CreditpayVatAmount>,
    @SerializedName("currency")
    val currency: String,
    @SerializedName("depositCount")
    val depositCount: Int,
    @SerializedName("depositSum")
    val depositSum: Double,
    @SerializedName("moneyBackBonusSum")
    val moneyBackBonusSum: Double,
    @SerializedName("moneyBackCashSum")
    val moneyBackCashSum: Double,
    @SerializedName("moneyBackCashlessSum")
    val moneyBackCashlessSum: Double,
    @SerializedName("moneyBackCount")
    val moneyBackCount: Int,
    @SerializedName("moneyBackCreditSum")
    val moneyBackCreditSum: Double,
    @SerializedName("moneyBackPrepaymentSum")
    val moneyBackPrepaymentSum: Double,
    @SerializedName("moneyBackSum")
    val moneyBackSum: Double,
    @SerializedName("moneyBackVatAmounts")
    val moneyBackVatAmounts: List<com.example.myshopapp.data.remote.model.repsonse.shift.close.MoneyBackVatAmount>,
    @SerializedName("prepayBonusSum")
    val prepayBonusSum: Double,
    @SerializedName("prepayCashSum")
    val prepayCashSum: Double,
    @SerializedName("prepayCashlessSum")
    val prepayCashlessSum: Double,
    @SerializedName("prepayCount")
    val prepayCount: Int,
    @SerializedName("prepayCreditSum")
    val prepayCreditSum: Double,
    @SerializedName("prepayPrepaymentSum")
    val prepayPrepaymentSum: Double,
    @SerializedName("prepaySum")
    val prepaySum: Double,
    @SerializedName("prepayVatAmounts")
    val prepayVatAmounts: List<com.example.myshopapp.data.remote.model.repsonse.shift.close.PrepayVatAmount>,
    @SerializedName("rollbackBonusSum")
    val rollbackBonusSum: Double,
    @SerializedName("rollbackCashSum")
    val rollbackCashSum: Double,
    @SerializedName("rollbackCashlessSum")
    val rollbackCashlessSum: Double,
    @SerializedName("rollbackCount")
    val rollbackCount: Int,
    @SerializedName("rollbackCreditSum")
    val rollbackCreditSum: Double,
    @SerializedName("rollbackPrepaymentSum")
    val rollbackPrepaymentSum: Double,
    @SerializedName("rollbackSum")
    val rollbackSum: Double,
    @SerializedName("rollbackVatAmounts")
    val rollbackVatAmounts: List<com.example.myshopapp.data.remote.model.repsonse.shift.close.RollbackVatAmount>,
    @SerializedName("saleBonusSum")
    val saleBonusSum: Double,
    @SerializedName("saleCashSum")
    val saleCashSum: Double,
    @SerializedName("saleCashlessSum")
    val saleCashlessSum: Double,
    @SerializedName("saleCount")
    val saleCount: Int,
    @SerializedName("saleCreditSum")
    val saleCreditSum: Double,
    @SerializedName("salePrepaymentSum")
    val salePrepaymentSum: Double,
    @SerializedName("saleSum")
    val saleSum: Double,
    @SerializedName("saleVatAmounts")
    val saleVatAmounts: List<com.example.myshopapp.data.remote.model.repsonse.shift.close.SaleVatAmount>,
    @SerializedName("withdrawCount")
    val withdrawCount: Int,
    @SerializedName("withdrawSum")
    val withdrawSum: Double
)