package com.example.myshopapp.di

import com.example.myshopapp.data.repository.AuthRepositoryImpl
import com.example.myshopapp.data.repository.ProductRepositoryImpl
import com.example.myshopapp.data.repository.SaleRepositoryImpl
import com.example.myshopapp.data.repository.ShiftRepositoryImpl
import com.example.myshopapp.domain.repository.AuthRepository
import com.example.myshopapp.domain.repository.ProductRepository
import com.example.myshopapp.domain.repository.SaleRepository
import com.example.myshopapp.domain.repository.ShiftRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindShiftRepository(impl: ShiftRepositoryImpl): ShiftRepository

    @Binds
    @Singleton
    abstract fun bindSaleRepository(impl: SaleRepositoryImpl): SaleRepository

    @Binds
    @Singleton
    abstract fun bindProductRepository(impl: ProductRepositoryImpl): ProductRepository
}
