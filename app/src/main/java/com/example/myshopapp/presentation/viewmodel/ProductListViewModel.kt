package com.example.myshopapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.myshopapp.util.LogTags
import com.example.myshopapp.data.local.entity.ProductEntity
import com.example.myshopapp.domain.usecase.DeleteProductUseCase
import com.example.myshopapp.domain.usecase.GetProductsUseCase
import com.example.myshopapp.presentation.base.BaseViewModel
import com.example.myshopapp.presentation.state.ProductListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val deleteProductUseCase: DeleteProductUseCase
) : BaseViewModel() {

    private val _state = MutableStateFlow(ProductListUiState())
    val state = _state.asStateFlow()

    private var allProducts: List<ProductEntity> = emptyList()

    fun loadProducts() {
        viewModelScope.launch {

            _state.update { it.copy(isLoading = true) }

            val result = getProductsUseCase()
            result.onSuccess {
                allProducts = it

                _state.update { state ->

                    state.copy(
                        isLoading = false,
                        products = allProducts,
                        filteredProducts = allProducts
                    )
                }

            }.onFailure {

                _state.update { it.copy(isLoading = false) }
                emitError(result.exceptionOrNull()?.message)
            }
        }
    }

    fun search(query: String) {
        val filtered = if (query.isBlank()) {
            allProducts
        } else {
            allProducts.filter { product ->
                product.name.contains(query, true) ||
                        product.code.contains(query, true) ||
                        product.barcode.contains(query, true)
            }
        }

        _state.update { it.copy(filteredProducts = filtered) }

    }

    fun deleteProduct(product: ProductEntity) {
        viewModelScope.launch {

            val result = deleteProductUseCase(product)

            result.onSuccess {

                loadProducts()
            }.onFailure {
                emitError(result.exceptionOrNull()?.message)
            }
        }
    }
}