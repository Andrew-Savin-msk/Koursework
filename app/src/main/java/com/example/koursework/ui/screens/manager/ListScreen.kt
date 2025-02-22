package com.example.koursework.ui.screens.manager

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.koursework.ui.components.CarList
import com.example.koursework.ui.components.CarViewModel
import com.example.koursework.ui.theme.MyAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(viewModel: CarViewModel = CarViewModel()) {
    val cars = viewModel.cars

    // Стейт для поискового запроса
    var searchQuery by remember { mutableStateOf("") }

    // Управление открытием/закрытием BottomSheet
    var isSheetOpen by remember { mutableStateOf(false) }

    // Поле для ввода email
    var email by remember { mutableStateOf("") }

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
                            // Логика поиска (если нужна)
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
                onDeleteCar = {
                    // При нажатии на кнопку в карточке открываем BottomSheet
                    isSheetOpen = true
                },
                buttonText = "Назначить покупателя"
            )
        }

        // Если флаг включён, показываем шторку
        if (isSheetOpen) {
            AssignBuyerBottomSheet(
                email = email,
                onEmailChange = { email = it },
                onCloseSheet = { isSheetOpen = false },
                onConfirm = {
                    // Тут можно вызвать логику из ViewModel:
                    // viewModel.assignBuyerToCar(selectedCar, email)
                    // Закрываем шторку
                    isSheetOpen = false
                }
            )
        }
    }
}



@Preview(showBackground = true, name = "Home Screen Preview")
@Composable
fun HomeScreenPreview() {
    MyAppTheme {
        ListScreen()
    }
}

// Modal window
@Composable
fun AssignBuyerDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit = {}
) {
    var email by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Назначить покупателя")
        },
        text = {
            Column {
                Text(text = "Введите email покупателя:")
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("some@mail.com") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(email)
                }
            ) {
                Text("Назначить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}


// SlidingCard
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignBuyerBottomSheet(
    email: String,
    onEmailChange: (String) -> Unit,
    onCloseSheet: () -> Unit,
    onConfirm: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = onCloseSheet,
        sheetState = sheetState,
        shape = MaterialTheme.shapes.large,
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Создаем ссылки для управления позиционированием компонентов
            val (emailField, confirmButton) = createRefs()

            OutlinedTextField(
                value = email,
                onValueChange = onEmailChange,
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
                onClick = onConfirm,
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
}