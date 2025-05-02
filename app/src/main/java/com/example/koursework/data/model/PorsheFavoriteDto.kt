package com.example.koursework.data.model

data class PorsheFavoriteDto(
    val id: Long? = null,
    val user: UserDto,
    val car: PorsheCarDto
)