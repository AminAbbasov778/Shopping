package com.example.myshopapp.presentation.util

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.myshopapp.presentation.base.BaseViewModel
import kotlinx.coroutines.launch

fun Fragment.collectEvents(
    viewModel: BaseViewModel,
    onSuccess: ((String) -> Unit)? = null,
    onNavigateBack: (() -> Unit)? = null,
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewModel.events.collect { event ->
            when (event) {
                is UiEvent.ShowError -> {
                    Toast.makeText(requireContext(), event.message, Toast.LENGTH_LONG).show()
                }
                is UiEvent.ShowSuccess -> {
                    if (event.message.isNotBlank()) {
                        Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
                    }
                    onSuccess?.invoke(event.message)
                }
                is UiEvent.NavigateBack -> {
                    onNavigateBack?.invoke() ?: findNavController().popBackStack()
                }
            }
        }
    }
}
