package com.example.koursework.data.remote.repository

import com.example.koursework.data.model.UserDto
import com.example.koursework.data.remote.RetrofitInstance
import retrofit2.Response

class UserRepository {

    suspend fun getAllUsers(): List<UserDto> {
        return RetrofitInstance.api.getAllUsers()
    }

    suspend fun getUserById(id: Long): UserDto {
        return RetrofitInstance.api.getUserById(id)
    }

    suspend fun createUser(user: UserDto): Response<UserDto> {
        return RetrofitInstance.api.createUser(user)
    }

    suspend fun updateUser(id: Long, user: UserDto): Response<UserDto> {
        return RetrofitInstance.api.updateUser(id, user)
    }

    suspend fun deleteUser(id: Long): Response<Void> {
        return RetrofitInstance.api.deleteUser(id)
    }
}
