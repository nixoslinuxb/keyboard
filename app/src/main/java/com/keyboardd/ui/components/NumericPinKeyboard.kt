package com.keyboardd.ui.components

import android.view.inputmethod.EditorInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.automirrored.filled.KeyboardReturn
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keyboardd.ui.theme.LocalKeyboardColors

@Composable
fun NumericPinKeyboard(
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

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Modern Header Badge for PIN / Banking Password Entry
        Box(
            modifier = Modifier
                .padding(bottom = 6.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(keyboardColors.keyBackground.copy(alpha = 0.6f))
                .padding(horizontal = 16.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = keyboardColors.candidateTextColor,
                    modifier = Modifier.padding(end = 6.dp)
                )
                Text(
                    text = "Güvenli Sayısal Parola (PIN)",
                    color = keyboardColors.keyTextColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Row 1: 1 2 3
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("1", "2", "3").forEach { num ->
                KeyButton(
                    label = num,
                    keyType = KeyType.NORMAL,
                    keyHeight = 54.dp,
                    hapticsEnabled = hapticsEnabled,
                    soundEnabled = soundEnabled,
                    onClick = { onKeyClick(num) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Row 2: 4 5 6
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("4", "5", "6").forEach { num ->
                KeyButton(
                    label = num,
                    keyType = KeyType.NORMAL,
                    keyHeight = 54.dp,
                    hapticsEnabled = hapticsEnabled,
                    soundEnabled = soundEnabled,
                    onClick = { onKeyClick(num) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Row 3: 7 8 9
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("7", "8", "9").forEach { num ->
                KeyButton(
                    label = num,
                    keyType = KeyType.NORMAL,
                    keyHeight = 54.dp,
                    hapticsEnabled = hapticsEnabled,
                    soundEnabled = soundEnabled,
                    onClick = { onKeyClick(num) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Row 4: [ABC] | 0 | [Backspace]
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // ABC Switch
            KeyButton(
                label = "ABC",
                keyType = KeyType.SPECIAL,
                keyHeight = 54.dp,
                showPopup = false,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = onAbcToggle,
                modifier = Modifier.weight(1f)
            )

            // 0
            KeyButton(
                label = "0",
                keyType = KeyType.NORMAL,
                keyHeight = 54.dp,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = { onKeyClick("0") },
                modifier = Modifier.weight(1f)
            )

            // Backspace Key
            KeyButton(
                keyType = KeyType.SPECIAL,
                keyHeight = 54.dp,
                showPopup = false,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = onBackspaceClick,
                onRepeatClick = onBackspaceClick,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Backspace,
                    contentDescription = "Backspace",
                    tint = keyboardColors.specialKeyTextColor
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Bottom Action Button (Wide Enter/Done/Check Action)
        val actionMask = imeOptions and EditorInfo.IME_MASK_ACTION
        KeyButton(
            keyType = KeyType.ACTION,
            keyHeight = 48.dp,
            showPopup = false,
            hapticsEnabled = hapticsEnabled,
            soundEnabled = soundEnabled,
            onClick = onActionClick,
            modifier = Modifier.fillMaxWidth()
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
