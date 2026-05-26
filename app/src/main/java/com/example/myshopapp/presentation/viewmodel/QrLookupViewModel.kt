package com.example.myshopapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.myshopapp.data.local.entity.SaleEntity
import com.example.myshopapp.domain.usecase.GetSaleFullByQrUseCase
import com.example.myshopapp.presentation.base.BaseViewModel
import com.example.myshopapp.presentation.state.QrScanUiState
import com.example.myshopapp.presentation.util.Util.extractDocumentId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QrLookupViewModel @Inject constructor(
    private val getSaleByQr: GetSaleFullByQrUseCase
) : BaseViewModel() {

    private val _state = MutableStateFlow(QrScanUiState())
    val state = _state.asStateFlow()

    private var isProcessing = false
    private var lastScannedQr: String? = null

    fun scan(rawQr: String) {
        if (rawQr.isBlank()) return
        if (isProcessing) return

        val cleanDocumentId = extractDocumentId(rawQr)

        if (cleanDocumentId == lastScannedQr) return

        isProcessing = true
        lastScannedQr = cleanDocumentId

        _state.update {
            it.copy(isLoading = true)
        }

        viewModelScope.launch {
            getSaleByQr(cleanDocumentId)
                .onSuccess { saleFull ->
                    val limit = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000)

                    if (saleFull == null) {
                        _state.update { it.copy(isLoading = false, sale = null) }
                        isProcessing = false
                        lastScannedQr = null
                        return@onSuccess
                    }

                    if (saleFull.sale.createdAt >= limit) {
                        _state.update { state ->
                            state.copy(isLoading = false, sale = saleFull.sale)
                        }
                    } else {
                        _state.update { it.copy(isLoading = false, sale = null) }
                        lastScannedQr = null
                    }
                    isProcessing = false
                }
                .onFailure { throwable ->
                    _state.update { it.copy(isLoading = false, sale = null) }
                    emitError(throwable.message ?: "Xəta baş verdi")
                    isProcessing = false
                    lastScannedQr = null
                }
        }
    }



    fun clear() {
        _state.update { QrScanUiState() }
        isProcessing = false
        lastScannedQr = null
    }
}