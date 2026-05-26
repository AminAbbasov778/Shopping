package com.example.myshopapp.data.remote.service

import com.example.myshopapp.data.remote.model.repsonse.LastDocumentResponse
import com.example.myshopapp.data.remote.model.repsonse.deposit.DepositResponse
import com.example.myshopapp.data.remote.model.repsonse.login.LoginResponse
import com.example.myshopapp.data.remote.model.repsonse.moneyback.MoneyBackResponse
import com.example.myshopapp.data.remote.model.repsonse.print.PrintResponse
import com.example.myshopapp.data.remote.model.repsonse.shift.close.ShiftClose
import com.example.myshopapp.data.remote.model.repsonse.shift.open.ShiftOpen
import com.example.myshopapp.data.remote.model.repsonse.shift.status.ShiftStatus
import com.example.myshopapp.data.remote.model.repsonse.withdraw.WithDrawResponse
import com.example.myshopapp.data.remote.model.request.moneyback.MoneyBackRequest
import com.example.myshopapp.data.remote.model.request.print.PrintRequest
import com.example.myshopapp.data.remote.model.request.rollback.RollbackRequest
import com.example.myshopapp.data.remote.model.request.withdraw.WithDrawRequest
import com.example.shopapp.data.remote.model.request.deposit.DepositRequest
import com.example.shopapp.data.remote.model.request.sale.SaleRequest
import com.example.shopapp.data.remote.model.response.rollback.RollbackResponse
import com.example.shopapp.data.remote.model.response.sale.SaleResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("kas_login")
    suspend fun login(): Response<LoginResponse>


    @GET("kas_shift")
    suspend fun getShift(): Response<ShiftStatus>

    @GET("kas_openshift")
    suspend fun openShift(): Response<ShiftOpen>

    @GET("kas_closeshift")
    suspend fun closeShift(): Response<ShiftClose>

    @POST("kas_sale")
    suspend fun submitSale(
        @Body request: SaleRequest,
    ): Response<SaleResponse>


    @POST("kas_receipt_copy")
    suspend fun printReceipt(

        @Body body: PrintRequest,
    ): Response<PrintResponse>


    @POST("kas_moneyback")
    suspend fun moneyBack(
        @Body request: MoneyBackRequest,
    ): Response<MoneyBackResponse>

    @POST("kas_rollback")
    suspend fun rollback(
        @Body request: RollbackRequest,
    ): Response<RollbackResponse>

    @POST("kas_depo")
    suspend fun deposit(
        @Body request: DepositRequest,
    ): Response<DepositResponse>


    @POST("kas_withdraw")
    suspend fun withdraw(
        @Body request: WithDrawRequest,
    ): Response<WithDrawResponse>


        @GET("kas_lastDocument")
        suspend fun getLastDocument(
        ): Response<LastDocumentResponse>


}