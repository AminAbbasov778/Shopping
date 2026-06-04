package com.example.myshopapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.myshopapp.data.local.entity.SaleEntity
import com.example.myshopapp.data.local.entity.SaleItemEntity
import com.example.myshopapp.data.local.entity.SaleFull
import com.example.myshopapp.presentation.util.SaleStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface SaleDao {

    @Insert
    suspend fun insertSale(sale: SaleEntity): Long

    @Insert
    suspend fun insertItems(items: List<SaleItemEntity>)



    @Query("""
        UPDATE sales 
        SET status = :status 
        WHERE documentId = :documentId
    """)
    suspend fun updateStatus(
        documentId: String,
        status: SaleStatus
    )

    @Query("""
        UPDATE sale_items 
        SET quantity = :quantity, sum = :sum
        WHERE id = :itemId AND saleDocumentId = :saleDocumentId
    """)
    suspend fun updateItemQuantityAndSum(
        itemId: Long, quantity: Double, sum: Double, saleDocumentId: String
    )

    @Query("""
        UPDATE sales
        SET total = :total,
            cashSum = :cashSum,
            cardSum = :cardSum,
            bonusSum = :bonusSum,
            creditSum = :creditSum,
            prepaymentSum = :prepaymentSum
        WHERE documentId = :documentId
    """)
    suspend fun updateSaleTotals(
        documentId: String,
        total: Double,
        cashSum: Double,
        cardSum: Double,
        bonusSum: Double,
        creditSum: Double,
        prepaymentSum: Double
    )

    @Transaction
    @Query("SELECT * FROM sales WHERE fullDocumentId = :qr LIMIT 1")
    suspend fun getSaleFullByQr(qr: String): SaleFull?

    @Transaction
    @Query("SELECT * FROM sales WHERE shiftKey = :shiftKey ORDER BY createdAt DESC")
    fun getShiftSalesFull(shiftKey: String): Flow<List<SaleFull>>

    

    @Transaction
    @Query("""
        SELECT * FROM sales 
        WHERE createdAt >= :fromDate 
        ORDER BY createdAt DESC
    """)
    fun getLast30DaysFull(fromDate: Long): Flow<List<SaleFull>>
}