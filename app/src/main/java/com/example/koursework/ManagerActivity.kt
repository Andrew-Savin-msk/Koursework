package com.example.koursework

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Stars
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.koursework.ui.components.NavHostAndNavBar
import com.example.koursework.ui.components.NavItem
import com.example.koursework.ui.screens.manager.EditScreen
import com.example.koursework.ui.screens.manager.ListScreen
import com.example.koursework.ui.screens.manager.StatisticsScreen
import com.example.koursework.ui.screens.user.AdditionalScreen
import com.example.koursework.ui.screens.user.CarScreen
import com.example.koursework.ui.screens.user.HomeScreen
import com.example.koursework.ui.theme.MyAppTheme

class ManagerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyAppTheme {
                val navController = rememberNavController()

                val navItems = listOf(
                    NavItem(route = "List", icon = Icons.Default.DirectionsCar, label = "Каталог"),
                    NavItem(route = "Edit", icon = Icons.Default.Edit, label = "Редактирование"),
                    NavItem(route = "Statistics", icon = Icons.Default.MoreHoriz, label = "Статистика")
                )

                NavHostAndNavBar(
                    navController = navController,
                    navHostContent = {
                        NavHost(navController = navController, startDestination = "Statistics") {
                            composable("List") { ListScreen() }
                            composable("Edit") { EditScreen() }
                            composable("Statistics") { StatisticsScreen() }
                        }
                    },
                    navItems = navItems
                )
            }
        }
    }
}
