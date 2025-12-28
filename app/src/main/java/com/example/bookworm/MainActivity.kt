package com.example.bookworm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.bookworm.core.data.models.Theme
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