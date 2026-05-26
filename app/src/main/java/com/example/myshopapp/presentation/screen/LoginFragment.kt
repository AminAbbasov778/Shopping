package com.example.myshopapp.presentation.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.myshopapp.R
import com.example.myshopapp.databinding.FragmentLoginBinding
import com.example.myshopapp.presentation.util.collectEvents
import com.example.myshopapp.presentation.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class LoginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.btnLogin.setOnClickListener {

            val name = binding.etCashierName.text.toString()
            val pass = binding.etPassword.text.toString()

            viewModel.login(name)
        }

        observe()
    }

    private fun observe() {
        collectEvents(viewModel)

        viewLifecycleOwner.lifecycleScope.launch {

            repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.navigate.collect {
                        findNavController().navigate(
                            R.id.action_loginFragment_to_shiftFragment
                        )
                    }
                }

                launch {
                    viewModel.loading.collect { loading ->
                        binding.progressBar.visibility =
                            if (loading) View.VISIBLE else View.GONE
                        binding.btnLogin.isEnabled = !loading
                    }
                }
            }
        }
    }


}