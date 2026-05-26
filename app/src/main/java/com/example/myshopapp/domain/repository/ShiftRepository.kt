package com.example.myshopapp.domain.repository


import com.example.myshopapp.data.remote.model.repsonse.shift.close.ShiftClose
import com.example.myshopapp.data.remote.model.repsonse.shift.open.ShiftOpen
import com.example.myshopapp.data.remote.model.repsonse.shift.status.ShiftStatus

interface ShiftRepository {
    suspend fun getShift(): Result<ShiftStatus>
    suspend fun openShift(): Result<ShiftOpen>
    suspend fun closeShift(): Result<ShiftClose>
}