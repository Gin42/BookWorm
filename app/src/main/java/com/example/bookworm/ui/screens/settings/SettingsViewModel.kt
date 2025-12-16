package com.example.bookworm.ui.screens.settings


import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SettingsState(
    val isDarkTheme: Boolean = false
)

interface SettingsActions {
    fun setDarkTheme(isDarkTheme: Boolean)
}

class SettingsViewModel : ViewModel() {
    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()
    val actions = object : SettingsActions {
        override fun setDarkTheme(isDarkTheme: Boolean) {
            _state.value = SettingsState(isDarkTheme)
        }

    }
}
