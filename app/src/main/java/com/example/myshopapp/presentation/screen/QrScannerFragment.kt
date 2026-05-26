package com.example.myshopapp.presentation.screen

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.myshopapp.R
import com.example.myshopapp.databinding.FragmentQrScannerBinding
import com.example.myshopapp.presentation.util.QrScannerManager
import com.example.myshopapp.presentation.util.collectEvents
import com.example.myshopapp.presentation.viewmodel.QrLookupViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QrScannerFragment : Fragment() {

    lateinit var binding: FragmentQrScannerBinding

    private val viewModel: QrLookupViewModel by viewModels()
    private var qrScannerManager: QrScannerManager? = null

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startScanning()
            } else {
                Toast.makeText(requireContext(), "Kamera icazəsi verilmədi", Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentQrScannerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        qrScannerManager = QrScannerManager(requireContext()) { qrCode ->
            viewModel.scan(qrCode)
        }

        checkCameraPermissionAndStart()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.clear()
        qrScannerManager?.resetScanner()
        if (hasCameraPermission()) {
            startScanning()
        }
    }

    override fun onPause() {
        super.onPause()
        qrScannerManager?.stopCamera()
    }

    private fun checkCameraPermissionAndStart() {
        if (hasCameraPermission()) {
            startScanning()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun hasCameraPermission() = ContextCompat.checkSelfPermission(
        requireContext(), Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    private fun startScanning() {
        qrScannerManager?.startCamera(viewLifecycleOwner, binding.previewView)
    }

    private fun observeViewModel() {
        collectEvents(viewModel)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                state.sale?.let {
                    viewModel.clear()
                    findNavController().navigate(
                        R.id.action_qrScannerFragment_to_saleDetailFragment,
                        bundleOf("documentId" to it.documentId)
                    )
                }
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        qrScannerManager?.shutdown()
        qrScannerManager = null
    }
}