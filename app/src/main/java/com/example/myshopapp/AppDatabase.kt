package com.example.myshopapp

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myshopapp.data.local.dao.ProductDao
import com.example.myshopapp.data.local.dao.SaleDao
import com.example.myshopapp.data.local.entity.ProductEntity
import com.example.myshopapp.data.local.entity.SaleEntity
import com.example.myshopapp.data.local.entity.SaleItemEntity
import com.example.myshopapp.data.local.entity.SaleVatEntity

@Database(
    entities = [ProductEntity::class, SaleEntity::class, SaleItemEntity::class, SaleVatEntity::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun saleDao(): SaleDao
}