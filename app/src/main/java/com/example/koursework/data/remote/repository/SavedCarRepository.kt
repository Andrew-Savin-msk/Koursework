package com.example.koursework.data.remote.repository

import com.example.koursework.data.remote.RetrofitInstance
import retrofit2.Response

class SavedCarRepository {

    suspend fun saveCarByEmail(email: String, carId: Long): Response<Void> {
        return RetrofitInstance.api.saveCarByEmail(email, carId)
    }
}