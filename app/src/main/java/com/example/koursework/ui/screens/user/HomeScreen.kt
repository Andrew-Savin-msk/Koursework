package com.example.koursework.ui.screens.user

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.koursework.ui.components.CarList
import com.example.koursework.ui.components.CarViewModel
import com.example.koursework.ui.theme.MyAppTheme
import kotlinx.coroutines.launch
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.Icon
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.koursework.ui.outbox.SearchHistoryManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: CarViewModel = CarViewModel()) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var searchQuery by rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("")) }
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
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = {
                                searchQuery = it
                                showHistory = it.text.isEmpty()
                            },
                            placeholder = { Text("Поиск по названию авто", color = MaterialTheme.colorScheme.inverseSurface) },
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
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
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
        modifier = Modifier.fillMaxSize()
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
                            viewModel.addToFavorites(car) { success ->
                                val msg = if (success) "Добавлено в избранное" else "Не удалось добавить"
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            }
                        },
                        buttonText = "Сохранить"
                    )
                }
            }
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
