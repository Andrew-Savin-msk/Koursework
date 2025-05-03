package com.example.koursework.ui.screens.user

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.koursework.ui.components.Car
import com.example.koursework.ui.components.CarList
import com.example.koursework.ui.components.FavoritesViewModel
import com.example.koursework.ui.outbox.AppState
import com.example.koursework.ui.theme.MyAppTheme
import kotlinx.coroutines.launch

@Composable
fun FavoritesScreen(viewModel: FavoritesViewModel = viewModel()) {
    val context = LocalContext.current
    val favorites by viewModel.favorites.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    // Подгрузка данных при первом запуске
    LaunchedEffect(Unit) {
        viewModel.loadFavorites()
    }

    val favoriteCars = remember(favorites) {
        favorites.map {
            Car(
                id = it.car.id.toString(),
                name = it.car.name,
                price = "${it.car.price}₽",
                fuelConsumption = "${it.car.consumption} л/100км",
                seats = it.car.seats,
                co2 = "${it.car.co2}",
                imageBase64 = it.car.image // здесь уже base64 строка
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(8.dp)
    ) {
        if (favoriteCars.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Избранных автомобилей нет", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            CarList(
                cars = favoriteCars,
                buttonText = "Удалить",
                onDeleteCar = { car ->
                    val favoriteToDelete = favorites.find { it.car.id.toString() == car.id }
                    if (favoriteToDelete != null && favoriteToDelete.id != null) {
                        coroutineScope.launch {
                            viewModel.deleteFavorite(favoriteToDelete.id)
                            Toast
                                .makeText(context, "Удалено из избранного", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FavoritesScreenPreview() {
    MyAppTheme {
        FavoritesScreen()
    }
}
