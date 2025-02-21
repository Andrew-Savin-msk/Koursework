package com.example.koursework.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.koursework.ui.theme.MyAppTheme

// USE IT FOR SCREENS

// Модель для элемента навигации
data class NavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

// Объединённый composable, принимающий готовый NavHost и список элементов для NavBar
@Composable
fun NavHostAndNavBar(
    navController: NavController,
    navHostContent: @Composable () -> Unit,
    navItems: List<NavItem>
) {
    Scaffold(
        bottomBar = {
            NavigationBar {
                // Отслеживаем текущий маршрут для выделения выбранного элемента
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                navItems.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                // Опционально: настройки для оптимизации стека навигации
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        // Отображаем NavHost с учётом отступов от Scaffold
        Box(modifier = Modifier.padding(innerPadding)) {
            navHostContent()
        }
    }
}

// Пример экранов
@Composable
fun LoginScreen(navController: NavController) {
    Text("Экран входа")
}

@Composable
fun RegisterScreen(navController: NavController) {
    Text("Экран регистрации")
}

// Пример использования объединённого composable
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    // Определяем список элементов для навигации
    val navItems = listOf(
        NavItem(route = "LoginScreen", icon = Icons.Default.Home, label = "Вход"),
        NavItem(route = "RegisterScreen", icon = Icons.Default.Person, label = "Регистрация")
    )

    // Передаём готовый NavHost как лямбду
    NavHostAndNavBar(
        navController = navController,
        navHostContent = {
            NavHost(navController = navController, startDestination = "LoginScreen") {
                composable("LoginScreen") { LoginScreen(navController) }
                composable("RegisterScreen") { RegisterScreen(navController) }
            }
        },
        navItems = navItems
    )
}

@Preview(showBackground = true, name = "Login Screen Preview")
@Composable
fun NavHostBarPreview() {
    MyAppTheme {
        NavBarScreen()
    }
}
