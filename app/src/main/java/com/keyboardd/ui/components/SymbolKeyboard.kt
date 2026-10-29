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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.keyboardd.ui.theme.LocalKeyboardColors

@Composable
fun SymbolKeyboard(
    hapticsEnabled: Boolean,
    soundEnabled: Boolean,
    imeOptions: Int,
    onKeyClick: (String) -> Unit,
    onBackspaceClick: () -> Unit,
    onAbcToggle: () -> Unit,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardColors = LocalKeyboardColors.current
    var isAltPage by remember { mutableStateOf(false) }

    val page1Row1 = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0")
    val page1Row2 = listOf("@", "#", "$", "%", "&", "-", "+", "(", ")", "/")
    val page1Row3 = listOf("*", "\"", "'", ":", ";", "!", "?")

    val page2Row1 = listOf("~", "`", "|", "•", "√", "π", "÷", "×", "¶", "∆")
    val page2Row2 = listOf("£", "¢", "€", "¥", "^", "°", "=", "{", "}", "\\")
    val page2Row3 = listOf("%", "©", "®", "™", "✓", "[", "]", "<", ">")

    val row1 = if (isAltPage) page2Row1 else page1Row1
    val row2 = if (isAltPage) page2Row2 else page1Row2
    val row3 = if (isAltPage) page2Row3 else page1Row3

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp, vertical = 2.dp)
    ) {
        // Row 1
        Row(modifier = Modifier.fillMaxWidth()) {
            row1.forEach { char ->
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

        // Row 2
        Row(modifier = Modifier.fillMaxWidth()) {
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

        // Row 3 (Alt Page Toggle, Symbols, Backspace)
        Row(modifier = Modifier.fillMaxWidth()) {
            // Toggle Page 1 / Page 2 (?123 / =<)
            KeyButton(
                label = if (isAltPage) "?123" else "=\\<",
                keyType = KeyType.SPECIAL,
                showPopup = false,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = { isAltPage = !isAltPage },
                modifier = Modifier.weight(1.5f)
            )

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

            // Backspace Key
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

        // Bottom Row: [ABC] [,] [Spacebar] [.] [Action/Enter]
        Row(modifier = Modifier.fillMaxWidth()) {
            // ABC Switch Button
            KeyButton(
                label = "ABC",
                keyType = KeyType.SPECIAL,
                showPopup = false,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = onAbcToggle,
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
