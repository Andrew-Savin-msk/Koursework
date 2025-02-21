package com.example.koursework.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.example.koursework.ui.outbox.AppState
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val LightColorScheme = lightColorScheme(
    background = BackgroundLight,
    outline = TextGreyLight,
    outlineVariant = TextGreyLight,
    primary = ButtonLight,
    onPrimary = ButtonTextLight,
    surface = TopBottomLight,
    surfaceContainerHigh = PressedBottomButtonLight,
    inverseSurface = BackgroundDark,
    primaryContainer = BoxGreyLight,
    onPrimaryContainer = FocusedContentLight,
)

private val DarkColorScheme = darkColorScheme(
    background = BackgroundDark,
    outline = TextGreyDark,
    outlineVariant = TextGreyDark,
    primary = ButtonDark,
    onPrimary = ButtonTextDark,
    surface = TopBottomDark,
    surfaceContainerHigh = PressedBottomButtonDark,
    inverseSurface = BackgroundLight,
    primaryContainer = BoxGreyDark,
    onPrimaryContainer = FocusedContentDark,
)

/**
 * Основная функция темы, которую будем вызывать в Activity или корневом Composable.
 */
@Composable
fun MyAppTheme(
    darkTheme: Boolean = AppState.isDarkTheme,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setSystemBarsColor(color = colorScheme.background)
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
