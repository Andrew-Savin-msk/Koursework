package com.example.koursework.ui.outbox

data class User(
    val id: Long = -1,
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

    fun logInUser(id: Long, email: String, password: String, isAdmin: Boolean) {
        user = User(
            id = id,
            email = email,
            password = password,
            isAdmin = isAdmin,
            isLoggedIn = true
        )
    }

    fun getUser(): User? = user

    fun logOut() {
        user = null
    }
}