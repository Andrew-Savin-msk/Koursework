package com.example.koursework.ui.screens.manager

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil3.compose.rememberAsyncImagePainter
import com.example.koursework.ui.components.AssignBuyerBottomSheet
import com.example.koursework.ui.components.CarList
import com.example.koursework.ui.components.CarViewModel
import com.example.koursework.ui.theme.MyAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(viewModel: CarViewModel = CarViewModel()) {
    val cars = viewModel.cars
    var searchQuery by remember { mutableStateOf("") }
    var isSheetOpen by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Поля для формы
    var brandAndModel by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Функция для получения имени файла из Uri
    fun getFileNameFromUri(context: Context, uri: Uri): String {
        var fileName = ""
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex >= 0) {
                    fileName = it.getString(nameIndex)
                }
            }
        }
        return fileName
    }

    // Получаем имя файла для отображения в TextField
    val selectedImageName = selectedImageUri?.let { uri ->
        getFileNameFromUri(context, uri)
    } ?: ""

    // Лаунчер для выбора изображения
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
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
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.inverseSurface
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
        modifier = Modifier.fillMaxWidth(),
        // Добавляем "плавающую" кнопку
        floatingActionButton = {
            FloatingActionButton(
                onClick = { isSheetOpen = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Добавить объявление"
                )
            }
        }
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
                    isSheetOpen = true
                },
                buttonText = "Реактировать"
            )
        }

        // Наша нижняя шторка (BottomSheet)
        if (isSheetOpen) {
            AssignBuyerBottomSheet(
                onCloseSheet = { isSheetOpen = false },
                sheetContent = {
                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // Создаем ссылки для всех элементов
                        val (
                            rowImagePicker, imageBox, brandField,
                            priceField, descriptionField, saveButton
                        ) = createRefs()

                        // Ряд для поля ввода выбранного изображения и иконки загрузки
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .constrainAs(rowImagePicker) {
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                }
                        ) {
                            OutlinedTextField(
                                value = selectedImageName,
                                onValueChange = { /* Ручное редактирование запрещено */ },
                                label = { Text("Выбранное изображение", color = MaterialTheme.colorScheme.inverseSurface) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.outline,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                                    cursorColor = MaterialTheme.colorScheme.outline,
                                    focusedTextColor = MaterialTheme.colorScheme.outline,
                                    unfocusedTextColor = MaterialTheme.colorScheme.outline,
                                ),
                                trailingIcon = {
                                    if (selectedImageUri != null) {
                                        IconButton(
                                            onClick = { /* Очищаем выбранное изображение */ selectedImageUri = null },
                                            colors = IconButtonDefaults.iconButtonColors(
                                                containerColor = MaterialTheme.colorScheme.surface,
                                                contentColor = MaterialTheme.colorScheme.inverseSurface
                                            )
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Cancel,
                                                contentDescription = "Очистить выбор"
                                            )
                                        }
                                    }
                                },
                                readOnly = true,
                                modifier = Modifier.weight(1f)
                            )

                            IconButton(
                                onClick = { imagePickerLauncher.launch("image/*") }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FileUpload,
                                    contentDescription = "Загрузить фото"
                                )
                            }
                        }

                        // Блок для изображения: если изображение выбрано – показываем его,
                        // иначе оставляем пустое пространство с нулевой высотой.
                        if (selectedImageUri != null) {
                            Image(
                                painter = rememberAsyncImagePainter(model = selectedImageUri),
                                contentDescription = "Selected image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .border(BorderStroke(1.dp, Color.Gray), RoundedCornerShape(8.dp))
                                    .constrainAs(imageBox) {
                                        top.linkTo(rowImagePicker.bottom, margin = 8.dp)
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                    }
                            )
                        } else {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(0.dp)
                                    .constrainAs(imageBox) {
                                        top.linkTo(rowImagePicker.bottom, margin = 8.dp)
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                    }
                            )
                        }

                        // Поле ввода "Марка и модель"
                        OutlinedTextField(
                            value = brandAndModel,
                            onValueChange = { brandAndModel = it },
                            label = { Text("Марка и модель", color = MaterialTheme.colorScheme.inverseSurface) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.outline,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                                cursorColor = MaterialTheme.colorScheme.outline,
                                focusedTextColor = MaterialTheme.colorScheme.outline,
                                unfocusedTextColor = MaterialTheme.colorScheme.outline,
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .constrainAs(brandField) {
                                    top.linkTo(imageBox.bottom, margin = 8.dp)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                }
                        )

                        // Поле ввода "Цена"
                        OutlinedTextField(
                            value = price,
                            onValueChange = { price = it },
                            label = { Text("Цена", color = MaterialTheme.colorScheme.inverseSurface) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.outline,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                                cursorColor = MaterialTheme.colorScheme.outline,
                                focusedTextColor = MaterialTheme.colorScheme.outline,
                                unfocusedTextColor = MaterialTheme.colorScheme.outline,
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .constrainAs(priceField) {
                                    top.linkTo(brandField.bottom, margin = 8.dp)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                }
                        )

                        // Поле ввода "Описание"
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Описание", color = MaterialTheme.colorScheme.inverseSurface) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.outline,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                                cursorColor = MaterialTheme.colorScheme.outline,
                                focusedTextColor = MaterialTheme.colorScheme.outline,
                                unfocusedTextColor = MaterialTheme.colorScheme.outline,
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .constrainAs(descriptionField) {
                                    top.linkTo(priceField.bottom, margin = 8.dp)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                }
                        )

                        Button(
                            onClick = {
                                if (selectedImageUri == null) {
                                    Toast.makeText(context, "Пожалуйста, выберите фото", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Данные сохранены", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            },
                            shape = MaterialTheme.shapes.small,
                            colors = ButtonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                                disabledContainerColor = MaterialTheme.colorScheme.primary,
                                disabledContentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .constrainAs(saveButton) {
                                    top.linkTo(descriptionField.bottom, margin = 8.dp)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                }
                        ) {
                            Text("Сохранить")
                        }
                    }
                }
            )
        }
    }
}



@Preview(showBackground = true, name = "Home Screen Preview")
@Composable
fun EditScreenPreview() {
    MyAppTheme {
        EditScreen()
    }
}