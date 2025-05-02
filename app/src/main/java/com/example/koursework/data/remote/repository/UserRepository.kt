package com.example.koursework.data.remote.repository

import com.example.koursework.data.model.UserDto
import com.example.koursework.data.remote.RetrofitInstance
import retrofit2.Response

class UserRepository {

    suspend fun getAllUsers(): List<UserDto> {
        return RetrofitInstance.api.getAllUsers()
    }

    suspend fun register(user: UserDto): Response<UserDto> {
        return RetrofitInstance.api.register(user)
    }

    suspend fun login(user: UserDto): Response<UserDto> {
        return RetrofitInstance.api.login(user)
    }
}
