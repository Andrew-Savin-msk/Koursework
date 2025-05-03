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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil3.compose.rememberAsyncImagePainter
import com.example.koursework.ui.components.AssignBuyerBottomSheet
import com.example.koursework.ui.components.CarList
import com.example.koursework.ui.components.CarViewModel
import com.example.koursework.ui.outbox.SearchHistoryManager
import com.example.koursework.ui.theme.MyAppTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(viewModel: CarViewModel = CarViewModel()) {
    var isSheetOpen by remember { mutableStateOf(false) }
    val context = LocalContext.current
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