package com.example.koursework.ui.outbox

data class User(
    val email: String = "",
    val password: String = "",
    val isAdmin: Boolean = false,
    val isLoggedIn: Boolean = false
)

object AppState {
    var isDarkTheme: Boolean = false
    private var user: User? = null

    fun switchTheme() {
        isDarkTheme = !isDarkTheme
    }

    fun logInUser(email: String, password: String, isAdmin: Boolean) {
        user = User(
            email = email,
            password = password,
            isAdmin = isAdmin,
            isLoggedIn = true
        )
    }

    fun logOut() {
        user = null
    }
}

