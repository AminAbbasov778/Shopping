package com.example.myshopapp.presentation.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.myshopapp.R
import com.example.myshopapp.databinding.FragmentShiftBinding
import com.example.myshopapp.presentation.viewmodel.ShiftViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShiftFragment : Fragment(R.layout.fragment_shift) {

    lateinit var binding: FragmentShiftBinding

    private val viewModel: ShiftViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShiftBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupObservers()
        setupClicks()

        viewModel.loadShift()
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->

                val isLoading = state.isLoading
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE

                state.shift?.let { shiftStatus ->


                    val isOpen = shiftStatus.data.shiftOpen

                    binding.tvStatus.text = if (isOpen) "SHIFT AÇIQDIR" else "SHIFT BAĞLIDIR"
                    binding.tvTime.text = shiftStatus.data.shiftOpenTime ?: "-"


                    binding.btnOpenShift.isEnabled = !isOpen && !isLoading
                    binding.btnCloseShift.isEnabled = isOpen && !isLoading
                }

                state.error?.let {
                    binding.tvStatus.text = it

                    binding.btnOpenShift.isEnabled = !isLoading
                    binding.btnCloseShift.isEnabled = !isLoading
                }
            }
        }
    }

    private fun setupClicks() {
        binding.btnOpenShift.setOnClickListener {
            viewModel.openShift()
        }

        binding.btnCloseShift.setOnClickListener {
            viewModel.closeShift()
        }

        binding.btnRefresh.setOnClickListener {
            viewModel.loadShift()
        }
    }


}