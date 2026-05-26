package com.example.myshopapp.data.repository

import com.example.myshopapp.data.remote.model.repsonse.shift.close.ShiftClose
import com.example.myshopapp.data.remote.model.repsonse.shift.open.ShiftOpen
import com.example.myshopapp.data.remote.model.repsonse.shift.status.ShiftStatus
import com.example.myshopapp.data.remote.service.ApiService
import com.example.myshopapp.domain.repository.ShiftRepository
import com.example.myshopapp.data.util.safeApiCall
import javax.inject.Inject

class ShiftRepositoryImpl @Inject constructor(
    private val api: ApiService
) : ShiftRepository {

    override suspend fun getShift(): Result<ShiftStatus> = safeApiCall {

        api.getShift()
    }

    override suspend fun openShift(): Result<ShiftOpen> = safeApiCall {


        api.openShift()
    }

    override suspend fun closeShift(): Result<ShiftClose> = safeApiCall {

        api.closeShift()
    }
}