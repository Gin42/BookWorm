package com.example.bookworm.ui.screens.bookdetails

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
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
        }
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
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 15.dp),
                    thickness = 2.dp
                )
            }

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
                            IconButton(onClick = actions::updateFavourite) {
                                Icon(
                                    imageVector =
                                    if (state.selectedBook.favourite)
                                        Icons.Filled.Favorite
                                    else Icons.Outlined.FavoriteBorder,
                                    contentDescription = "Favorite"
                                )
                            }
                            IconButton(
                                onClick = {
                                    navController.navigate(
                                        BookWormRoute.AddBook(state.selectedBook.bookId)
                                    )
                                }
                            ) {
                                Icon(Icons.Outlined.Edit, contentDescription = "Edit")
                            }
                        }
                    }
                )
            }

            item { StatusSelection(state, actions) }

            item {

                val totalPages = state.selectedBook.pages

                Log.println(
                    Log.DEBUG,
                    TAG,
                    "JOURNEY: ${state.bookJourneys.firstOrNull()}\n" +
                            "ENTRY: ${state.bookJourneys
                                .firstOrNull()  
                                ?.entries
                                ?.lastOrNull()}"
                )

                val lastPagesRead = state.bookJourneys
                    .firstOrNull()
                    ?.entries
                    ?.lastOrNull()
                    ?.pagesRead ?: 0

                val progressFraction =
                    if (totalPages > 0) lastPagesRead.toFloat() / totalPages else 0f

                val progressPercent = (progressFraction * 100).toInt()

                ListItem(
                    headlineContent = {
                        LinearProgressIndicator(
                            progress = { progressFraction.coerceIn(0f, 1f) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    leadingContent = {
                        Text("Progress:", style = MaterialTheme.typography.labelLarge)
                    },
                    trailingContent = {
                        Text("$progressPercent%", style = MaterialTheme.typography.labelLarge)
                    }
                )
            }

            if (state.bookJourneys.isNotEmpty()) {
                items(state.bookJourneys.size) { index ->
                    BookJourney(index, state, actions)
                }
            } else {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Outlined.Book, null, modifier = Modifier.size(64.dp))
                        Text(
                            "It seems like you haven't started this journey yet.\nStart reading.",
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
fun BookJourney(
    journeyIndex: Int,
    state: BookDetailsState,
    actions: BookDetailsAction
) {
    val journey = state.bookJourneys[journeyIndex]
    val expanded = state.journeyExpanded.getOrNull(journeyIndex) == true

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .clickable { actions.toggleJourneyEntry(journeyIndex) }
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Column {
            ListItem(
                headlineContent = {
                    Text(
                        "${journey.startDate.toFormattedDate()} - " +
                                (journey.endDate?.toFormattedDate() ?: "Current")
                    )
                },
                supportingContent = {
                    Text("Number of entries: ${journey.entries.size}")
                },
                trailingContent = {
                    Icon(
                        imageVector =
                        if (expanded) Icons.Filled.ArrowDropUp
                        else Icons.Filled.ArrowDropDown,
                        contentDescription = null
                    )
                },
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )

            AnimatedVisibility(visible = expanded) {
                Column {
                    journey.entries.forEachIndexed { index, entry ->
                        DiaryEntry(entry, state, actions)
                        if (index < journey.entries.lastIndex) {
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
fun DiaryEntry(
    entry: Entry,
    state: BookDetailsState,
    actions: BookDetailsAction
) {
    val expanded = state.entryExpanded[entry.entryId] == true

    Box(modifier = Modifier.fillMaxWidth()) {
        ListItem(
            modifier = Modifier.clickable {
                actions.openEntry(entry.entryId, true)
            },
            headlineContent = {
                Text(entry.date.toFormattedDate())
            },
            supportingContent = {
                Text(
                    entry.comment.orEmpty(),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            },
            colors = ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            )
        )

        if (expanded) {
            ModalBottomSheet(
                modifier = Modifier.fillMaxHeight(),
                sheetState = rememberModalBottomSheetState(),
                onDismissRequest = {
                    actions.openEntry(entry.entryId, false)
                }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        entry.date.toFormattedDate(),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text("Pages read: ${entry.pagesRead}")
                    Text(entry.comment.orEmpty())
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