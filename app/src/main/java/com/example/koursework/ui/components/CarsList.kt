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
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.ViewModel
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.koursework.data.model.PorsheCarDto
import com.example.koursework.data.remote.repository.CarRepository
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import android.widget.Toast
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.koursework.data.model.PorsheFavoriteDto
import com.example.koursework.data.remote.repository.FavoriteRepository
import com.example.koursework.data.remote.repository.SavedCarRepository
import com.example.koursework.ui.outbox.AppState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.IOException

data class Car(
    val id: String,
    val name: String,
    val price: String,
    val fuelConsumption: String,
    val seats: Int,
    val co2: String,
    val imageBase64: String? = null
)

class CarViewModel : ViewModel() {
    private val favoriteRepository = FavoriteRepository()
    private val repository = CarRepository()

    private val _cars = mutableStateListOf<Car>()
    val cars: List<Car> = _cars

    init {
        loadCarsFromApi()
    }

    fun addToFavorites(car: Car, onResult: (Boolean) -> Unit = {}) {
        val userId = AppState.getUser()?.id ?: return onResult(false)
        viewModelScope.launch {
            try {
                val response = favoriteRepository.createFavorite(userId, car.id.toLong())
                if (response.isSuccessful) {
                    onResult(true)
                } else {
                    Log.e("AddFavorite", "Ошибка: ${response.errorBody()?.string()}")
                    onResult(false)
                }
            } catch (e: Exception) {
                Log.e("AddFavorite", "Ошибка запроса", e)
                onResult(false)
            }
        }
    }

    fun loadCarsFromApi() {
        viewModelScope.launch {
            try {
                val dtoList = repository.getAllCars()
                val formatted = dtoList.map { dto -> dto.toCar() }
                _cars.clear()
                _cars.addAll(formatted)
            } catch (e: Exception) {
                Log.e("CarViewModel", "Ошибка загрузки машин: ${e.message}", e)
            }
        }
    }

    fun deleteCar(car: Car) {
        viewModelScope.launch {
            try {
                repository.deleteCar(car.id.toLong())
                _cars.remove(car)
            } catch (e: Exception) {
                Log.e("CarViewModel", "Ошибка удаления: ${e.message}", e)
            }
        }
    }

    fun updateCars(newCars: List<Car>) {
        _cars.clear()
        _cars.addAll(newCars)
    }

    private fun PorsheCarDto.toCar(): Car {
        val priceFormatter = DecimalFormat("#,###.##")
        return Car(
            id = this.id?.toString() ?: "",
            name = this.name,
            price = priceFormatter.format(this.price),
            fuelConsumption = "${this.consumption} л/100км",
            seats = this.seats,
            co2 = "${this.co2} г/км",
            imageBase64 = this.image// если image в DTO — ByteArray
        )
    }

    fun createCar(
        name: String,
        price: String,
        description: String?,
        imageBase64: String?,
        consumption: String,
        seats: String,
        co2: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val dto = PorsheCarDto(
                    name = name,
                    price = price.toBigDecimal(),
                    image = imageBase64,
                    description = description,
                    consumption = consumption.toBigDecimal(),
                    seats = seats.toInt(),
                    co2 = co2.toBigDecimal()
                )
                val response = repository.createCar(dto)
                onResult(response.isSuccessful)
                if (response.isSuccessful) loadCarsFromApi()
            } catch (e: Exception) {
                Log.e("CarViewModel", "Ошибка при создании автомобиля", e)
                onResult(false)
            }
        }
    }

    fun updateCar(
        id: Long,
        name: String,
        price: String,
        description: String?,
        imageBase64: String?,
        consumption: String,
        seats: String,
        co2: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val dto = PorsheCarDto(
                    id = id,
                    name = name,
                    price = price.toBigDecimal(),
                    image = imageBase64,
                    description = description,
                    consumption = consumption.toBigDecimal(),
                    seats = seats.toInt(),
                    co2 = co2.toBigDecimal()
                )
                val response = repository.updateCar(id, dto)
                onResult(response.isSuccessful)
                if (response.isSuccessful) loadCarsFromApi()
            } catch (e: Exception) {
                Log.e("CarViewModel", "Ошибка при обновлении автомобиля", e)
                onResult(false)
            }
        }
    }

}


class FavoritesViewModel : ViewModel() {
    private val repository = FavoriteRepository()
    private val _favorites = MutableStateFlow<List<PorsheFavoriteDto>>(emptyList())
    val favorites = _favorites.asStateFlow()

    init {
        loadFavorites()
    }

    fun loadFavorites() {
        viewModelScope.launch {
            try {
                val all = repository.getAllFavorites()
                val userId = AppState.getUser()?.id
                _favorites.value = all.filter { it.user.id == userId }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteFavorite(favoriteId: Long) {
        viewModelScope.launch {
            try {
                val response = repository.deleteFavorite(favoriteId)
                if (response.isSuccessful) {
                    _favorites.value = _favorites.value.filterNot { it.id == favoriteId }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

class SavedCarViewModel : ViewModel() {
    private val repository = SavedCarRepository()

    private val _isSaved = MutableStateFlow<Boolean?>(null)
    val isSaved: StateFlow<Boolean?> = _isSaved

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun saveCar(email: String, carId: Long) {
        viewModelScope.launch {
            try {
                val response = repository.saveCarByEmail(email, carId)
                if (response.isSuccessful) {
                    _isSaved.value = true
                } else {
                    Log.e("SavedCar", "Ошибка ${response.code()}")
                    _errorMessage.value = "Не удалось сохранить авто"
                    _isSaved.value = false
                }
            } catch (e: IOException) {
                Log.e("SavedCar", "Ошибка сети", e)
                _errorMessage.value = "Проблема с интернетом"
                _isSaved.value = false
            } catch (e: Exception) {
                Log.e("SavedCar", "Ошибка запроса", e)
                _errorMessage.value = "Произошла ошибка"
                _isSaved.value = false
            }
        }
    }
}

@Composable
fun CarCard(
    car: Car,
    onDeleteClick: (Car) -> Unit,
    modifier: Modifier = Modifier,
    buttonText: String,
) {
    val bitmap = remember(car.imageBase64) {
        car.imageBase64?.let {
            try {
                val decodedBytes = Base64.decode(it, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            } catch (e: Exception) {
                null
            }
        }
    }
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        val (cardRef) = createRefs()

        Card(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .constrainAs(cardRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
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
            ConstraintLayout(
                modifier = Modifier
                    .wrapContentHeight()
                    .wrapContentWidth()
                    .padding(8.dp)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                val (imageRef, textName, textPrice, textFuel, textSeats, textCO2, buttonRef) = createRefs()

                if (bitmap != null) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
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
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.porshe_911),
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
                }


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