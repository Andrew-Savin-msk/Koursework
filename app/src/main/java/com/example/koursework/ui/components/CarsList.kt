package com.example.koursework.ui.components

import androidx.compose.material3.Card
import com.example.koursework.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.ViewModel
import com.example.koursework.ui.theme.MyAppTheme

data class Car(
    val id: String,
    val name: String,
    val price: String,
    val fuelConsumption: String,
    val seats: Int,
    val co2: String
)

class CarViewModel : ViewModel() {
    private val _cars = mutableStateListOf<Car>()
    val cars: List<Car> = _cars

    init {
        _cars.addAll(
            listOf(
                Car("1", "Porsche Cayman", "777 777", "10 л/100км", 4, "247-201"),
                Car("2", "Toyota Camry", "2 000 000", "8 л/100км", 5, "150-180"),
                Car("3", "BMW M3", "5 000 000", "12 л/100км", 4, "230-260")
            )
        )
    }

    fun updateCars(newCars: List<Car>) {
        _cars.clear()
        _cars.addAll(newCars)
    }

    fun deleteCar(car: Car) {
        _cars.remove(car)
    }
}

@Composable
fun CarCard(
    car: Car,
    onDeleteClick: (Car) -> Unit,
    modifier: Modifier = Modifier,
    buttonText: String,
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Создаем ссылку для карточки внутри ConstraintLayout
        val (cardRef) = createRefs()

        Card(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .constrainAs(cardRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    // При необходимости можно задать и нижнюю связь
                    // bottom.linkTo(parent.bottom)
                },
            colors = CardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
                disabledContentColor = MaterialTheme.colorScheme.primary
            ),
            elevation = CardDefaults.cardElevation(),
            shape = MaterialTheme.shapes.small,
        ) {
            // Внутри карточки можно использовать ConstraintLayout или другой компоновщик (например, Column)
            ConstraintLayout(
                modifier = Modifier
                    .wrapContentHeight()
                    .wrapContentWidth()
                    .padding(8.dp)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                val (imageRef, textName, textPrice, textFuel, textSeats, textCO2, buttonRef) = createRefs()

                Image(
                    painter = painterResource(id = R.drawable.road),
                    contentDescription = "Изображение автомобиля",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .constrainAs(imageRef) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                )

                Text(
                    text = car.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.constrainAs(textName) {
                        top.linkTo(imageRef.bottom, margin = 8.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                    }
                )

                Text(
                    text = "Цена: ${car.price} рублей",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.constrainAs(textPrice) {
                        top.linkTo(textName.bottom, margin = 4.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                    }
                )

                Text(
                    text = "Расход топлива: \n${car.fuelConsumption}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.constrainAs(textFuel) {
                        top.linkTo(textPrice.bottom, margin = 20.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                    }
                )

                Text(
                    text = "Мест: ${car.seats}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.constrainAs(textSeats) {
                        top.linkTo(textFuel.bottom, margin = 4.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                    }
                )

                Text(
                    text = "Выбросы CO2: ${car.co2}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.constrainAs(textCO2) {
                        top.linkTo(textSeats.bottom, margin = 4.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                    }
                )

                Button(
                    onClick = { onDeleteClick(car) },
                    modifier = Modifier.constrainAs(buttonRef) {
                        top.linkTo(textCO2.bottom, margin = 16.dp)
                        bottom.linkTo(parent.bottom, margin = 18.dp)
                        end.linkTo(parent.end, margin = 16.dp)
                    },
                    shape = MaterialTheme.shapes.small,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(buttonText)
                }
            }
        }
    }
}


@Composable
fun CarList(
    cars: List<Car>,
    onDeleteCar: (Car) -> Unit,
    buttonText: String,
) {
    LazyColumn {
        items(cars) { car ->
            CarCard(
                car = car,
                onDeleteClick = onDeleteCar,
                buttonText = buttonText,
            )
        }
    }
}