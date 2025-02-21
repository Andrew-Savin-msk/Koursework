package com.example.koursework.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.koursework.R
import com.example.koursework.ui.screens.auth.RegisterScreen
import com.example.koursework.ui.theme.MyAppTheme

// BOTTOM BAR COMPONENT

@Composable
fun BottomAppBarExample() {
    Scaffold(
        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(Icons.Filled.Check, contentDescription = "Localized description")
                    }
                    IconButton(onClick = { /* do something */ }) {
                        Icon(Icons.Filled.Edit, contentDescription = "Localized description")
                    }
                    IconButton(onClick = { /* do something */ }) {
                        Icon(Icons.Filled.ThumbUp, contentDescription = "Localized description")
                    }
                }
            }
        },
    ) { innerPadding ->
        Text(
            modifier = Modifier.padding(innerPadding),
            text = "Example of a scaffold with a bottom app bar."
        )
    }
}


@Preview(showBackground = true, name = "Login Screen Preview")
@Composable
fun BottomBarPreview() {
    MyAppTheme {
        NavigationBarWithItem()
    }
}

@Composable
fun NavigationBarWithItem() {
    NavigationBar {
        NavigationBarItem(
            icon = {
                // Иконка (можно заменить на любую другую, например Icons.Default.MoreHoriz)
                Icon(
                    imageVector = Icons.Default.MoreHoriz,
                    contentDescription = "More"
                )
            },
            label = { Text("Статистика") },
            selected = false,
            onClick = { /* TODO */ }
        )
        NavigationBarItem(
            icon = {
                // Иконка (можно заменить на любую другую, например Icons.Default.MoreHoriz)
                Icon(
                    imageVector = Icons.Default.MoreHoriz,
                    contentDescription = "More"
                )
            },
            label = { Text("Статистика") },
            selected = false,
            onClick = { /* TODO */ }
        )
        NavigationBarItem(
            icon = {
                // Иконка (можно заменить на любую другую, например Icons.Default.MoreHoriz)
                Icon(
                    imageVector = Icons.Default.MoreHoriz,
                    contentDescription = "More"
                )
            },
            label = { Text("Статистика") },
            selected = false,
            onClick = { /* TODO */ }
        )
    }
}