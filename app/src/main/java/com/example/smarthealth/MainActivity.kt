package com.example.smarthealth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.smarthealth.uii.InputScreen
import com.example.smarthealth.ui.theme.AppThemeColor
import com.example.smarthealth.ui.theme.SmartHealthTheme
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val systemTheme = isSystemInDarkTheme()
            var isDarkMode by remember { mutableStateOf<Boolean?>(null) }
            var selectedThemeColor by remember { mutableStateOf(AppThemeColor.GREEN) }
            var language by remember { mutableStateOf("en") }
            var useMetric by remember { mutableStateOf(true) }
            
            val activeDarkMode = isDarkMode ?: systemTheme
            
            // Manual language switching logic
            val context = LocalContext.current
            val localizedContext = remember(language) {
                val locale = Locale(language)
                Locale.setDefault(locale)
                val config = context.resources.configuration
                config.setLocale(locale)
                context.createConfigurationContext(config)
            }

            CompositionLocalProvider(LocalContext provides localizedContext) {
                SmartHealthTheme(
                    darkTheme = activeDarkMode,
                    themeColor = selectedThemeColor
                ) {
                    InputScreen(
                        isDarkMode = activeDarkMode,
                        onThemeToggle = { isDarkMode = !activeDarkMode },
                        currentThemeColor = selectedThemeColor,
                        onColorSelect = { selectedThemeColor = it },
                        onLanguageToggle = { language = if (language == "en") "ru" else "en" },
                        useMetric = useMetric,
                        onUnitToggle = { useMetric = !useMetric }
                    )
                }
            }
        }
    }
}
