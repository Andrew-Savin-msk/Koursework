package com.example.koursework.ui.screens.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.koursework.data.model.UserDto
import com.example.koursework.data.remote.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException

data class LoginResult(val success: Boolean, val message: String)

data class RegisterResult(val success: Boolean, val message: String)

class UserViewModel : ViewModel() {
    private val repository = UserRepository()

    private val _loginResult = MutableStateFlow<LoginResult?>(null)
    val loginResult: StateFlow<LoginResult?> = _loginResult

    private val _registerResult = MutableStateFlow<RegisterResult?>(null)
    val registerResult: StateFlow<RegisterResult?> = _registerResult

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val users = repository.getAllUsers()
                val match = users.find { it.email == email && it.password == password }
                _loginResult.value = if (match != null) {
                    LoginResult(true, "")
                } else {
                    LoginResult(false, "Неправильная почта или пароль")
                }
            } catch (e: IOException) {
                Log.e("Login", "Ошибка сети", e)
                _loginResult.value = LoginResult(false, "Проверьте подключение к интернету")
            } catch (e: Exception) {
                Log.e("Login", "Неизвестная ошибка", e)
                _loginResult.value = LoginResult(false, "Ошибка на сервере")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val newUser = UserDto(email = email, password = password, role = "user")
                val response = repository.createUser(newUser)
                if (response.isSuccessful) {
                    _registerResult.value = RegisterResult(true, "")
                } else {
                    _registerResult.value = RegisterResult(false, "Ошибка при регистрации")
                    Log.e("Register", "Ошибка: ${response.errorBody()?.string()}")
                }
            } catch (e: IOException) {
                Log.e("Register", "Ошибка сети", e)
                _registerResult.value = RegisterResult(false, "Нет подключения к интернету")
            } catch (e: Exception) {
                Log.e("Register", "Иная ошибка", e)
                _registerResult.value = RegisterResult(false, "Что-то пошло не так")
            } finally {
                _isLoading.value = false
            }
        }
    }
}
