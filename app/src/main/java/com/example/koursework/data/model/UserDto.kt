package com.example.koursework.data.model

data class UserDto(
    val id: Long? = null,
    val email: String,
    val password: String,
    val role: String
)