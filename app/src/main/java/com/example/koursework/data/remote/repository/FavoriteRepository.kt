package com.example.koursework.data.remote.repository

import com.example.koursework.data.model.PorsheFavoriteDto
import com.example.koursework.data.remote.RetrofitInstance
import retrofit2.Response

class FavoriteRepository {

    suspend fun getAllFavorites(): List<PorsheFavoriteDto> {
        return RetrofitInstance.api.getAllFavorites()
    }

    suspend fun getFavoriteById(id: Long): PorsheFavoriteDto {
        return RetrofitInstance.api.getFavoriteById(id)
    }

    suspend fun createFavorite(userId: Long, carId: Long): Response<PorsheFavoriteDto> {
        return RetrofitInstance.api.createFavorite(userId, carId)
    }

    suspend fun deleteFavorite(id: Long): Response<Void> {
        return RetrofitInstance.api.deleteFavorite(id)
    }
}
