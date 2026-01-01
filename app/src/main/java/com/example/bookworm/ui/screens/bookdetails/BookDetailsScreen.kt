package com.example.bookworm.ui.screens.bookdetails

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.FavoriteBorder
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withAnnotation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookworm.R
import com.example.bookworm.core.data.models.ReadingStatus
import com.example.bookworm.ui.BookWormRoute
import com.example.bookworm.ui.composables.AddDiaryFloatingButton
import com.example.bookworm.ui.composables.AppBar
import com.example.bookworm.ui.composables.ImageWithPlaceholder
import com.example.bookworm.ui.composables.Size
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@Composable
fun BookDetailsScreen(
    navController: NavController,
    state: BookDetailsState,
    actions: BookDetailsAction,
) {

    Scaffold(
        topBar = { AppBar(navController = navController, goBack = true) },
        floatingActionButton = {
            AddDiaryFloatingButton(
                navController,
                state.selectedBook.bookId
            )
        },
        /*TODO*/
    ) { contentPadding ->

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.padding(contentPadding)
        ) {
            item {
                state.selectedBook.image?.let {
                    ImageWithPlaceholder(
                        uri = Uri.parse(it),
                        size = Size.BookDetail,
                        desc = "Book cover",
                        shape = RoundedCornerShape(16.dp)
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

            // Book main info
            item {
                ListItem(
                    headlineContent = {
                        Text(
                            state.selectedBook.title,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    supportingContent = {
                        Text(
                            state.selectedBook.author,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    trailingContent = {
                        Row {
                            IconButton(onClick = { actions.updateFavourite() }) {
                                Icon(
                                    imageVector = if (state.selectedBook.favourite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                    contentDescription = "Favorite Icon"
                                )
                            }
                            IconButton(onClick = {
                                navController.navigate(BookWormRoute.AddBook(state.selectedBook.bookId))
                            }) {
                                Icon(
                                    imageVector = Icons.Outlined.Edit,
                                    contentDescription = "Edit Icon"
                                )
                            }
                        }
                    }
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
            if (state.bookJourneys.isNotEmpty()) {
                items(state.bookJourneys.size) { journeyIndex ->
                    BookJourney(journeyIndex, state, actions)
                }
            } else {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Book,
                            contentDescription = "Book icon",
                            modifier = Modifier.size(64.dp)
                        )
                        Text(
                            text = "It seems like you haven't started this journey yet.\nStart reading.",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatusSelection(
    state: BookDetailsState,
    actions: BookDetailsAction
) {
    ListItem(
        modifier = Modifier
            .border(1.dp, Color.Transparent)
            .clip(MaterialTheme.shapes.medium)
            .clickable { actions.toggleStatusExpanded(!state.statusExpanded) },
        headlineContent = {
            Text(
                state.selectedBook.status.name.replace('_', ' ').lowercase()
                    .replaceFirstChar { it.titlecase() },
                style = MaterialTheme.typography.titleMedium
            )
        },
        trailingContent = {
            IconButton(
                onClick = {
                    actions.toggleStatusExpanded(!state.statusExpanded)
                },
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
                            actions.updateReadingStatus(status)
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
fun BookJourney(journeyIndex: Int, state: BookDetailsState, actions: BookDetailsAction) {

    val journey = state.bookJourneys[journeyIndex]

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .clickable { actions.toggleJourneyEntry(journeyIndex) }
            .border(1.dp, Color.Transparent)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceContainer),
    ) {
        Column {
            ListItem(
                headlineContent = {
                    Text(
                        text = "${journey.startDate.toFormattedDate()} - ${
                            journey.endDate?.toFormattedDate() ?: "Current"
                        }",
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                supportingContent = {
                    Text("Number of entries: ${journey.entries.size}")
                },
                trailingContent = {
                    IconButton(
                        onClick = { actions.toggleJourneyEntry(journeyIndex) },
                    ) {
                        Icon(
                            imageVector = if (state.journeyExpanded[journeyIndex]) {
                                Icons.Filled.ArrowDropUp
                            } else {
                                Icons.Filled.ArrowDropDown
                            },
                            contentDescription = "Status options" /*TODO*/
                        )
                    }
                },
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )

            AnimatedVisibility(visible = state.journeyExpanded[journeyIndex]) {
                Column {

                    repeat(state.bookJourneys[journeyIndex].entries.size) { entryIndex ->
                        DiaryEntry(journeyIndex, entryIndex, state, actions)
                        if (entryIndex < state.bookJourneys[journeyIndex].entries.size - 1) {
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
fun DiaryEntry(journeyIndex: Int, entryIndex: Int, state: BookDetailsState, actions: BookDetailsAction) {

    val journey = state.bookJourneys[journeyIndex]
    val entry = journey.entries[entryIndex]

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        ListItem(
            modifier = Modifier
                .clickable {
                    actions.openEntry(entryIndex, true)
                },
            headlineContent = {
                Text(
                    entry.date.toFormattedDate(),
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            supportingContent = {
                Text(
                    entry.comment ?: "",
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            colors = ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            )
        )

        if (state.entryExpanded[entryIndex]) {
            ModalBottomSheet(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(),
                sheetState = rememberModalBottomSheetState(
                    skipPartiallyExpanded = false,
                ),
                onDismissRequest = { actions.openEntry(entryIndex, false) }
            ) {
                Column(modifier = Modifier.padding(horizontal = 15.dp)) {
                    Text(
                        entry.date.toFormattedDate(),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Text(
                        "Pages read: ${entry.pagesRead.toString()}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        entry.comment ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}


private val dateFormatter = DateTimeFormatter
    .ofPattern("dd MMM yyyy") // e.g. 04 Jan 2026
    .withZone(ZoneId.systemDefault())

fun Long.toFormattedDate(): String {
    return dateFormatter.format(Instant.ofEpochMilli(this))
}