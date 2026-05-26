package com.example.myshopapp.domain.usecase

import com.example.myshopapp.domain.repository.ShiftRepository
import javax.inject.Inject

class OpenShiftUseCase @Inject constructor(
    private val repo: ShiftRepository
) {
    suspend operator fun invoke() = repo.openShift()
}