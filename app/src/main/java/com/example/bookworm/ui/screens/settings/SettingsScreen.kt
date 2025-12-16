package com.example.bookworm.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookworm.data.models.Theme
import com.example.bookworm.ui.composables.AppBar
import com.example.bookworm.ui.composables.NavBottom

@Composable
fun SettingsScreen(
    navController: NavController,
    state: ThemeState,
    onThemeSelected: (Theme) -> Unit
) {
    Scaffold(
        topBar = { AppBar(navController, true) },
        bottomBar = { NavBottom(navController) }
    ) { contentPadding ->
        Column ( Modifier
            .padding(contentPadding)
            .padding(8.dp)
            .padding(bottom = 15.dp)) {
            Text(
                "Theme selection",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
            Column(
                Modifier
                    .selectableGroup()
            ) {
                Theme.entries.forEach { theme ->
                    ListItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .selectable(
                                selected = (theme == state.theme),
                                onClick = { onThemeSelected(theme) },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 16.dp),
                        headlineContent = {
                            Text(
                                text = theme.toString(),
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        },

                        leadingContent = {
                            RadioButton(
                                selected = (theme == state.theme),
                                onClick = null
                            )
                        },
                    )
                }
            }

        }

    }
}