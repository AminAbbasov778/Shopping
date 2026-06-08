package com.example.myshopapp.presentation.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.myshopapp.databinding.FragmentSaleDetailBinding
import com.example.myshopapp.presentation.adapter.SoldProductAdapter
import com.example.myshopapp.presentation.state.SaleDetailUiState
import com.example.myshopapp.presentation.util.SaleStatus
import com.example.myshopapp.presentation.util.Util.formatDate
import com.example.myshopapp.presentation.util.collectEvents
import com.example.myshopapp.presentation.viewmodel.SaleDetailViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SaleDetailFragment : Fragment() {

    private var _binding: FragmentSaleDetailBinding? = null
    private val binding get() = _binding!!

    lateinit var documentId: String
    private val viewModel: SaleDetailViewModel by viewModels()
    lateinit var soldAdapter: SoldProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSaleDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        soldAdapter = SoldProductAdapter(
            onPlus = { viewModel.plus(it) },
            onMinus = { viewModel.minus(it) },
            onRemove = { }
        )

        binding.tvItems.adapter = soldAdapter

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
        binding.btnRollback.setOnClickListener {
            viewModel.rollback()
        }
        binding.btnReprint.setOnClickListener {
            viewModel.reprint()
        }
    }

    private fun renderButtons(state: SaleDetailUiState) {
        val status = state.saleFull?.sale?.status

        when (status) {
            SaleStatus.REFUNDED -> {
                binding.btnRefund.text = "Refunded"
                binding.btnRefund.isEnabled = false
                binding.btnRollback.visibility = View.GONE
                return
            }
            SaleStatus.ROLLED_BACK -> {
                binding.btnRefund.text = "Voided"
                binding.btnRefund.isEnabled = false
                binding.btnRollback.visibility = View.GONE
                return
            }
            SaleStatus.PARTIALLY_REFUNDED -> {
                binding.btnRefund.text = "Refund"
                binding.btnRefund.isEnabled = true
                binding.btnRollback.visibility = View.GONE
                binding.btnRollback.isEnabled = false
                return
            }
            else -> Unit
        }

        // COMPLETED
        if (state.isCashless) {
            binding.btnRollback.visibility = View.GONE
            if (state.canRollback) {
                binding.btnRefund.text = "Rollback"
                binding.btnRefund.isEnabled = true
            } else {
                binding.btnRefund.text = "Refund"
                binding.btnRefund.isEnabled = true
            }
        } else {
            binding.btnRollback.visibility = View.VISIBLE
            binding.btnRollback.isEnabled = true
            binding.btnRefund.text = "Refund"
            binding.btnRefund.isEnabled = true
        }
    }

    private fun showRollbackWarningDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Diqqət")
            .setMessage(
                "Məhsul miqdarlarını dəyişmisiniz, lakin rollback bütün məhsulları tam miqdarla qaytaracaq. Davam etmək istəyirsiniz?"
            )
            .setPositiveButton("Bəli") { dialog, _ ->
                dialog.dismiss()
                viewModel.confirmRollback()
            }
            .setNegativeButton("Xeyr") { dialog, _ ->
                dialog.dismiss()
                viewModel.dismissRollbackWarning()
            }
            .show()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE

                renderButtons(state)

                // Rollback xəbərdarlıq dialoqu
                if (state.showRollbackWarningDialog) {
                    showRollbackWarningDialog()
                }

                state.saleFull?.let { full ->
                    val sale = full.sale
                    binding.tvDocumentId.text = sale.shortDocumentId
                    binding.tvTotal.text = "${sale.total} ${sale.currency}"
                    binding.tvCash.text = "Cash: ${sale.cashSum}"
                    binding.tvCard.text = "Card: ${sale.cardSum}"
                    binding.tvBonus.text = "Bonus: ${sale.bonusSum}"
                    binding.tvCashier.text = "Cashier: ${sale.cashier}"
                    binding.tvRrn.text = "RRN: ${sale.rrn ?: "-"}"
                    binding.tvDate.text = formatDate(sale.createdAt)

                    soldAdapter.submitList(state.updatedItems)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}