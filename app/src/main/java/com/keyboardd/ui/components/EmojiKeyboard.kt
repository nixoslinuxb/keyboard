package com.keyboardd.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material3.Icon
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keyboardd.ui.theme.LocalKeyboardColors

data class EmojiCategory(val icon: String, val name: String, val emojis: List<String>)

@Composable
fun EmojiKeyboard(
    hapticsEnabled: Boolean,
    soundEnabled: Boolean,
    onEmojiClick: (String) -> Unit,
    onBackspaceClick: () -> Unit,
    onAbcToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardColors = LocalKeyboardColors.current

    val categories = remember {
        listOf(
            EmojiCategory("😀", "Smileys", listOf(
                "😀", "😃", "😄", "😁", "😆", "😅", "😂", "🤣", "🥹", "😉", "😊", "😇", "🥰", "😍", "🤩", "😘",
                "😗", "😚", "😋", "😛", "😜", "🤪", "🤨", "🧐", "🤓", "😎", "🥳", "😏", "😒", "😌", "😔", "😪",
                "🤤", "😴", "😷", "🤒", "🤕", "🤢", "🤮", "🤧", "🥵", "🥶", "🥴", "😵", "🤯", "🤠", "🥸", "😈"
            )),
            EmojiCategory("👋", "People", listOf(
                "👋", "🤚", "🖐️", "✋", "🖖", "👌", "🤌", "🤏", "✌️", "🤞", "🫰", "🤟", "🤘", "🤙", "👈", "👉",
                "👆", "🖕", "👇", "☝️", "👍", "👎", "✊", "👊", "🤛", "🤜", "👏", "🙌", "👐", "🤲", "🤝", "🙏",
                "✍️", "💅", "🤳", "💪", "🧠", "🫀", "🫁", "🦴", "👀", "👁️", "👅", "👄", "👶", "🧒", "👦", "👧"
            )),
            EmojiCategory("🐶", "Animals", listOf(
                "🐶", "🐱", "🐭", "🐹", "🐰", "🦊", "🐻", "🐼", "🐻‍❄️", "🐨", "🐯", "🦁", "🐮", "🐷", "🐸", "🐵",
                "🐔", "🐧", "🐦", "🐤", "🦆", "🦅", "🦉", "🦇", "🐺", "🐗", "🐴", "🦄", "🐝", "🪲", "🐛", "🦋",
                "🐌", "🐞", "🪰", "🪱", "🌸", "🌹", "🌺", "🌻", "🌼", "🌲", "🌴", "🌵", "🍁", "🍂", "🍃", "🍄"
            )),
            EmojiCategory("🍎", "Food", listOf(
                "🍏", "🍎", "🍐", "🍊", "🍋", "🍌", "🍉", "🍇", "🍓", "🫐", "🍈", "🍒", "🍑", "🥭", "🍍", "🥥",
                "🥝", "🍅", "🍆", "🥑", "🥦", "🌽", "🌶️", "🫑", "🍞", "🥐", "🥖", "🫓", "🥨", "🥯", "🥞", "🧇",
                "🧀", "🍖", "🍗", "🥩", "🥓", "🍔", "🍟", "🍕", "🌭", "🥪", "🌮", "🌯", "🥗", "🍿", "🍱", "🍣"
            )),
            EmojiCategory("⚽", "Activities", listOf(
                "⚽", "🏀", "🏈", "⚾", "🥎", "🎾", "🏐", "🏉", "🥏", "🎱", "🪀", "🏓", "🏸", "🏒", "🏑", "🥍",
                "🏏", "🪃", "🥅", "⛳", "🪁", "🏹", "🎣", "🦺", "🥊", "🥋", "🎽", "🛹", "🛼", "🛷", "⛸️", "🥌",
                "🎿", "🎯", "🪗", "🎸", "🪕", "🎻", "🎲", "🧩", "♟️", "🎨", "🎬", "🎤", "🎧", "📻", "🎷", "🎺"
            )),
            EmojiCategory("❤️", "Symbols", listOf(
                "❤️", "🧡", "💛", "💚", "💙", "💜", "🖤", "🤍", "🤎", "💔", "❣️", "💕", "💞", "💓", "💗", "💖",
                "💘", "💝", "💟", "☮️", "✝️", "☪️", "🕉️", "☸️", "✡️", "🔯", "🕎", "☯️", "☦️", "🛐", "⛎", "♈",
                "♉", "♊", "♋", "♌", "♍", "♎", "♏", "♐", "♑", "♒", "♓", "🆔", "⚛️", "🌐", "🏁", "🚩"
            ))
        )
    }

    var selectedCategoryIndex by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(260.dp)
            .background(keyboardColors.keyboardBackground)
    ) {
        // Category Tabs Header
        ScrollableTabRow(
            selectedTabIndex = selectedCategoryIndex,
            edgePadding = 8.dp,
            containerColor = keyboardColors.candidateBarBackground,
            contentColor = keyboardColors.keyTextColor,
            modifier = Modifier.fillMaxWidth()
        ) {
            categories.forEachIndexed { index, category ->
                Tab(
                    selected = selectedCategoryIndex == index,
                    onClick = { selectedCategoryIndex = index },
                    text = {
                        Text(
                            text = category.icon,
                            fontSize = 18.sp
                        )
                    }
                )
            }
        }

        // Emoji Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(6.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(categories[selectedCategoryIndex].emojis) { emoji ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onEmojiClick(emoji) }
                        .padding(vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = emoji,
                        fontSize = 24.sp
                    )
                }
            }
        }

        // Bottom Controls: [ABC] and [Backspace]
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            KeyButton(
                label = "ABC",
                keyType = KeyType.SPECIAL,
                showPopup = false,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = onAbcToggle,
                modifier = Modifier.width(100.dp)
            )

            KeyButton(
                keyType = KeyType.SPECIAL,
                showPopup = false,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = onBackspaceClick,
                onRepeatClick = onBackspaceClick,
                modifier = Modifier.width(100.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Backspace,
                    contentDescription = "Backspace",
                    tint = keyboardColors.specialKeyTextColor
                )
            }
        }
    }
}
