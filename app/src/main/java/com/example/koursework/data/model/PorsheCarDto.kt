package com.example.koursework.data.model

import java.math.BigDecimal

data class PorsheCarDto(
    val id: Long? = null,
    val name: String,
    val price: BigDecimal,
    val image: String?, // base64
    val description: String?,
    val consumption: BigDecimal,
    val seats: Int,
    val co2: BigDecimal
)
