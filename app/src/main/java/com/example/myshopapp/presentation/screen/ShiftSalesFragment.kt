package com.example.myshopapp.presentation.screen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshopapp.R
import com.example.myshopapp.databinding.FragmentShiftSalesBinding
import com.example.myshopapp.presentation.adapter.ShiftSalesAdapter
import com.example.myshopapp.presentation.util.collectEvents
import com.example.myshopapp.presentation.viewmodel.ShiftSalesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShiftSalesFragment : Fragment() {

  lateinit var binding: FragmentShiftSalesBinding

    private val viewModel: ShiftSalesViewModel by viewModels()

    private lateinit var adapter: ShiftSalesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShiftSalesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setupRecycler()
        observeState()

        viewModel.loadShiftSales()
    }

    private fun setupRecycler() {

        adapter = ShiftSalesAdapter(
            onClick = { sale ->
                viewModel.reprintReceipt(sale.fullDocumentId)
            }
        )

        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext())

        binding.recyclerView.adapter = adapter
    }

    private fun observeState() {
        collectEvents(viewModel)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                adapter.submitList(state.sales)
            }
        }
    }

}