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

            Log.d(LogTags.PRODUCT, "ViewModel: loadProducts called")

            _state.value = _state.value.copy(isLoading = true)

         val result = getProductsUseCase()
         result.onSuccess{

                    allProducts = it

                    _state.value = _state.value.copy(
                        isLoading = false,
                        products = allProducts,
                        filteredProducts = allProducts
                    )

                    Log.d(LogTags.PRODUCT, "ViewModel: loaded ${allProducts.size} products")
                }

             .onFailure {

                    Log.e(LogTags.PRODUCT, "ViewModel: load error -> ${result.exceptionOrNull()?.message}")

                    _state.value = _state.value.copy(isLoading = false)
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

        _state.value = _state.value.copy(filteredProducts = filtered)

        Log.d(LogTags.PRODUCT, "ViewModel: search -> $query result=${filtered.size}")
    }


    fun deleteProduct(product: ProductEntity) {
        viewModelScope.launch {

            Log.d(LogTags.PRODUCT, "ViewModel: delete product id=${product.id}")

         val result = deleteProductUseCase(product)

             result.onSuccess {

                    Log.d(LogTags.PRODUCT, "ViewModel: delete success")

                    loadProducts()
                }

              .onFailure {

                    Log.e(LogTags.PRODUCT, "ViewModel: delete error -> ${result.exceptionOrNull()?.message}")

                    emitError(result.exceptionOrNull()?.message)
                }
            }

    }
}