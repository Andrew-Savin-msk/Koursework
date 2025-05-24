package com.example.koursework.data.remote

import com.example.koursework.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // --- USERS ---
    @GET("/users")
    suspend fun getAllUsers(): List<UserDto>

    @GET("/users/{id}")
    suspend fun getUserById(@Path("id") id: Long): UserDto

    @POST("/users")
    suspend fun createUser(@Body user: UserDto): Response<UserDto>

    @PUT("/users/{id}")
    suspend fun updateUser(@Path("id") id: Long, @Body user: UserDto): Response<UserDto>

    @DELETE("/users/{id}")
    suspend fun deleteUser(@Path("id") id: Long): Response<Void>

    @GET("/porshe/cars")
    suspend fun getAllCars(): List<PorsheCarDto>

    @GET("/porshe/cars/{id}")
    suspend fun getCarById(@Path("id") id: Long): PorsheCarDto

    @POST("/porshe/cars")
    suspend fun createCar(@Body car: PorsheCarDto): Response<PorsheCarDto>

    @PUT("/porshe/cars/{id}")
    suspend fun updateCar(@Path("id") id: Long, @Body car: PorsheCarDto): Response<PorsheCarDto>

    @DELETE("/porshe/cars/{id}")
    suspend fun deleteCar(@Path("id") id: Long): Response<Void>

    @GET("/porshe/favorites")
    suspend fun getAllFavorites(): List<PorsheFavoriteDto>

    @GET("/porshe/favorites/{id}")
    suspend fun getFavoriteById(@Path("id") id: Long): PorsheFavoriteDto

    @POST("/porshe/favorites")
    suspend fun createFavorite(
        @Query("userId") userId: Long,
        @Query("carId") carId: Long
    ): Response<PorsheFavoriteDto>

    @DELETE("/porshe/favorites/{id}")
    suspend fun deleteFavorite(@Path("id") id: Long): Response<Void>

    @POST("/porshe/saved/by-email")
    suspend fun saveCarByEmail(@Query("email") email: String, @Query("carId") carId: Long, @Query("managerId") managerId: Long): Response<Void>

    @GET("/porshe/saved/count-by-user")
    suspend fun сountByManagerId(@Query("managerId") managerId: Long): Response<CountResponse>
}
