package com.keyboardd.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.keyboardd.data.KeyboardThemeOption

data class KeyboardColors(
    val keyboardBackground: Color,
    val keyBackground: Color,
    val keyTextColor: Color,
    val specialKeyBackground: Color,
    val specialKeyTextColor: Color,
    val actionKeyBackground: Color,
    val actionKeyTextColor: Color,
    val keyShadowColor: Color,
    val candidateBarBackground: Color,
    val candidateTextColor: Color,
    val keyPopupBackground: Color,
    val keyPopupTextColor: Color
)

val GboardBlueColors = KeyboardColors(
    keyboardBackground = Color(0xFF1E2A38),
    keyBackground = Color(0xFF2C3E50),
    keyTextColor = Color(0xFFFFFFFF),
    specialKeyBackground = Color(0xFF243342),
    specialKeyTextColor = Color(0xFFE2E8F0),
    actionKeyBackground = Color(0xFF1A73E8),
    actionKeyTextColor = Color(0xFFFFFFFF),
    keyShadowColor = Color(0xFF0F172A),
    candidateBarBackground = Color(0xFF17212B),
    candidateTextColor = Color(0xFF64B5F6),
    keyPopupBackground = Color(0xFF1A73E8),
    keyPopupTextColor = Color(0xFFFFFFFF)
)

val LightKeyboardColors = KeyboardColors(
    keyboardBackground = Color(0xFFECEFF1),
    keyBackground = Color(0xFFFFFFFF),
    keyTextColor = Color(0xFF263238),
    specialKeyBackground = Color(0xFFCFD8DC),
    specialKeyTextColor = Color(0xFF37474F),
    actionKeyBackground = Color(0xFF1976D2),
    actionKeyTextColor = Color(0xFFFFFFFF),
    keyShadowColor = Color(0xFFB0BEC5),
    candidateBarBackground = Color(0xFFE0E0E0),
    candidateTextColor = Color(0xFF0D47A1),
    keyPopupBackground = Color(0xFF1976D2),
    keyPopupTextColor = Color(0xFFFFFFFF)
)

val DarkKeyboardColors = KeyboardColors(
    keyboardBackground = Color(0xFF121212),
    keyBackground = Color(0xFF242424),
    keyTextColor = Color(0xFFE0E0E0),
    specialKeyBackground = Color(0xFF1E1E1E),
    specialKeyTextColor = Color(0xFFB0BEC5),
    actionKeyBackground = Color(0xFF00897B),
    actionKeyTextColor = Color(0xFFFFFFFF),
    keyShadowColor = Color(0xFF000000),
    candidateBarBackground = Color(0xFF1A1A1A),
    candidateTextColor = Color(0xFF80CBC4),
    keyPopupBackground = Color(0xFF00897B),
    keyPopupTextColor = Color(0xFFFFFFFF)
)

val PitchBlackColors = KeyboardColors(
    keyboardBackground = Color(0xFF000000),
    keyBackground = Color(0xFF181818),
    keyTextColor = Color(0xFFFFFFFF),
    specialKeyBackground = Color(0xFF101010),
    specialKeyTextColor = Color(0xFFCCCCCC),
    actionKeyBackground = Color(0xFF6200EE),
    actionKeyTextColor = Color(0xFFFFFFFF),
    keyShadowColor = Color(0xFF050505),
    candidateBarBackground = Color(0xFF0A0A0A),
    candidateTextColor = Color(0xFFBB86FC),
    keyPopupBackground = Color(0xFF6200EE),
    keyPopupTextColor = Color(0xFFFFFFFF)
)

val LocalKeyboardColors = staticCompositionLocalOf { GboardBlueColors }

fun getKeyboardColors(option: KeyboardThemeOption, isSystemDark: Boolean): KeyboardColors {
    return when (option) {
        KeyboardThemeOption.GBOARD_BLUE -> GboardBlueColors
        KeyboardThemeOption.LIGHT -> LightKeyboardColors
        KeyboardThemeOption.DARK -> DarkKeyboardColors
        KeyboardThemeOption.PITCH_BLACK -> PitchBlackColors
        KeyboardThemeOption.SYSTEM -> if (isSystemDark) DarkKeyboardColors else LightKeyboardColors
    }
}
