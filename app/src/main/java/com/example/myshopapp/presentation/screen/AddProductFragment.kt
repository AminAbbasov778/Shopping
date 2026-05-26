package com.example.myshopapp.presentation.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.myshopapp.data.local.entity.ProductEntity
import com.example.myshopapp.databinding.FragmentAddProductBinding
import com.example.myshopapp.presentation.state.ProductUiState
import com.example.myshopapp.presentation.util.collectEvents
import com.example.myshopapp.presentation.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddProductFragment : Fragment() {

    private lateinit var binding: FragmentAddProductBinding

    private val viewModel: ProductViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAddProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editProductId = arguments?.getLong("productId", -1L) ?: -1L
        if (editProductId != -1L) {
            viewModel.loadProduct(editProductId)
        }

        setupAgroSwitch()
        observeState()
        setupClicks()

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun setupAgroSwitch() {
        binding.switchAgro.setOnCheckedChangeListener { _, isChecked ->
            applyAgroVatState(isChecked)
        }


    }

    private fun applyAgroVatState(isAgro: Boolean) {
        with(binding.etVat) {
            if (isAgro) {
                setText("0")
                isEnabled = false
                alpha = 0.4f
            } else {
                isEnabled = true
                alpha = 1.0f
                if (text.toString() == "0") {
                    setText("18")
                }
            }
        }
    }

    private fun observeState() {
        collectEvents(viewModel)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.formState.collect { state ->

                binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
                binding.btnSave.isEnabled = !state.isLoading

                if (state.editProduct != null && !state.isFormPopulated) {
                    populateForm(state.editProduct)
                    viewModel.setFormPopulated()
                }

                handleValidationErrors(state)
            }
        }
    }

    private fun populateForm(product: ProductEntity) {
        with(binding) {
            etName.setText(product.name)



            etCode.setText(product.code)
            etBarcode.setText(product.barcode)

            etSalePrice.setText(product.salePrice.toString())
            etPurchasePrice.setText(product.purchasePrice.toString())

            etVat.setText(product.vatPercent.toString())
            switchAgro.isChecked = product.isAgro
            applyAgroVatState(product.isAgro)
        }
    }

    private fun handleValidationErrors(state: ProductUiState) {
        with(binding) {
            etName.error = state.nameError


            state.nameError?.let {
                etName.requestFocus()
            }

            etCode.error = state.codeError
            state.codeError?.let {
                etCode.requestFocus()
            }

            etBarcode.error = state.barcodeError


            state.barcodeError?.let {
                etBarcode.requestFocus()
            }

            etSalePrice.error = state.salePriceError
            state.salePriceError?.let {


                etSalePrice.requestFocus()
            }

            etVat.error = state.vatError
            state.vatError?.let {
                etVat.requestFocus()
            }

            etPurchasePrice.error = state.purchasePriceError
            state.purchasePriceError?.let {
                etPurchasePrice.requestFocus()
            }
        }
    }

    private fun setupClicks() {
        binding.btnSave.setOnClickListener {
            viewModel.validateAndSaveProduct(
                name = binding.etName.text.toString().trim(),

                code = binding.etCode.text.toString().trim(),

                barcode = binding.etBarcode.text.toString().trim(),

                salePriceStr = binding.etSalePrice.text.toString().trim(),

                purchasePrice = binding.etPurchasePrice.text.toString().toDoubleOrNull() ?: 0.0,

                isAgro = binding.switchAgro.isChecked, vatStr = binding.etVat.text.toString().trim()
            )
        }
    }

}