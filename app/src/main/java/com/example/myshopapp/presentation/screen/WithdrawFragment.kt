package com.example.myshopapp.presentation.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.myshopapp.databinding.FragmentWithDrawBinding
import com.example.myshopapp.presentation.util.collectEvents
import com.example.myshopapp.presentation.viewmodel.WithdrawViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WithdrawFragment : Fragment() {

    private lateinit var binding: FragmentWithDrawBinding
    private val viewModel: WithdrawViewModel by viewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentWithDrawBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etAmount.addTextChangedListener {
            viewModel.setAmount(it.toString())
        }

        binding.btnWithdraw.setOnClickListener {
            viewModel.withdraw()
        }

        observeState()
    }

    private fun observeState() {
        collectEvents(
            viewModel = viewModel,
            onSuccess = { requireActivity().onBackPressedDispatcher.onBackPressed() }
        )

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { s ->
                binding.progressBar.visibility = if (s.isLoading) View.VISIBLE else View.GONE
                binding.btnWithdraw.isEnabled  = !s.isLoading
            }
        }
    }
}