package com.example.koursework.ui.components

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignBuyerBottomSheet(
    onCloseSheet: () -> Unit,
    sheetContent: @Composable () -> Unit
) {
    // Контроль поведения шторки: отключаем промежуточное состояние
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = onCloseSheet,
        sheetState = sheetState,
        shape = MaterialTheme.shapes.medium,

    ) {
        sheetContent()
    }
}
