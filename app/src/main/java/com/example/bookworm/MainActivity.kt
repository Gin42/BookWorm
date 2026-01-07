package com.example.bookworm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.bookworm.ui.BookWormNavGraph
import com.example.bookworm.ui.screens.settings.ThemeViewModel
import com.example.bookworm.ui.theme.BookWormTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeViewModel = koinViewModel<ThemeViewModel>()
            val themeState by themeViewModel.state.collectAsStateWithLifecycle()


            BookWormTheme(
                darkTheme = when (themeState.theme) {
                    com.example.bookworm.core.data.models.Theme.Light -> false
                    com.example.bookworm.core.data.models.Theme.Dark -> true
                    com.example.bookworm.core.data.models.Theme.System -> isSystemInDarkTheme()
                }
            ) {
                val navController = rememberNavController()
                BookWormNavGraph(navController)
            }
        }
    }
}