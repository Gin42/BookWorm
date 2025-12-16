package com.example.bookworm.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookworm.ui.composables.AppBar
import com.example.bookworm.ui.composables.NavBottom
import com.example.bookworm.ui.screens.settings.SettingsActions
import com.example.bookworm.ui.screens.settings.SettingsState

@Composable
fun SettingsScreen(
    navController: NavController,
    state: SettingsState,
    actions: SettingsActions,
) {
    Scaffold(
        topBar = { AppBar(navController, true) },
        bottomBar = { NavBottom(navController) }
    ) { contentPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(contentPadding)
                .padding(12.dp)
                .fillMaxSize()
        ) {
            ListItem(
                modifier = Modifier
                    .fillMaxWidth(),
                headlineContent =
                {
                    Text(
                        text = "Dark Mode",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Outlined.DarkMode,
                        contentDescription = "Favorite Icon",
                    )
                },
                trailingContent = {
                    Switch(
                        checked = state.isDarkTheme,
                        onCheckedChange = { (actions::setDarkTheme)(!state.isDarkTheme) }
                    )
                },

                )
        }
    }
}