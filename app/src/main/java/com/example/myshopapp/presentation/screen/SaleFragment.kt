package com.example.myshopapp.presentation.screen

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshopapp.R
import com.example.myshopapp.databinding.FragmentSaleBinding
import com.example.myshopapp.presentation.adapter.CartAdapter
import com.example.myshopapp.presentation.viewmodel.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SaleFragment : Fragment() {

    private lateinit var binding: FragmentSaleBinding
    private val cartViewModel: CartViewModel by activityViewModels()
    private lateinit var adapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSaleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecycler()
        observeCart()
        setupCartDiscount()

        binding.btnCheckout.setOnClickListener {
            if (cartViewModel.state.value.items.isNotEmpty()) {
                findNavController().navigate(R.id.action_saleFragment2_to_paymentFragment)
            }
        }

        binding.btnClear.setOnClickListener {
            cartViewModel.clearCart()
            binding.etCartDiscount.setText("")
        }
    }

    private fun setupRecycler() {
        adapter = CartAdapter(
            onPlus = {
                cartViewModel.add(it.product)
            },
            onMinus = {
                cartViewModel.minus(it.product.id)
            },

            onRemove = { cartViewModel.remove(it.product.id) },

            onDiscount = { id, d -> cartViewModel.setItemDiscount(id, d) }
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@SaleFragment.adapter
        }
    }

    private fun setupCartDiscount() {
        binding.etCartDiscount.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                val percent = s?.toString()?.toDoubleOrNull() ?: 0.0
                cartViewModel.setCartDiscount(percent)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun observeCart() {
        viewLifecycleOwner.lifecycleScope.launch {
            cartViewModel.state.collect { state ->

                adapter.submitList(state.items)

                binding.tvTotal.text = "Cəmi: ${"%.2f".format(state.total)} ₼"

                binding.tvDiscount.text = "Endirim: ${"%.2f".format(state.totalDiscount)} ₼"

                val vatText = if (state.vatSummary.isEmpty()) {
                    "ƏDV: 0.00 ₼"
                } else {
                    state.vatSummary.entries.joinToString("  |  ") { (percent, amount) ->
                        "ƏDV ${percent}%: ${"%.2f".format(amount)} ₼"
                    }
                }
                binding.tvVat.text = vatText

                if (state.cartDiscountAmount > 0.0) {
                    binding.tvCartDiscountAmount.visibility = View.VISIBLE
                    binding.tvCartDiscountAmount.text =
                        "Ümumi endirim: –${"%.2f".format(state.cartDiscountAmount)} ₼"
                } else {
                    binding.tvCartDiscountAmount.visibility = View.GONE
                }

                val hasItems = state.items.isNotEmpty()
                binding.btnCheckout.isEnabled = hasItems
                binding.btnCheckout.alpha = if (hasItems) 1f else 0.5f
            }
        }
    }
}
