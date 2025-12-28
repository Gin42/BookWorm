package com.example.bookworm.core.data.repositories

import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.bookworm.core.data.models.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ThemeRepository(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val THEME_KEY = stringPreferencesKey("theme")
    }

    val theme = dataStore.data
        .map { preferences ->
            try {
                com.example.bookworm.core.data.models.Theme.valueOf(preferences[THEME_KEY] ?: "System")
            } catch (_: Exception) {
                com.example.bookworm.core.data.models.Theme.System
            }
        }

    suspend fun setTheme(theme: com.example.bookworm.core.data.models.Theme) = dataStore.edit { preferences ->
        preferences[THEME_KEY] = theme.toString()
    }
}
