package com.example.koursework.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val LightColorScheme = lightColorScheme(
    background = BackgroundLight,
    outline = TextGreyLight,
    outlineVariant = TextGreyLight,
    primary = ButtonLight,
    surface = TopBottomLight,
    surfaceContainerHigh = PressedBottomButtonLight,
    primaryContainer = BoxGreyLight,
    onPrimaryContainer = FocusedContentLight
)

private val DarkColorScheme = darkColorScheme(
    background = BackgroundDark,
    outline = TextGreyDark,
    outlineVariant = TextGreyDark,
    primary = ButtonDark,
    surface = TopBottomDark,
    surfaceContainerHigh = PressedBottomButtonDark,
    primaryContainer = BoxGreyDark,
    onPrimaryContainer = FocusedContentDark
)

/**
 * Основная функция темы, которую будем вызывать в Activity или корневом Composable.
 */
@Composable
fun MyAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
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
