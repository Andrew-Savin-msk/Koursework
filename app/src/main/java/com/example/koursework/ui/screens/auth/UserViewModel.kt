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

class UserViewModel : ViewModel() {
    private val repository = UserRepository()

    private val _loginResult = MutableStateFlow<LoginResult?>(null)
    val loginResult: StateFlow<LoginResult?> = _loginResult

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
                _loginResult.value = LoginResult(false, "Ошибка на стороне сервера")
            } finally {
                _isLoading.value = false
            }
        }
    }
}
