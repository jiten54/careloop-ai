package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryTealLight,
    onPrimary = MedicalNavy,
    primaryContainer = MedicalNavyContainer,
    onPrimaryContainer = PrimaryTealContainer,
    secondary = AccentCyan,
    onSecondary = MedicalNavy,
    secondaryContainer = MedicalNavySurface,
    onSecondaryContainer = Color.White,
    tertiary = AccentMint,
    onTertiary = MedicalNavy,
    background = MedicalNavy,
    surface = MedicalNavySurface,
    onBackground = Color(0xFFF1F5F9),
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = MedicalNavyContainer,
    onSurfaceVariant = Color(0xFFCBD5E1),
    error = AlertRed,
    errorContainer = Color(0xFF450A0A),
    onError = Color.White,
    onErrorContainer = Color(0xFFFCA5A5)
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryTeal,
    onPrimary = Color.White,
    primaryContainer = PrimaryTealContainer,
    onPrimaryContainer = OnPrimaryTealContainer,
    secondary = PrimaryTealLight,
    onSecondary = Color.White,
    secondaryContainer = SlateSurfaceVariant,
    onSecondaryContainer = SlateTextPrimary,
    tertiary = AccentMint,
    onTertiary = Color.White,
    tertiaryContainer = AccentMintContainer,
    onTertiaryContainer = OnAccentMintContainer,
    background = SlateBackground,
    surface = SlateSurface,
    onBackground = SlateTextPrimary,
    onSurface = SlateTextPrimary,
    surfaceVariant = SlateSurfaceVariant,
    onSurfaceVariant = SlateTextSecondary,
    outline = SlateBorder,
    error = AlertRed,
    errorContainer = AlertRedContainer,
    onError = Color.White,
    onErrorContainer = OnAlertRedContainer
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Preserve clinical branding
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
