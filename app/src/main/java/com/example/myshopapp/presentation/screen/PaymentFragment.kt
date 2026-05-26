package com.example.myshopapp.presentation.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.myshopapp.R
import com.example.myshopapp.databinding.FragmentPaymentBinding
import com.example.myshopapp.presentation.mapper.buildSaleRequest
import com.example.myshopapp.presentation.util.collectEvents
import com.example.myshopapp.presentation.viewmodel.CartViewModel
import com.example.myshopapp.presentation.viewmodel.PaymentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PaymentFragment : Fragment() {

    private lateinit var binding: FragmentPaymentBinding

    private val viewModel: PaymentViewModel by viewModels()
    private val cartViewModel: CartViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cart = cartViewModel.state.value
        viewModel.setTotal(cart.total)

        setupInputs()
        setupPayButton()
        observeState()
    }

    private fun setupInputs() {
        binding.etCash.addTextChangedListener {
            viewModel.setCash(it.toString())
        }
        binding.etCard.addTextChangedListener {
            viewModel.setCard(it.toString())
        }


        binding.etBonus.addTextChangedListener {
            viewModel.setBonus(it.toString())
        }



    }

    private fun setupPayButton() {
        binding.btnPay.setOnClickListener {
            val cart = cartViewModel.state.value
            val request = buildSaleRequest(cart, viewModel.state.value)
            viewModel.submitSale(request, cart.items)
        }
    }

    private fun observeState() {
        collectEvents(
            viewModel = viewModel,
            onSuccess = {
                cartViewModel.clearCart()
            },
            onNavigateBack = {
                findNavController().popBackStack(R.id.saleFragment2, false)

            }


        )

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                binding.tvTotal.text = "Cəmi: %.2f ₼".format(state.total)

                binding.tvPaid.text = "Ödənilib: %.2f ₼".format(state.paid)
                binding.tvRemaining.text = "Qalıq: %.2f ₼".format(state.remaining)


                binding.tvChange.text = "Qaytarılacaq: %.2f ₼".format(state.change)

                binding.btnPay.isEnabled = !state.isLoading

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}