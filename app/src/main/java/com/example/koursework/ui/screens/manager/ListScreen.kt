package com.example.koursework.ui.screens.manager

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.koursework.ui.components.AssignBuyerBottomSheet
import com.example.koursework.ui.components.Car
import com.example.koursework.ui.components.CarList
import com.example.koursework.ui.components.CarViewModel
import com.example.koursework.ui.components.SavedCarViewModel
import com.example.koursework.ui.outbox.AppState
import com.example.koursework.ui.outbox.SearchHistoryManager
import com.example.koursework.ui.theme.MyAppTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(viewModel: CarViewModel = CarViewModel(), savedCarViewModel: SavedCarViewModel) {
    // Управление открытием/закрытием BottomSheet
    var isSheetOpen by remember { mutableStateOf(false) }

    // Поле для ввода email
    var email by remember { mutableStateOf("") }

    val context = LocalContext.current
    val isSaved by savedCarViewModel.isSaved.collectAsState()
    val saveError by savedCarViewModel.errorMessage.collectAsState()
    var selectedCar by remember { mutableStateOf<Car?>(null) }


    LaunchedEffect(isSaved, saveError) {
        when (isSaved) {
            true -> Toast.makeText(context, "Авто назначено!", Toast.LENGTH_SHORT).show()
            false -> Toast.makeText(context, saveError ?: "Ошибка назначения", Toast.LENGTH_LONG).show()
            else -> {} // null — ничего не делаем
        }
    }

    val coroutineScope = rememberCoroutineScope()
    var searchQuery by rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(
        TextFieldValue("")
    ) }
    var isLoading by remember { mutableStateOf(false) }
    var errorOccurred by remember { mutableStateOf(false) }
    var history by remember { mutableStateOf(SearchHistoryManager.getHistory(context)) }
    var showHistory by remember { mutableStateOf(false) }

    val cars = viewModel.cars
    val filteredCars = if (searchQuery.text.isEmpty()) cars else cars.filter {
        it.name.contains(searchQuery.text, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { newQuery ->
                                searchQuery = newQuery
                            },
                            placeholder = {
                                Text(
                                    text = "Поиск по названию авто",
                                    color = MaterialTheme.colorScheme.inverseSurface
                                )
                            },
                            singleLine = true,
                            trailingIcon = {
                                if (searchQuery.text.isNotEmpty()) {
                                    IconButton(
                                        onClick = {
                                            searchQuery = TextFieldValue("")
                                            showHistory = true
                                        },
                                        colors = IconButtonColors(
                                            containerColor = MaterialTheme.colorScheme.surface,
                                            contentColor = MaterialTheme.colorScheme.inverseSurface,
                                            disabledContainerColor = MaterialTheme.colorScheme.surface,
                                            disabledContentColor = MaterialTheme.colorScheme.inverseSurface
                                        )) {
                                        Icon(Icons.Default.Close, contentDescription = "Очистить")
                                    }
                                }
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.surface,
                                unfocusedBorderColor = MaterialTheme.colorScheme.surface,
                                cursorColor = MaterialTheme.colorScheme.inverseSurface,
                                focusedTextColor = MaterialTheme.colorScheme.inverseSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.inverseSurface,
                            ),
                            modifier = Modifier.weight(1f).padding(end = 8.dp)
                        )
                        IconButton(
                            onClick = {
                                if (searchQuery.text.isNotEmpty()) {
                                    isLoading = true
                                    errorOccurred = false
                                    showHistory = false
                                    coroutineScope.launch {
                                        try {
                                            SearchHistoryManager.saveQuery(context, searchQuery.text)
                                            history = SearchHistoryManager.getHistory(context)
                                            // Тут можно добавить реальный вызов API, если нужно
                                        } catch (e: Exception) {
                                            errorOccurred = true
                                        } finally {
                                            isLoading = false
                                        }
                                    }
                                }
                            },
                            colors = IconButtonColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.inverseSurface,
                                disabledContainerColor = MaterialTheme.colorScheme.surface,
                                disabledContentColor = MaterialTheme.colorScheme.inverseSurface
                            )) {
                            Icon(Icons.Default.Search, contentDescription = "Поиск")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors()
            )
        },
        modifier = Modifier.fillMaxWidth()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .padding(4.dp)
                .fillMaxSize()
        ) {
            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                errorOccurred -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(64.dp)
                            )
                            Text("Ошибка при поиске", color = MaterialTheme.colorScheme.inverseSurface)
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = {
                                errorOccurred = false
                                isLoading = true
                                coroutineScope.launch {
                                    try {
                                        SearchHistoryManager.saveQuery(context, searchQuery.text)
                                        history = SearchHistoryManager.getHistory(context)
                                    } catch (e: Exception) {
                                        errorOccurred = true
                                    } finally {
                                        isLoading = false
                                    }
                                }
                            }) {
                                Text("Обновить", color = MaterialTheme.colorScheme.inverseSurface)
                            }
                        }
                    }
                }

                showHistory && history.isNotEmpty() -> {
                    LazyColumn {
                        items(history) { item ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 4.dp)
                                    .clickable {
                                        searchQuery = TextFieldValue(item)
                                        showHistory = false
                                    },
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    contentColor = MaterialTheme.colorScheme.inverseSurface,
                                    disabledContainerColor = MaterialTheme.colorScheme.surface,
                                    disabledContentColor = MaterialTheme.colorScheme.inverseSurface
                                ),
                                shape = MaterialTheme.shapes.large,
                                elevation = CardDefaults.cardElevation(2.dp)
                            ) {
                                Text(
                                    text = item,
                                    modifier = Modifier.padding(16.dp),
//                                    color = MaterialTheme.colorScheme.inverseSurface,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                        item {
                            TextButton(
                                onClick = {
                                    SearchHistoryManager.clearHistory(context)
                                    history = emptyList()
                                    showHistory = false
                                },
                                shape = MaterialTheme.shapes.small,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    disabledContainerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary,
                                    disabledContentColor = MaterialTheme.colorScheme.onPrimary
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Text("Очистить историю", textAlign = TextAlign.Center)
                            }
                        }
                    }
                }

                filteredCars.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(64.dp)
                            )
                            Text("Ничего не найдено", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }

                else -> {
                    CarList(
                        cars = filteredCars,
                        onDeleteCar = { car ->
                            selectedCar = car
                            isSheetOpen = true
                        },
                        buttonText = "Назначить"
                    )

                    if (isSheetOpen) {
                        AssignBuyerBottomSheet(
                            onCloseSheet = { isSheetOpen = false },
                            sheetContent = {
                                ConstraintLayout(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    // Создаем ссылки для управления позиционированием компонентов
                                    val (emailField, confirmButton) = createRefs()

                                    OutlinedTextField(
                                        value = email,
                                        onValueChange = {
                                            email = it
                                        },
                                        placeholder = { Text(text = "some@gmail.com", color = MaterialTheme.colorScheme.outline) },
                                        label = { Text(text = "Почта", color = MaterialTheme.colorScheme.outline) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .constrainAs(emailField) {
                                                top.linkTo(parent.top)
                                                start.linkTo(parent.start)
                                                end.linkTo(parent.end)
                                            },
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = MaterialTheme.colorScheme.outline,
                                            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                                            cursorColor = MaterialTheme.colorScheme.outline,
                                            focusedTextColor = MaterialTheme.colorScheme.outline,
                                            unfocusedTextColor = MaterialTheme.colorScheme.outline,
                                        )
                                    )

                                    Button(
                                        onClick = {
                                            selectedCar?.let { car ->
                                                savedCarViewModel.saveCar(email, car.id.toLong(), AppState.getUser()!!.id)
                                            }
                                            isSheetOpen = false
                                        },
                                        colors = ButtonColors(
                                            containerColor = MaterialTheme.colorScheme.primary,
                                            contentColor = MaterialTheme.colorScheme.onPrimary,
                                            disabledContainerColor = MaterialTheme.colorScheme.primary,
                                            disabledContentColor = MaterialTheme.colorScheme.onPrimary
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .constrainAs(confirmButton) {
                                                top.linkTo(emailField.bottom, margin = 24.dp)
                                                start.linkTo(parent.start)
                                                end.linkTo(parent.end)
                                            },
                                        shape = MaterialTheme.shapes.small
                                    ) {
                                        Text("Назначить")
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
