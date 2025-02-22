package com.example.koursework.ui.screens.user

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.koursework.ui.components.CarList
import com.example.koursework.ui.components.CarViewModel
import com.example.koursework.ui.theme.MyAppTheme


@Composable
fun CarScreen(viewModel: CarViewModel = CarViewModel()) {
    val cars = viewModel.cars
    Box(
        modifier = Modifier.padding(4.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        CarList(
            cars = cars,
            onDeleteCar = {
                            car -> viewModel.deleteCar(car)
                          },
            buttonText = "Удалить",
            )
    }
}

@Preview(showBackground = true, name = "Login Screen Preview")
@Composable
fun CarScreenPreview() {
    MyAppTheme {
        CarScreen()
    }
}
