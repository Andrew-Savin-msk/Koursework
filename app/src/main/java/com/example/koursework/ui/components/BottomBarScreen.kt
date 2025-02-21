package com.example.koursework.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.koursework.ui.screens.auth.LoginScreen
import com.example.koursework.ui.screens.auth.RegisterScreen
import com.example.koursework.ui.theme.MyAppTheme

// NOT TO USE

@Composable
fun NavBarScreen() {
    val navController = rememberNavController()

    val navItems = listOf("LoginScreen", "RegisterScreen")

    Scaffold(
        bottomBar = {
            NavigationBar {
                navItems.forEach { route ->
                    NavigationBarItem(
                        icon = {
                            Icon(imageVector = Icons.Default.Home, contentDescription = route)
                        },
                        label = { Text(route) },
                        selected = false,
                        onClick = { navController.navigate(route) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "LoginScreen",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("LoginScreen") { LoginScreen(navController) }
            composable("RegisterScreen") { RegisterScreen(navController) }
        }
    }
}

@Preview(showBackground = true, name = "Login Screen Preview")
@Composable
fun NavBarPreview() {
    MyAppTheme {
        NavBarScreen()
    }
}