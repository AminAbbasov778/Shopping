package com.example.myshopapp.presentation.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.myshopapp.databinding.FragmentSaleDetailBinding
import com.example.myshopapp.presentation.state.SaleDetailUiState
import com.example.myshopapp.presentation.util.SaleStatus
import com.example.myshopapp.presentation.util.Util.formatDate
import com.example.myshopapp.presentation.util.collectEvents
import com.example.myshopapp.presentation.viewmodel.SaleDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SaleDetailFragment : Fragment() {

    private var _binding: FragmentSaleDetailBinding? = null
    private val binding get() = _binding!!

   lateinit var documentId : String

    private val viewModel: SaleDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSaleDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

         documentId = requireArguments().getString("documentId") ?: return

        viewModel.load(documentId)

        setupButtons()
        collectEvents(viewModel)
        observeState()
    }

    private fun setupButtons() {
        binding.btnRefund.setOnClickListener {
            viewModel.rollbackOrRefund()
        }

        binding.btnReprint.setOnClickListener {
            viewModel.reprint()
        }
    }

    private fun renderButton(state: SaleDetailUiState) {

        when {
            state.saleFull?.sale?.status == SaleStatus.REFUNDED -> {
                binding.btnRefund.text = "Refunded"
                binding.btnRefund.isEnabled = false
            }

            state.saleFull?.sale?.status == SaleStatus.ROLLED_BACK -> {
                binding.btnRefund.text = "Rolled Back"
                binding.btnRefund.isEnabled = false
            }

            state.canRollback -> {
                binding.btnRefund.text = "Rollback"
                binding.btnRefund.isEnabled = true
            }

            state.canRefund -> {
                binding.btnRefund.text = "Refund"
                binding.btnRefund.isEnabled = true
            }
            state.saleFull?.sale?.status == SaleStatus.COMPLETED -> {
                binding.btnRefund.isEnabled = false
                viewModel.getStatus()
            }

            else -> {
                binding.btnRefund.isEnabled = false
            }
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->

                binding.progressBar.visibility =
                    if (state.isLoading) View.VISIBLE else View.GONE

                renderButton(state)

                state.saleFull?.let { full ->
                    val sale = full.sale

                    binding.tvDocumentId.text = sale.shortDocumentId
                    binding.tvTotal.text = "${sale.total} ${sale.currency}"
                    binding.tvCash.text = sale.cashSum.toString()
                    binding.tvCard.text = sale.cardSum.toString()
                    binding.tvBonus.text = sale.bonusSum.toString()
                    binding.tvCashier.text = sale.cashier
                    binding.tvRrn.text = sale.rrn
                    binding.tvDate.text = formatDate(sale.createdAt)

                    binding.tvItems.text = full.items.joinToString("\n") {
                        "${it.itemName} x${it.quantity} = ${it.sum}"
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}