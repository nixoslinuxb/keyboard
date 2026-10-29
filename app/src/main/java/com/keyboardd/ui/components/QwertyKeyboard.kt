package com.keyboardd.ui.components

import android.view.inputmethod.EditorInfo
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.automirrored.filled.KeyboardReturn
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardCapslock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.keyboardd.ui.theme.LocalKeyboardColors

enum class ShiftState {
    OFF,
    SHIFT,
    CAPS_LOCK
}

@Composable
fun QwertyKeyboard(
    shiftState: ShiftState,
    showNumberRow: Boolean,
    hapticsEnabled: Boolean,
    soundEnabled: Boolean,
    imeOptions: Int,
    onKeyClick: (String) -> Unit,
    onBackspaceClick: () -> Unit,
    onShiftClick: () -> Unit,
    onSymbolToggle: () -> Unit,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardColors = LocalKeyboardColors.current

    val isUpper = shiftState == ShiftState.SHIFT || shiftState == ShiftState.CAPS_LOCK

    // Turkish Q Layout Keys
    val row1 = if (isUpper) listOf("Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "Ğ", "Ü")
               else listOf("q", "w", "e", "r", "t", "y", "u", "ı", "o", "p", "ğ", "ü")

    val row2 = if (isUpper) listOf("A", "S", "D", "F", "G", "H", "J", "K", "L", "Ş", "İ")
               else listOf("a", "s", "d", "f", "g", "h", "j", "k", "l", "ş", "i")

    val row3 = if (isUpper) listOf("Z", "X", "C", "V", "B", "N", "M", "Ö", "Ç")
               else listOf("z", "x", "c", "v", "b", "n", "m", "ö", "ç")

    val numbersRow = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0")

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp, vertical = 2.dp)
    ) {
        // Top Number Row (Optional)
        if (showNumberRow) {
            Row(modifier = Modifier.fillMaxWidth()) {
                numbersRow.forEach { num ->
                    KeyButton(
                        label = num,
                        keyType = KeyType.SPECIAL,
                        keyHeight = 44.dp,
                        hapticsEnabled = hapticsEnabled,
                        soundEnabled = soundEnabled,
                        onClick = { onKeyClick(num) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Row 1
        Row(modifier = Modifier.fillMaxWidth()) {
            row1.forEachIndexed { index, char ->
                val subLabel = if (!showNumberRow && index < 10) numbersRow[index] else null
                KeyButton(
                    label = char,
                    subLabel = subLabel,
                    keyType = KeyType.NORMAL,
                    hapticsEnabled = hapticsEnabled,
                    soundEnabled = soundEnabled,
                    onClick = { onKeyClick(char) },
                    onLongClick = if (subLabel != null) { { onKeyClick(subLabel) } } else null,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Row 2
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp)
        ) {
            row2.forEach { char ->
                KeyButton(
                    label = char,
                    keyType = KeyType.NORMAL,
                    hapticsEnabled = hapticsEnabled,
                    soundEnabled = soundEnabled,
                    onClick = { onKeyClick(char) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Row 3 (Shift, ZXCV, Backspace)
        Row(modifier = Modifier.fillMaxWidth()) {
            // Shift Key
            KeyButton(
                keyType = if (shiftState != ShiftState.OFF) KeyType.ACTION else KeyType.SPECIAL,
                showPopup = false,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = onShiftClick,
                modifier = Modifier.weight(1.5f)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardCapslock,
                    contentDescription = "Shift",
                    tint = if (shiftState != ShiftState.OFF) keyboardColors.actionKeyTextColor else keyboardColors.specialKeyTextColor
                )
            }

            row3.forEach { char ->
                KeyButton(
                    label = char,
                    keyType = KeyType.NORMAL,
                    hapticsEnabled = hapticsEnabled,
                    soundEnabled = soundEnabled,
                    onClick = { onKeyClick(char) },
                    modifier = Modifier.weight(1f)
                )
            }

            // Backspace Key with Auto-Repeat
            KeyButton(
                keyType = KeyType.SPECIAL,
                showPopup = false,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = onBackspaceClick,
                onRepeatClick = onBackspaceClick,
                modifier = Modifier.weight(1.5f)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Backspace,
                    contentDescription = "Backspace",
                    tint = keyboardColors.specialKeyTextColor
                )
            }
        }

        // Bottom Row: [?123] [,] [Spacebar] [.] [Action/Enter]
        Row(modifier = Modifier.fillMaxWidth()) {
            // Symbols Switch Button (?123)
            KeyButton(
                label = "?123",
                keyType = KeyType.SPECIAL,
                showPopup = false,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = onSymbolToggle,
                modifier = Modifier.weight(1.5f)
            )

            // Comma Key
            KeyButton(
                label = ",",
                keyType = KeyType.NORMAL,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = { onKeyClick(",") },
                modifier = Modifier.weight(1f)
            )

            // Spacebar Key (Blank / No text)
            KeyButton(
                label = null,
                keyType = KeyType.NORMAL,
                showPopup = false,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = { onKeyClick(" ") },
                modifier = Modifier.weight(4.5f)
            )

            // Period Key
            KeyButton(
                label = ".",
                keyType = KeyType.NORMAL,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = { onKeyClick(".") },
                modifier = Modifier.weight(1f)
            )

            // Action / Enter Key
            val actionMask = imeOptions and EditorInfo.IME_MASK_ACTION
            KeyButton(
                keyType = KeyType.ACTION,
                showPopup = false,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = onActionClick,
                modifier = Modifier.weight(1.8f)
            ) {
                when (actionMask) {
                    EditorInfo.IME_ACTION_SEARCH -> {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = keyboardColors.actionKeyTextColor
                        )
                    }
                    EditorInfo.IME_ACTION_SEND -> {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            tint = keyboardColors.actionKeyTextColor
                        )
                    }
                    EditorInfo.IME_ACTION_GO, EditorInfo.IME_ACTION_NEXT -> {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Go",
                            tint = keyboardColors.actionKeyTextColor
                        )
                    }
                    EditorInfo.IME_ACTION_DONE -> {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Done",
                            tint = keyboardColors.actionKeyTextColor
                        )
                    }
                    else -> {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardReturn,
                            contentDescription = "Enter",
                            tint = keyboardColors.actionKeyTextColor
                        )
                    }
                }
            }
        }
    }
}
