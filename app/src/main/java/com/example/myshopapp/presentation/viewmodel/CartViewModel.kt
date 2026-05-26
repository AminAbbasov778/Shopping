package com.example.myshopapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myshopapp.data.local.entity.ProductEntity
import com.example.myshopapp.presentation.state.CartItem
import com.example.myshopapp.presentation.state.CartUiState
import com.example.myshopapp.presentation.util.CartCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor() : ViewModel() {

    private val _rawItems = MutableStateFlow<List<CartItem>>(emptyList())

    private val _state = MutableStateFlow(CartUiState())
    val state = _state.asStateFlow()

    private var cartDiscountPercent: Double = 0.0

    fun add(product: ProductEntity) {
        val list  = _rawItems.value.toMutableList()
        val index = list.indexOfFirst { it.product.id == product.id }

        if (index >= 0) {
            list[index] = list[index].copy(qty = list[index].qty + 1)
        } else {
            list.add(CartItem(product))
        }

        _rawItems.value = list
        calculate()
    }

    fun minus(productId: Long) {
        val list  = _rawItems.value.toMutableList()
        val index = list.indexOfFirst { it.product.id == productId }

        if (index >= 0) {
            val item = list[index]
            if (item.qty > 1) list[index] = item.copy(qty = item.qty - 1)
            else list.removeAt(index)
        }

        _rawItems.value = list
        calculate()
    }

    fun remove(productId: Long) {
        _rawItems.value = _rawItems.value.filterNot { it.product.id == productId }
        calculate()
    }

    fun setItemDiscount(productId: Long, discount: Double) {
        _rawItems.value = _rawItems.value.map {
            if (it.product.id == productId) it.copy(discount = discount) else it
        }
        calculate()
    }

    fun setCartDiscount(percent: Double) {
        cartDiscountPercent = percent.coerceIn(0.0, 100.0)
        calculate()
    }

    fun clearCart() {
        cartDiscountPercent = 0.0
        _rawItems.value = emptyList()
        _state.value    = CartUiState()
    }

    private fun calculate() {
        val totals = CartCalculator.calculate(_rawItems.value, cartDiscountPercent)

        _state.value = CartUiState(
            items               = totals.calculatedItems,
            cartDiscountPercent = cartDiscountPercent,
            cartDiscountAmount  = totals.cartDiscountAmount,
            total               = totals.total,
            totalDiscount       = totals.totalDiscount,
            vatSummary          = totals.vatMap,
            netTotal            = totals.total,
        )
    }
}