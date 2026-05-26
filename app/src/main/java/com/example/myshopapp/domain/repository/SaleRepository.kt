package com.example.myshopapp.domain.repository

import com.example.myshopapp.data.local.entity.SaleEntity
import com.example.myshopapp.data.local.entity.SaleItemEntity
import com.example.myshopapp.data.local.entity.SaleVatEntity
import com.example.myshopapp.data.local.entity.SaleFull
import com.example.myshopapp.data.remote.model.repsonse.LastDocumentResponse
import com.example.myshopapp.data.remote.model.repsonse.deposit.DepositResponse
import com.example.myshopapp.data.remote.model.repsonse.moneyback.MoneyBackResponse
import com.example.myshopapp.data.remote.model.repsonse.print.PrintResponse
import com.example.myshopapp.data.remote.model.repsonse.withdraw.WithDrawResponse
import com.example.myshopapp.data.remote.model.request.moneyback.MoneyBackRequest
import com.example.myshopapp.data.remote.model.request.print.PrintRequest
import com.example.myshopapp.data.remote.model.request.rollback.RollbackRequest
import com.example.myshopapp.data.remote.model.request.withdraw.WithDrawRequest
import com.example.myshopapp.presentation.util.SaleStatus
import com.example.shopapp.data.remote.model.request.deposit.DepositRequest
import com.example.shopapp.data.remote.model.request.sale.SaleRequest
import com.example.shopapp.data.remote.model.response.rollback.RollbackResponse
import com.example.shopapp.data.remote.model.response.sale.SaleResponse
import kotlinx.coroutines.flow.Flow

interface SaleRepository {


    suspend fun saveSale(
        sale: SaleEntity,
        items: List<SaleItemEntity>,
        vat: List<SaleVatEntity>
    ): Result<Unit>

    suspend fun updateStatus(documentId: String, status: SaleStatus) : Result<Unit>


    suspend fun getLastDocument(

    ): Result<LastDocumentResponse>

    suspend fun rollback(
        request: RollbackRequest
    ): Result<RollbackResponse>

    suspend fun deposit(
        request: DepositRequest
    ): Result<DepositResponse>

    suspend fun withdraw(
        request: WithDrawRequest
    ): Result<WithDrawResponse>

    suspend fun getSaleFullByQr(qr: String): Result<SaleFull?>


    fun getShiftSalesFull(shiftKey: String): Flow<Result<List<SaleFull>>>

    fun getLast30DaysFull(from: Long): Flow<Result<List<SaleFull>>>


    suspend fun submitSale(request: SaleRequest): Result<SaleResponse>

    suspend fun moneyBack(request: MoneyBackRequest): Result<MoneyBackResponse>

    suspend fun reprintReceipt(printRequest: PrintRequest): Result<PrintResponse>
}