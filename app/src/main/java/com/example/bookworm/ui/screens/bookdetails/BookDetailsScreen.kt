package com.example.bookworm.ui.screens.bookdetails

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookworm.ui.composables.AddDiaryFloatingButton
import com.example.bookworm.ui.composables.AppBar


@Composable
fun BookDetailsScreen(
    navController: NavController,
    bookId: String,
    state: BookDetailsState,
    actions: BookDetailsAction
) {
    Scaffold(
        topBar = { AppBar(navController = navController, goBack = true) },
        floatingActionButton = { AddDiaryFloatingButton(navController) },
    ) { contentPadding ->

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.padding(contentPadding)
        ) {
            item {
                // Image
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .height(400.dp)
                        .width(250.dp)
                        .border(2.dp, Color.Gray, RoundedCornerShape(20.dp)), /*DELETE*/
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        imageVector = Icons.Outlined.Book,
                        contentDescription = "Book cover",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
            }

            item {
                // Divider
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 15.dp),
                    thickness = 2.dp
                )
            }

            //book main info
            item {
                ListItem(
                    headlineContent = {
                        Text(
                            "Book Title",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    supportingContent = {
                        Text(
                            "Author Name",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    trailingContent = {
                        Row {
                            IconButton(
                                onClick = { /* TODO */ }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.FavoriteBorder,
                                    contentDescription = "Favorite Icon",
                                )
                            }
                            IconButton(
                                onClick = { /* TODO */ }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Edit,
                                    contentDescription = "Edit Icon",
                                )
                            }
                        }
                    },
                )
            }

            //book status
            item {
                StatusSelection(state, actions)
            }

            //progress bar
            item {
                ListItem(
                    headlineContent = {
                        LinearProgressIndicator(
                            progress = { 0.8f },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    },
                    leadingContent = {
                        Text(
                            "Progress:",
                            style = MaterialTheme.typography.labelLarge,
                        )
                    },
                    trailingContent = {
                        Text(
                            "80%",
                            style = MaterialTheme.typography.labelLarge,
                        )
                    },
                )
            }

            //book journey
            items(NUMBER_OF_ENTRIES) { index ->
                BookJourney(index, state, actions)
            }
        }
    }
}

@Composable
fun StatusSelection(state: BookDetailsState, actions: BookDetailsAction) {
    ListItem(
        modifier = Modifier
            .border(1.dp, Color.Transparent)
            .clip(MaterialTheme.shapes.medium),
        headlineContent = {
            Text(
                state.readingStatus.name.replace('_', ' ').lowercase()
                    .replaceFirstChar { it.titlecase() },
                style = MaterialTheme.typography.titleMedium
            )
        },
        trailingContent = {
            IconButton(
                onClick = { actions.toggleStatusExpanded(!state.statusExpanded) },
            ) {
                Icon(
                    imageVector = if (state.statusExpanded) {
                        Icons.Filled.ArrowDropUp
                    } else {
                        Icons.Filled.ArrowDropDown
                    },
                    contentDescription = "Status options"
                )
            }
            DropdownMenu(
                expanded = state.statusExpanded,
                onDismissRequest = { actions.toggleStatusExpanded(false) }
            ) {
                ReadingStatus.entries.forEach { status ->
                    DropdownMenuItem(
                        onClick = {
                            actions.setReadingStatus(status)
                            actions.toggleStatusExpanded(false)
                        },
                        text = {
                            Text(
                                status.name.replace('_', ' ').lowercase()
                                    .replaceFirstChar { it.titlecase() })
                        },
                    )
                }
            }
        },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    )
}

@Composable
fun BookJourney(index: Int, state: BookDetailsState, actions: BookDetailsAction) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .clickable { actions.toggleJourneyEntry(index) }
            .border(1.dp, Color.Transparent)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceContainer),
    ) {
        Column {
            ListItem(
                headlineContent = {
                    Text(
                        "dd/mm/aaaa - dd/mm/aaaa",
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                supportingContent = {
                    Text("N. entries: ##")
                },
                trailingContent = {
                    IconButton(
                        onClick = { actions.toggleJourneyEntry(index) },
                    ) {
                        Icon(
                            imageVector = if (state.journeyExpanded[index]) {
                                Icons.Filled.ArrowDropUp
                            } else {
                                Icons.Filled.ArrowDropDown
                            },
                            contentDescription = "Status options"
                        )
                    }
                },
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )

            AnimatedVisibility(visible = state.journeyExpanded[index]) {
                Column {
                    repeat(NUMBER_OF_ENTRIES) { entryIndex ->
                        DiaryEntry(entryIndex, state, actions)
                        if (entryIndex < NUMBER_OF_ENTRIES - 1) {
                            HorizontalDivider(thickness = 2.dp)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryEntry(index: Int, state: BookDetailsState, actions: BookDetailsAction) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        ListItem(
            modifier = Modifier
                .clickable {
                    actions.openEntry(index, true)
                },
            headlineContent = {
                Text(
                    "gg/mm/aaaa",
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            supportingContent = {
                Text(
                    "Sit non officiis aliquam ipsa corporis quidem a id. Iste enim quidem animi aliquam eius. Error porro et suscipit quasi nostrum.",
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            colors = ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            )
        )

        if (state.entryExpanded[index]) {
            ModalBottomSheet(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(),
                sheetState = rememberModalBottomSheetState(
                    skipPartiallyExpanded = false,
                ),
                onDismissRequest = { actions.openEntry(index, false) }
            ) {
                Column(modifier = Modifier.padding(15.dp)) {
                    Text(
                        "gg/mm/aaaa",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(8.dp)
                    )
                    Text(
                        "Pages read: 250",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Text(
                        "Sit non officiis aliquam ipsa corporis quidem a id. Iste enim quidem animi aliquam eius. Error porro et suscipit quasi nostrum.",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}
