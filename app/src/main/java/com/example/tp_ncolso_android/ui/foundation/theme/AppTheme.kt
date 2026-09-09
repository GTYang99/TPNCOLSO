package com.example.tp_ncolso_android.ui.foundation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Immutable
data class AppColors(
    val brandPrimary: Color,
    val onBrandPrimary: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val borderDefault: Color,
    val surface: Color,
    val surfaceMuted: Color,
    val error: Color,
    val onError: Color,
    val fieldErrorBorder: Color,
    val errorText: Color,
    val scrim: Color,
)

@Immutable
data class AppTypography(
    val screenTitle: TextStyle,
    val sectionTitle: TextStyle,
    val fieldLabel: TextStyle,
    val body: TextStyle,
    val supporting: TextStyle,
    val buttonLabel: TextStyle,
)

@Immutable
data class AppSpacing(
    val xs: Dp,
    val sm: Dp,
    val md: Dp,
    val lg: Dp,
    val xl: Dp,
    val minimumTouchTarget: Dp,
)

object AppThemeTokens {
    val colors: AppColors
        @Composable get() = LocalAppColors.current
    val typography: AppTypography
        @Composable get() = LocalAppTypography.current
    val spacing: AppSpacing
        @Composable get() = LocalAppSpacing.current
}

private val LightAppColors = AppColors(
    brandPrimary = Color(0xFF0D23AC),
    onBrandPrimary = Color.White,
    textPrimary = Color(0xFF303133),
    textSecondary = Color(0xFF606266),
    borderDefault = Color(0xFFDCDFE6),
    surface = Color(0xFFFFFBFE),
    surfaceMuted = Color(0xFFE7E9F6),
    error = Color(0xFFC8320A),
    onError = Color.White,
    fieldErrorBorder = Color(0xFFC8320A),
    errorText = Color(0xFFE00000),
    scrim = Color(0x99000000),
)

private val DarkAppColors = AppColors(
    brandPrimary = Color(0xFF67DBAE),
    onBrandPrimary = Color(0xFF003828),
    textPrimary = Color(0xFFE4EDE7),
    textSecondary = Color(0xFFBFCBC4),
    borderDefault = Color(0xFF51645B),
    surface = Color(0xFF101511),
    surfaceMuted = Color(0xFF1A211C),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    fieldErrorBorder = Color(0xFFFFB4AB),
    errorText = Color(0xFFFFB4AB),
    scrim = Color(0xCC000000),
)

private val AppSpacingDefaults = AppSpacing(
    xs = 4.dp,
    sm = 8.dp,
    md = 16.dp,
    lg = 24.dp,
    xl = 32.dp,
    minimumTouchTarget = 48.dp,
)

private val AppTypographyDefaults = AppTypography(
    screenTitle = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.SemiBold, fontSize = 28.sp, lineHeight = 34.sp),
    sectionTitle = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.SemiBold, fontSize = 20.sp, lineHeight = 26.sp),
    fieldLabel = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Medium, fontSize = 16.sp, lineHeight = 24.sp),
    body = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 24.sp),
    supporting = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Normal, fontSize = 13.sp, lineHeight = 18.sp),
    buttonLabel = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, lineHeight = 20.sp),
)

private val LocalAppColors = staticCompositionLocalOf { LightAppColors }
private val LocalAppTypography = staticCompositionLocalOf { AppTypographyDefaults }
private val LocalAppSpacing = staticCompositionLocalOf { AppSpacingDefaults }

private fun materialColors(appColors: AppColors): ColorScheme = lightColorScheme(
    primary = appColors.brandPrimary,
    onPrimary = appColors.onBrandPrimary,
    background = appColors.surface,
    onBackground = appColors.textPrimary,
    surface = appColors.surface,
    onSurface = appColors.textPrimary,
    error = appColors.error,
    onError = appColors.onError,
)

private fun materialTypography(appTypography: AppTypography): Typography = Typography(
    titleLarge = appTypography.screenTitle,
    titleMedium = appTypography.sectionTitle,
    bodyLarge = appTypography.body,
    bodyMedium = appTypography.body,
    labelLarge = appTypography.buttonLabel,
    labelMedium = appTypography.fieldLabel,
    bodySmall = appTypography.supporting,
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) DarkAppColors else LightAppColors
    androidx.compose.runtime.CompositionLocalProvider(
        LocalAppColors provides colors,
        LocalAppTypography provides AppTypographyDefaults,
        LocalAppSpacing provides AppSpacingDefaults,
    ) {
        MaterialTheme(
            colorScheme = materialColors(colors),
            typography = materialTypography(AppTypographyDefaults),
            shapes = Shapes(),
            content = content,
        )
    }
}
