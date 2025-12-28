package com.example.bookworm.ui.screens.settings


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookworm.core.data.repositories.ThemeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ThemeState(
    val theme: com.example.bookworm.core.data.models.Theme,
)

data class SettingState(
    val themeExpanded: Boolean = false
)

interface SettingsAction {
    fun toggleThemeExpanded(themeExpanded: Boolean)
}


class ThemeViewModel(
    private val repository: ThemeRepository
) : ViewModel() {

    private val _settingState = MutableStateFlow(SettingState())
    val settingState = _settingState.asStateFlow()

    val state = repository.theme.map { ThemeState(it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ThemeState(com.example.bookworm.core.data.models.Theme.System)
    )

    fun changeTheme(theme: com.example.bookworm.core.data.models.Theme) = viewModelScope.launch {
        repository.setTheme(theme)
    }

    val actions = object : SettingsAction {
        override fun toggleThemeExpanded(themeExpanded: Boolean) {
            _settingState.update { it.copy(themeExpanded = themeExpanded) }

        }
    }
}

