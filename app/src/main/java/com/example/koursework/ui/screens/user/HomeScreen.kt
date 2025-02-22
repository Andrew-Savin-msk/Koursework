package com.example.koursework.ui.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.koursework.ui.components.CarList
import com.example.koursework.ui.components.CarViewModel
import com.example.koursework.ui.theme.MyAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: CarViewModel = CarViewModel()) {
    val cars = viewModel.cars

    // Стейт для поискового запроса
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
//        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { newQuery ->
                                searchQuery = newQuery
                            },
                            placeholder = {
                                Text(text = "Поиск по названию авто", color = MaterialTheme.colorScheme.inverseSurface)
                            },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.surface,
                                unfocusedBorderColor = MaterialTheme.colorScheme.surface,
                                cursorColor = MaterialTheme.colorScheme.inverseSurface,
                                focusedTextColor = MaterialTheme.colorScheme.inverseSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.inverseSurface,
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                        },
                        colors = IconButtonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.inverseSurface,
                            disabledContainerColor = MaterialTheme.colorScheme.surface,
                            disabledContentColor = MaterialTheme.colorScheme.inverseSurface
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Иконка поиска"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors()
            )
        },
        modifier = Modifier.fillMaxWidth()
    ) { paddingValues ->
        // Пример простой фильтрации на лету
        val filteredCars = cars.filter { car ->
            car.name.contains(searchQuery, ignoreCase = true)
        }

        Box(
            modifier = Modifier
                .padding(paddingValues)
                .padding(4.dp)
        ) {
            CarList(
                cars = filteredCars,
                onDeleteCar = { car -> viewModel.deleteCar(car) },
                buttonText = "Сохранить"
            )
        }
    }
}


@Preview(showBackground = true, name = "Home Screen Preview")
@Composable
fun HomeScreenPreview() {
    MyAppTheme {
        HomeScreen()
    }
}