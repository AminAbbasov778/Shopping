package com.example.myshopapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.myshopapp.util.LogTags
import com.example.myshopapp.data.local.entity.ProductEntity
import com.example.myshopapp.domain.usecase.AddProductUseCase
import com.example.myshopapp.domain.usecase.GetProductsUseCase
import com.example.myshopapp.presentation.base.BaseViewModel
import com.example.myshopapp.presentation.mapper.mapToProductEntity
import com.example.myshopapp.presentation.state.ProductUiState
import com.example.myshopapp.presentation.util.SaleValidator
import com.example.myshopapp.presentation.util.roundTo2
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val addProductUseCase: AddProductUseCase,
    private val getProductsUseCase: GetProductsUseCase
) : BaseViewModel() {

    private val _formState = MutableStateFlow(ProductUiState())
    val formState = _formState.asStateFlow()

    private var editProductId: Long = -1L

    fun loadProduct(productId: Long) {
        if (_formState.value.isFormPopulated) return

        this.editProductId = productId
        viewModelScope.launch {
            _formState.update { it.copy(isLoading = true) }

            getProductsUseCase()
                .onSuccess { list ->
                    val product = list.firstOrNull { it.id == productId }
                    _formState.update {
                        it.copy(isLoading = false, editProduct = product)
                    }
                }
                .onFailure {
                    _formState.update { it.copy(isLoading = false) }
                    emitError(it.message)
                }
        }
    }

    fun setFormPopulated() {
        _formState.update { it.copy(isFormPopulated = true) }
    }

    fun validateAndSaveProduct(
        name: String,
        code: String,
        barcode: String,
        salePriceStr: String,
        purchasePrice: Double,
        isAgro: Boolean,
        vatStr: String
    ) {
        _formState.update {
            it.copy(
                nameError = null, codeError = null, barcodeError = null,
                salePriceError = null, vatError = null, purchasePriceError = null
            )
        }

        SaleValidator.validateItemName(name)?.let { error ->
            _formState.update { it.copy(nameError = error) }
            return
        }

        SaleValidator.validateItemCode(code)?.let { error ->
            _formState.update { it.copy(codeError = error) }
            return
        }

        if (barcode.isNotBlank()) {
            val codeType = when (barcode.length) {
                8 -> 1
                13 -> 2
                else -> 0
            }
            SaleValidator.validateItemCodeType(codeType, barcode)?.let { error ->
                _formState.update { it.copy(barcodeError = error) }
                return
            }
        }

        val salePrice = salePriceStr.toDoubleOrNull()?.roundTo2()
        if (salePrice == null || salePrice < 0) {
            _formState.update { it.copy(salePriceError = "Düzgün satış qiyməti daxil edin") }
            return
        }

        val vatPercent: Double
        if (isAgro) {
            vatPercent = 0.0
        } else {
            val parsed = vatStr.toDoubleOrNull() ?: 18.0
            SaleValidator.validateItemVatPercent(parsed)?.let { error ->
                _formState.update { it.copy(vatError = error) }
                return
            }
            vatPercent = parsed
        }
        val purchasePriceRounded = purchasePrice.roundTo2()


        if (isAgro && purchasePriceRounded <= 0) {
            _formState.update { it.copy(purchasePriceError = "Agro məhsul üçün alış qiyməti mütləqdir və 0-dan böyük olmalıdır") }
            return
        }

        if (purchasePrice > 0 && purchasePriceRounded >= salePrice) {
            _formState.update { it.copy(purchasePriceError = "Alış qiyməti satış qiymətindən kiçik olmalıdır") }
            return
        }

        val productId = if (editProductId != -1L) editProductId else 0L
        val product = mapToProductEntity(
            id            = productId,
            name          = name,
            code          = code,
            barcode       = barcode,
            salePrice     = salePrice,
            purchasePrice = purchasePriceRounded,
            vatPercent    = vatPercent,
            isAgro        = isAgro
        )

        executeProductSave(product)
    }

    private fun executeProductSave(product: ProductEntity) {
        viewModelScope.launch {
            _formState.update { it.copy(isLoading = true) }

            addProductUseCase(product)
                .onSuccess {
                    _formState.update { it.copy(isLoading = false) }
                    emitNavigateBack()
                }
                .onFailure {
                    _formState.update { it.copy(isLoading = false) }
                    emitError(it.message)
                }
        }
    }
}