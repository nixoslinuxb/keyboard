package com.keyboardd.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class KeyboardThemeOption {
    SYSTEM,
    LIGHT,
    DARK,
    GBOARD_BLUE,
    PITCH_BLACK
}

enum class KeyboardLanguage(val displayName: String) {
    TURKISH("Türkçe (Q)")
}

class KeyboardSettings private constructor(context: Context) {

    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences("keyboard_settings", Context.MODE_PRIVATE)

    private val _hapticsEnabled = MutableStateFlow(prefs.getBoolean(KEY_HAPTICS, true))
    val hapticsEnabled: StateFlow<Boolean> = _hapticsEnabled.asStateFlow()

    private val _soundEnabled = MutableStateFlow(prefs.getBoolean(KEY_SOUND, false))
    val soundEnabled: StateFlow<Boolean> = _soundEnabled.asStateFlow()

    private val _showNumberRow = MutableStateFlow(prefs.getBoolean(KEY_NUMBER_ROW, true))
    val showNumberRow: StateFlow<Boolean> = _showNumberRow.asStateFlow()

    private val _autoSuggestionsEnabled = MutableStateFlow(prefs.getBoolean(KEY_SUGGESTIONS, true))
    val autoSuggestionsEnabled: StateFlow<Boolean> = _autoSuggestionsEnabled.asStateFlow()

    private val _selectedTheme = MutableStateFlow(
        runCatching {
            KeyboardThemeOption.valueOf(prefs.getString(KEY_THEME, KeyboardThemeOption.GBOARD_BLUE.name)!!)
        }.getOrDefault(KeyboardThemeOption.GBOARD_BLUE)
    )
    val selectedTheme: StateFlow<KeyboardThemeOption> = _selectedTheme.asStateFlow()

    private val _selectedLanguage = MutableStateFlow(KeyboardLanguage.TURKISH)
    val selectedLanguage: StateFlow<KeyboardLanguage> = _selectedLanguage.asStateFlow()

    fun setHapticsEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_HAPTICS, enabled).apply()
        _hapticsEnabled.value = enabled
    }

    fun setSoundEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_SOUND, enabled).apply()
        _soundEnabled.value = enabled
    }

    fun setShowNumberRow(show: Boolean) {
        prefs.edit().putBoolean(KEY_NUMBER_ROW, show).apply()
        _showNumberRow.value = show
    }

    fun setAutoSuggestionsEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_SUGGESTIONS, enabled).apply()
        _autoSuggestionsEnabled.value = enabled
    }

    fun setTheme(theme: KeyboardThemeOption) {
        prefs.edit().putString(KEY_THEME, theme.name).apply()
        _selectedTheme.value = theme
    }

    companion object {
        private const val KEY_HAPTICS = "haptics_enabled"
        private const val KEY_SOUND = "sound_enabled"
        private const val KEY_NUMBER_ROW = "show_number_row"
        private const val KEY_SUGGESTIONS = "auto_suggestions_enabled"
        private const val KEY_THEME = "keyboard_theme"

        @Volatile
        private var instance: KeyboardSettings? = null

        fun getInstance(context: Context): KeyboardSettings {
            return instance ?: synchronized(this) {
                instance ?: KeyboardSettings(context).also { instance = it }
            }
        }
    }
}
