package com.example.koursework.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Stars
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
import com.example.koursework.ui.screens.user.AdditionalScreen
import com.example.koursework.ui.screens.user.CarScreen
import com.example.koursework.ui.screens.user.HomeScreen
import com.example.koursework.ui.theme.MyAppTheme

// USE IT FOR SCREENS

data class NavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

@Composable
fun NavHostAndNavBar(
    navController: NavController,
    navHostContent: @Composable () -> Unit,
    navItems: List<NavItem>
) {
    Scaffold(
        bottomBar = {
            NavigationBar (
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                navItems.forEach { item ->
                    NavigationBarItem(
                        colors = NavigationBarItemColors(
                            selectedIconColor = MaterialTheme.colorScheme.inverseSurface,
                            selectedTextColor = MaterialTheme.colorScheme.inverseSurface,
                            selectedIndicatorColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                            unselectedIconColor = MaterialTheme.colorScheme.inverseSurface,
                            unselectedTextColor = MaterialTheme.colorScheme.inverseSurface,
                            disabledIconColor = MaterialTheme.colorScheme.inverseSurface,
                            disabledTextColor = MaterialTheme.colorScheme.inverseSurface
                        ),
                        icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        },
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            navHostContent()
        }
    }
}

@Preview(showBackground = true, name = "Home Screen Preview")
@Composable
fun HomeScreenPreview() {
    MyAppTheme {
        val navController = rememberNavController()

        val navItems = listOf(
            NavItem(route = "List", icon = Icons.Default.DirectionsCar, label = "Каталог"),
            NavItem(route = "Favorite", icon = Icons.Default.FavoriteBorder, label = "Отмеченное"),
            NavItem(route = "Additional", icon = Icons.Default.Stars, label = "Дополнительно")
        )

        NavHostAndNavBar(
            navController = navController,
            navHostContent = {
                NavHost(navController = navController, startDestination = "Additional", modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
                    composable("List") { HomeScreen() }
                    composable("Favorite") { CarScreen() }
                    composable("Additional") { AdditionalScreen() }
                }
            },
            navItems = navItems
        )
    }
}
