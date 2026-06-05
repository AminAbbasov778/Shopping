package com.example.myshopapp.data.repository

import android.util.Log
import com.example.myshopapp.data.local.dao.SaleDao
import com.example.myshopapp.data.local.entity.SaleEntity
import com.example.myshopapp.data.local.entity.SaleItemEntity
import com.example.myshopapp.data.local.entity.SaleFull
import com.example.myshopapp.data.remote.model.repsonse.LastDocumentResponse
import com.example.myshopapp.data.remote.model.repsonse.deposit.DepositResponse
import com.example.myshopapp.data.remote.model.repsonse.moneyback.MoneyBackResponse
import com.example.myshopapp.data.remote.model.repsonse.print.PrintResponse
import com.example.myshopapp.data.remote.model.repsonse.withdraw.WithDrawResponse
import com.example.myshopapp.data.remote.model.request.moneyback.MoneyBackRequest
import com.example.myshopapp.data.remote.model.request.print.PrintRequest
import com.example.myshopapp.data.remote.service.ApiService
import com.example.myshopapp.domain.repository.SaleRepository
import com.example.myshopapp.data.remote.model.request.rollback.RollbackRequest
import com.example.myshopapp.data.remote.model.request.withdraw.WithDrawRequest
import com.example.shopapp.data.remote.model.request.deposit.DepositRequest
import com.example.shopapp.data.remote.model.request.sale.SaleRequest
import com.example.shopapp.data.remote.model.response.rollback.RollbackResponse
import com.example.shopapp.data.remote.model.response.sale.SaleResponse
import com.example.myshopapp.data.util.safeApiCall
import com.example.myshopapp.data.util.safeDbCall
import com.example.myshopapp.presentation.util.SaleStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SaleRepositoryImpl @Inject constructor(
    private val dao: SaleDao,
    private val api: ApiService
) : SaleRepository {

    override suspend fun updateStatus(documentId: String, status: SaleStatus) : Result<Unit> = safeDbCall {
        dao.updateStatus(documentId, status)
    }

    override suspend fun updateQuantity(quantity: Double, saleDocumentId: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateItemsQuantities(items: List<SaleItemEntity>, saleDocumentId: String): Result<Unit> = safeDbCall {
        items.forEach { item ->
            dao.updateItemQuantityAndSum(
                itemId = item.id,
                quantity = item.quantity,
                sum = item.sum,
                saleDocumentId = saleDocumentId
            )
        }
    }

    override suspend fun updateSaleTotals(
        documentId: String,
        total: Double,
        cashSum: Double,
        cardSum: Double,
        bonusSum: Double,
        creditSum: Double,
        prepaymentSum: Double
    ): Result<Unit> = safeDbCall {
        dao.updateSaleTotals(documentId, total, cashSum, cardSum, bonusSum, creditSum, prepaymentSum)
    }

    override suspend fun getLastDocument(): Result<LastDocumentResponse> = safeApiCall {
        api.getLastDocument()
    }

    override suspend fun withdraw(request: WithDrawRequest): Result<WithDrawResponse> = safeApiCall {
        api.withdraw(request)
    }

    override suspend fun deposit(request: DepositRequest): Result<DepositResponse> = safeApiCall {
        api.deposit(request)
    }

    override suspend fun rollback(request: RollbackRequest): Result<RollbackResponse> = safeApiCall {
        Log.d("TAG", "rollback: $request")

        api.rollback(request)
    }

    override suspend fun submitSale(request: SaleRequest): Result<SaleResponse> = safeApiCall {
        Log.d("TAG", "submitSale: $request")
        api.submitSale(request)
    }

    override suspend fun moneyBack(request: MoneyBackRequest): Result<MoneyBackResponse> = safeApiCall {
        Log.d("TAG", "moneyBack: $request")
        api.moneyBack(request)
    }

    override suspend fun reprintReceipt(printRequest: PrintRequest): Result<PrintResponse> = safeApiCall {
        api.printReceipt(printRequest)
    }

    override suspend fun saveSale(
        sale: SaleEntity,
        items: List<SaleItemEntity>,
    ): Result<Unit> = safeDbCall {
        dao.insertSale(sale)
        dao.insertItems(items)
    }

    override suspend fun getSaleFullByQr(qr: String): Result<SaleFull?> = safeDbCall {
        dao.getSaleFullByQr(qr)
    }

    override fun getShiftSalesFull(shiftKey: String): Flow<Result<List<SaleFull>>> {
        return dao.getShiftSalesFull(shiftKey)
            .map { Result.success(it) }
            .catch { emit(Result.failure(it)) }
    }

    override fun getLast30DaysFull(from: Long): Flow<Result<List<SaleFull>>> {
        return dao.getLast30DaysFull(from)
            .map { Result.success(it) }
            .catch { emit(Result.failure(it)) }
    }
}