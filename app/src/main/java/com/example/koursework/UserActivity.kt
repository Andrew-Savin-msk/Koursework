package com.example.koursework

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Stars
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.koursework.ui.components.CarViewModel
import com.example.koursework.ui.components.NavHostAndNavBar
import com.example.koursework.ui.components.NavItem
import com.example.koursework.ui.screens.user.AdditionalScreen
import com.example.koursework.ui.screens.user.CarScreen
import com.example.koursework.ui.screens.user.HomeScreen
import com.example.koursework.ui.theme.MyAppTheme

class UserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyAppTheme {
                val navController = rememberNavController()
                val carViewModel: CarViewModel = viewModel()

                val navItems = listOf(
                    NavItem(route = "List", icon = Icons.Default.DirectionsCar, label = "Каталог"),
                    NavItem(route = "Favorite", icon = Icons.Default.FavoriteBorder, label = "Отмеченное"),
                    NavItem(route = "Additional", icon = Icons.Default.Stars, label = "Дополнительно")
                )

                NavHostAndNavBar(
                    navController = navController,
                    navHostContent = {
                        NavHost(navController = navController, startDestination = "List") {
                            composable("List") { HomeScreen(viewModel = carViewModel) }
                            composable("Favorite") { CarScreen(viewModel = carViewModel) }
                            composable("Additional") { AdditionalScreen() }
                        }
                    },
                    navItems = navItems
                )
            }
        }
    }
}


//@Preview(showBackground = true, name = "Home Screen Preview")
//@Composable
//fun HomeScreenPreview() {
//    MyAppTheme {
//        val navController = rememberNavController()
//
//        val navItems = listOf(
//            NavItem(route = "List", icon = Icons.Default.DirectionsCar, label = "Каталог"),
//            NavItem(route = "Favorite", icon = Icons.Default.FavoriteBorder, label = "Отмеченное"),
//            NavItem(route = "Additional", icon = Icons.Default.Stars, label = "Дополнительно")
//        )
//
//        NavHostAndNavBar(
//            navController = navController,
//            navHostContent = {
//                NavHost(navController = navController, startDestination = "Additional", modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
//                    composable("List") { HomeScreen() }
//                    composable("Favorite") { CarScreen() }
//                    composable("Additional") { AdditionalScreen() }
//                }
//            },
//            navItems = navItems
//        )
//    }
//}
