package com.example.myshopapp.presentation.util

import android.content.Context
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class QrScannerManager(
    private val context: Context,
    private val onQrScanned: (String) -> Unit
) {

    private val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
    private var cameraProvider: ProcessCameraProvider? = null

    private var lastScannedQr: String? = null
    private var lastScanTime: Long = 0L
    private val debounceDelay = 2000L

    fun startCamera(lifecycleOwner: LifecycleOwner, previewView: PreviewView) {
        val providerFuture = ProcessCameraProvider.getInstance(context)

        providerFuture.addListener({
            try {
                val provider = providerFuture.get()
                cameraProvider = provider

                val preview = Preview.Builder().build().apply {
                    setSurfaceProvider(previewView.surfaceProvider)
                }

                val analysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()

                val scanner = BarcodeScanning.getClient()

                analysis.setAnalyzer(cameraExecutor) { imageProxy ->
                    val media = imageProxy.image ?: run {
                        imageProxy.close()
                        return@setAnalyzer
                    }

                    val input = InputImage.fromMediaImage(
                        media,
                        imageProxy.imageInfo.rotationDegrees
                    )

                    scanner.process(input)
                        .addOnSuccessListener { barcodes ->
                            barcodes.firstOrNull()?.rawValue?.let { qr ->
                                val currentTime = System.currentTimeMillis()


                                if (qr != lastScannedQr || (currentTime - lastScanTime > debounceDelay)) {
                                    Log.d("QrScannerManager", "QR kod tapıldı: $qr")
                                    lastScannedQr = qr
                                    lastScanTime = currentTime

                                    onQrScanned(qr)
                                }
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.e("QrScannerManager", "Scan xətası: ${e.message}")
                        }
                        .addOnCompleteListener {
                            imageProxy.close()
                        }
                }

                provider.unbindAll()
                provider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    analysis
                )

            } catch (e: Exception) {
                Log.e("QrScannerManager", "Kamera başlada bilmədi: ${e.message}")
            }
        }, ContextCompat.getMainExecutor(context))
    }

    fun resetScanner() {
        lastScannedQr = null
        lastScanTime = 0L
    }

    fun stopCamera() {
        cameraProvider?.unbindAll()
    }

    fun shutdown() {
        stopCamera()
        cameraExecutor.shutdown()
    }
}