package com.example.myshopapp.di

import android.content.Context
import androidx.room.Room
import com.example.myshopapp.AppDatabase
import com.example.myshopapp.data.local.dao.ProductDao
import com.example.myshopapp.data.local.dao.SaleDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDb(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "shop_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideDao(db: AppDatabase): ProductDao {
        return db.productDao()
    }


    @Provides
    @Singleton
    fun provideSaleDao(db: AppDatabase): SaleDao {
        return db.saleDao()
    }

}