package com.example.koursework.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

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
            NavigationBar {
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
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            navHostContent()
        }
    }
}