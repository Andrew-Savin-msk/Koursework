package com.example.koursework.data.remote.repository

import com.example.koursework.data.model.PorsheCarDto
import com.example.koursework.data.remote.RetrofitInstance
import retrofit2.Response

class CarRepository {

    suspend fun getAllCars(): List<PorsheCarDto> {
        return RetrofitInstance.api.getAllCars()
    }

    suspend fun getCarById(id: Long): PorsheCarDto {
        return RetrofitInstance.api.getCarById(id)
    }

    suspend fun createCar(car: PorsheCarDto): Response<PorsheCarDto> {
        return RetrofitInstance.api.createCar(car)
    }

    suspend fun updateCar(id: Long, updatedCar: PorsheCarDto): Response<PorsheCarDto> {
        return RetrofitInstance.api.updateCar(id, updatedCar)
    }

    suspend fun deleteCar(id: Long): Response<Void> {
        return RetrofitInstance.api.deleteCar(id)
    }
}
