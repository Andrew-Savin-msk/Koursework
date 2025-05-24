package com.example.koursework.data.remote.repository

import com.example.koursework.data.model.CountResponse
import com.example.koursework.data.remote.RetrofitInstance
import retrofit2.Response

class SavedCarRepository {

    suspend fun saveCarByEmail(email: String, carId: Long, managerId: Long): Response<Void> {
        return RetrofitInstance.api.saveCarByEmail(email, carId, managerId)
    }

    suspend fun сountByManagerId(managerId: Long): Response<CountResponse> {
        return RetrofitInstance.api.сountByManagerId(managerId)
    }
}