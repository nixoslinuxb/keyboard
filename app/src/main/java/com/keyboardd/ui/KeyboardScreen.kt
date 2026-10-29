package com.keyboardd.ui

import android.text.InputType
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.keyboardd.data.DictionaryRepository
import com.keyboardd.data.KeyboardSettings
import com.keyboardd.ui.components.CandidateStrip
import com.keyboardd.ui.components.EmojiKeyboard
import com.keyboardd.ui.components.NumericPinKeyboard
import com.keyboardd.ui.components.QwertyKeyboard
import com.keyboardd.ui.components.ShiftState
import com.keyboardd.ui.components.SymbolKeyboard
import com.keyboardd.ui.theme.KeyboardTheme
import com.keyboardd.ui.theme.LocalKeyboardColors

enum class KeyboardMode {
    ALPHABET,
    SYMBOLS,
    EMOJI,
    NUMERIC_PIN
}

fun isBankingOrNumericPassword(editorInfo: EditorInfo?): Boolean {
    if (editorInfo == null) return false
    val inputType = editorInfo.inputType
    if (inputType == InputType.TYPE_NULL) return false

    val typeClass = inputType and InputType.TYPE_MASK_CLASS
    val typeVariation = inputType and InputType.TYPE_MASK_VARIATION

    val isNumberClass = typeClass == InputType.TYPE_CLASS_NUMBER || typeClass == InputType.TYPE_CLASS_PHONE

    val isPasswordVariation = typeVariation == InputType.TYPE_NUMBER_VARIATION_PASSWORD ||
            typeVariation == InputType.TYPE_TEXT_VARIATION_PASSWORD ||
            typeVariation == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD ||
            typeVariation == InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD

    val hasPasswordBit = (inputType and InputType.TYPE_NUMBER_VARIATION_PASSWORD) != 0 ||
            (inputType and InputType.TYPE_TEXT_VARIATION_PASSWORD) != 0 ||
            (inputType and InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) != 0

    val hintText = (editorInfo.hintText ?: editorInfo.label ?: "").toString().lowercase()
    val isPasswordHint = hintText.contains("şifre") || hintText.contains("sifre") ||
            hintText.contains("parola") || hintText.contains("pin") || hintText.contains("pass") ||
            hintText.contains("tc") || hintText.contains("kod") || hintText.contains("code")

    return isNumberClass || isPasswordVariation || hasPasswordBit || isPasswordHint
}

@Composable
fun KeyboardScreen(
    inputConnectionProvider: () -> InputConnection?,
    editorInfo: EditorInfo?,
    dictionaryRepository: DictionaryRepository
) {
    val context = LocalContext.current
    val settings = remember { KeyboardSettings.getInstance(context) }

    val hapticsEnabled by settings.hapticsEnabled.collectAsState()
    val soundEnabled by settings.soundEnabled.collectAsState()
    val showNumberRow by settings.showNumberRow.collectAsState()
    val autoSuggestionsEnabled by settings.autoSuggestionsEnabled.collectAsState()
    val selectedTheme by settings.selectedTheme.collectAsState()

    var keyboardMode by remember { mutableStateOf(KeyboardMode.ALPHABET) }
    var shiftState by remember { mutableStateOf(ShiftState.OFF) }

    var composingWord by remember { mutableStateOf("") }
    var candidates by remember { mutableStateOf(emptyList<String>()) }

    LaunchedEffect(editorInfo) {
        if (isBankingOrNumericPassword(editorInfo)) {
            keyboardMode = KeyboardMode.NUMERIC_PIN
        } else if (keyboardMode == KeyboardMode.NUMERIC_PIN) {
            keyboardMode = KeyboardMode.ALPHABET
        }
    }

    fun updateComposingText(newWord: String) {
        composingWord = newWord
        if (autoSuggestionsEnabled && composingWord.length >= 2) {
            candidates = dictionaryRepository.getSuggestions(composingWord)
        } else {
            candidates = emptyList()
        }
    }

    fun handleKeyClick(text: String) {
        val ic = inputConnectionProvider() ?: return
        ic.commitText(text, 1)

        if (text == " ") {
            composingWord = ""
            candidates = emptyList()
        } else if (text.length == 1 && text[0].isLetter()) {
            updateComposingText(composingWord + text)
        } else {
            composingWord = ""
            candidates = emptyList()
        }

        // Auto revert single Shift to OFF after typing letter
        if (shiftState == ShiftState.SHIFT) {
            shiftState = ShiftState.OFF
        }
    }

    fun handleBackspace() {
        val ic = inputConnectionProvider() ?: return
        if (composingWord.isNotEmpty()) {
            val newWord = composingWord.dropLast(1)
            updateComposingText(newWord)
        }
        val deleted = ic.deleteSurroundingText(1, 0)
        if (!deleted) {
            ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
            ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL))
        }
    }

    fun handleSelectCandidate(candidate: String) {
        val ic = inputConnectionProvider() ?: return
        if (composingWord.isNotEmpty()) {
            ic.deleteSurroundingText(composingWord.length, 0)
        }
        ic.commitText("$candidate ", 1)
        composingWord = ""
        candidates = emptyList()
    }

    fun handleActionClick() {
        val ic = inputConnectionProvider() ?: return
        val actionId = editorInfo?.actionId ?: EditorInfo.IME_ACTION_NONE
        val actionMask = (editorInfo?.imeOptions ?: 0) and EditorInfo.IME_MASK_ACTION

        val effectiveAction = if (actionId != EditorInfo.IME_ACTION_NONE) actionId else actionMask

        var handled = false
        if (effectiveAction != EditorInfo.IME_ACTION_NONE && effectiveAction != EditorInfo.IME_ACTION_UNSPECIFIED) {
            handled = ic.performEditorAction(effectiveAction)
        }

        if (!handled) {
            val keyEventDownSent = ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER))
            val keyEventUpSent = ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER))
            if (!keyEventDownSent || !keyEventUpSent) {
                ic.commitText("\n", 1)
            }
        }

        composingWord = ""
        candidates = emptyList()
    }

    KeyboardTheme(themeOption = selectedTheme) {
        val keyboardColors = LocalKeyboardColors.current
        val imeOptions = editorInfo?.imeOptions ?: EditorInfo.IME_ACTION_NONE

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(keyboardColors.keyboardBackground)
                .padding(bottom = 6.dp)
        ) {
            // Suggestion Bar
            if (autoSuggestionsEnabled && keyboardMode == KeyboardMode.ALPHABET) {
                CandidateStrip(
                    candidates = candidates,
                    onSelectCandidate = { handleSelectCandidate(it) }
                )
            }

            // Keyboard Body depending on active Mode
            when (keyboardMode) {
                KeyboardMode.NUMERIC_PIN -> {
                    NumericPinKeyboard(
                        hapticsEnabled = hapticsEnabled,
                        soundEnabled = soundEnabled,
                        imeOptions = imeOptions,
                        onKeyClick = { handleKeyClick(it) },
                        onBackspaceClick = { handleBackspace() },
                        onAbcToggle = { keyboardMode = KeyboardMode.ALPHABET },
                        onActionClick = { handleActionClick() }
                    )
                }

                KeyboardMode.ALPHABET -> {
                    QwertyKeyboard(
                        shiftState = shiftState,
                        showNumberRow = showNumberRow,
                        hapticsEnabled = hapticsEnabled,
                        soundEnabled = soundEnabled,
                        imeOptions = imeOptions,
                        onKeyClick = { handleKeyClick(it) },
                        onBackspaceClick = { handleBackspace() },
                        onShiftClick = {
                            shiftState = when (shiftState) {
                                ShiftState.OFF -> ShiftState.SHIFT
                                ShiftState.SHIFT -> ShiftState.CAPS_LOCK
                                ShiftState.CAPS_LOCK -> ShiftState.OFF
                            }
                        },
                        onSymbolToggle = { keyboardMode = KeyboardMode.SYMBOLS },
                        onActionClick = { handleActionClick() }
                    )
                }

                KeyboardMode.SYMBOLS -> {
                    SymbolKeyboard(
                        hapticsEnabled = hapticsEnabled,
                        soundEnabled = soundEnabled,
                        imeOptions = imeOptions,
                        onKeyClick = { handleKeyClick(it) },
                        onBackspaceClick = { handleBackspace() },
                        onAbcToggle = { keyboardMode = KeyboardMode.ALPHABET },
                        onActionClick = { handleActionClick() }
                    )
                }

                KeyboardMode.EMOJI -> {
                    EmojiKeyboard(
                        hapticsEnabled = hapticsEnabled,
                        soundEnabled = soundEnabled,
                        onEmojiClick = { handleKeyClick(it) },
                        onBackspaceClick = { handleBackspace() },
                        onAbcToggle = { keyboardMode = KeyboardMode.ALPHABET }
                    )
                }
            }
        }
    }
}
