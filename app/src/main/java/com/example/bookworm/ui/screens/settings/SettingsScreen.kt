package com.example.bookworm.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookworm.R
import com.example.bookworm.core.data.models.Theme
import com.example.bookworm.ui.composables.AppBar

@Composable
fun SettingsScreen(
    navController: NavController,
    state: ThemeState,
    settingsState: SettingsState,
    actions: SettingsAction,
    onThemeSelected: (Theme) -> Unit
) {
    Scaffold(
        topBar = { AppBar(navController, true) },
    ) { contentPadding ->
        Column(
            Modifier
                .padding(contentPadding)
                .padding(8.dp)
                .padding(bottom = 15.dp)
        ) {
            ListItem(
                headlineContent = {
                    Text(
                        stringResource(R.string.theme_selection_message),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                },
                trailingContent = {
                    Row(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.surfaceContainer)
                            .clickable { actions.toggleThemeExpanded(!settingsState.themeExpanded) }
                            .padding(8.dp)
                            .width(100.dp),
                        horizontalArrangement = Arrangement.SpaceBetween

                    ) {
                        Text(
                            state.theme.toString(),
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(start = 12.dp),
                            style = MaterialTheme.typography.labelLarge
                        )
                        Icon(
                            imageVector = if (settingsState.themeExpanded) {
                                Icons.Filled.ArrowDropUp
                            } else {
                                Icons.Filled.ArrowDropDown
                            },
                            contentDescription = if (settingsState.themeExpanded) {
                                stringResource(R.string.minimize_options_icon_desc)
                            } else {
                                stringResource(R.string.expand_options_icon_desc)

                            }
                        )
                    }
                    DropdownMenu(
                        expanded = settingsState.themeExpanded,
                        onDismissRequest = { actions.toggleThemeExpanded(false) }
                    ) {
                        com.example.bookworm.core.data.models.Theme.entries.forEach { theme ->
                            DropdownMenuItem(
                                onClick = {
                                    onThemeSelected(theme)
                                    actions.toggleThemeExpanded(false)
                                },
                                text = {
                                    Text(
                                        theme.toString(),
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                },
                            )
                        }
                    }

                }
            )
        }
    }
}