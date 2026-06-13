package com.example.smarthealth.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.smarthealth.R

enum class AppThemeColor(val seed: Color, val labelRes: Int) {
    GREEN(SeedGreen, R.string.color_green),
    BLUE(SeedBlue, R.string.color_blue),
    RED(SeedRed, R.string.color_red),
    PURPLE(SeedPurple, R.string.color_purple),
    ORANGE(SeedOrange, R.string.color_orange),
    YELLOW(SeedYellow, R.string.color_yellow)
}

private fun createAppColorScheme(seed: Color, isDark: Boolean): ColorScheme {
    return if (isDark) {
        darkColorScheme(
            primary = seed,
            onPrimary = Color.Black,
            primaryContainer = seed.copy(alpha = 0.45f),
            onPrimaryContainer = Color.White,
            secondary = seed.copy(alpha = 0.85f),
            onSecondary = Color.Black,
            background = Color(0xFF0C110F),
            surface = Color(0xFF141A18),
            onSurface = Color(0xFFE1E3E1),
            onBackground = Color(0xFFE1E3E1)
        )
    } else {
        lightColorScheme(
            primary = seed,
            onPrimary = Color.White,
            primaryContainer = seed.copy(alpha = 0.22f),
            onPrimaryContainer = seed,
            secondary = seed.copy(alpha = 0.75f),
            onSecondary = Color.White,
            background = Color(0xFFF9FFFC),
            surface = Color.White,
            onSurface = Color(0xFF191C1B),
            onBackground = Color(0xFF191C1B)
        )
    }
}

@Composable
fun SmartHealthTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    themeColor: AppThemeColor = AppThemeColor.GREEN,
    content: @Composable () -> Unit
) {
    val colorScheme = createAppColorScheme(themeColor.seed, darkTheme)
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
