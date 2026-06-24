package com.example.tingshu.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    onPrimary = Color.White,
    primaryContainer = Purple90,
    onPrimaryContainer = Purple10,
    secondary = PurpleGrey40,
    onSecondary = Color.White,
    secondaryContainer = PurpleGrey80,
    onSecondaryContainer = Purple10,
    tertiary = Pink40,
    onTertiary = Color.White,
    tertiaryContainer = Pink80,
    onTertiaryContainer = Purple10,
    error = Color(0xFFB3261E),
    onError = Color.White,
    errorContainer = Color(0xFFF9DEDC),
    onErrorContainer = Color(0xFF410E0B),
    background = Purple99,
    onBackground = Purple10,
    surface = Purple99,
    onSurface = Purple10,
    surfaceVariant = PurpleGrey80,
    onSurfaceVariant = PurpleGrey40,
    outline = Color(0xFF79747E),
    outlineVariant = Color(0xFFCAC4D0),
    scrim = Color.Black,
    inverseSurface = Purple20,
    inverseOnSurface = Purple95,
    inversePrimary = Purple80,
    surfaceTint = Purple40
)

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    onPrimary = Purple20,
    primaryContainer = Purple30,
    onPrimaryContainer = Purple90,
    secondary = PurpleGrey80,
    onSecondary = PurpleGrey20,
    secondaryContainer = PurpleGrey30,
    onSecondaryContainer = PurpleGrey90,
    tertiary = Pink80,
    onTertiary = Pink20,
    tertiaryContainer = Pink30,
    onTertiaryContainer = Pink90,
    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410),
    errorContainer = Color(0xFF8C1D18),
    onErrorContainer = Color(0xFFF9DEDC),
    background = Purple10,
    onBackground = Purple90,
    surface = Purple10,
    onSurface = Purple90,
    surfaceVariant = PurpleGrey30,
    onSurfaceVariant = PurpleGrey80,
    outline = Color(0xFF938F99),
    outlineVariant = Color(0xFF49454F),
    scrim = Color.Black,
    inverseSurface = Purple90,
    inverseOnSurface = Purple10,
    inversePrimary = Purple40,
    surfaceTint = Purple80
)

@Composable
fun TingshuAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
