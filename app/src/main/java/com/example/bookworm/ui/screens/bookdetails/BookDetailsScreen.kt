package com.example.bookworm.ui.screens.bookdetails

import android.net.Uri
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookworm.R
import com.example.bookworm.core.data.models.ReadingStatus
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
    onNavigateToAddBook: () -> Unit
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
                        desc = stringResource(R.string.book_cover_desc),
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
                                    contentDescription = stringResource(R.string.favourite_icon_desc)
                                )
                            }
                            IconButton(
                                onClick = {
                                    onNavigateToAddBook()
                                }
                            ) {
                                Icon(
                                    Icons.Outlined.Edit,
                                    contentDescription = stringResource(R.string.modify_icon_desc)
                                )
                            }
                        }
                    }
                )
            }

            item { StatusSelection(state, actions) }

            item {

                val totalPages = state.selectedBook.pages

                val bookStatus = state.selectedBook.status

                var lastPagesRead = state.bookJourneys
                    .firstOrNull()
                    ?.entries
                    ?.lastOrNull()
                    ?.pagesRead ?: 0

                when (bookStatus) {
                    ReadingStatus.DROPPED, ReadingStatus.PLAN_TO_READ -> {
                        actions.showProgress(false)
                    }
                    ReadingStatus.FINISHED -> {
                        actions.showProgress(true)
                        lastPagesRead = state.selectedBook.pages
                    }
                    ReadingStatus.READING -> {
                        actions.showProgress(true)
                    }
                }

                val progressFraction =
                    if (totalPages > 0) lastPagesRead.toFloat() / totalPages else 0f

                val progressPercent = (progressFraction * 100).toInt()

                if (state.showProgress) {
                    ListItem(
                        headlineContent = {
                            LinearProgressIndicator(
                                progress = { progressFraction.coerceIn(0f, 1f) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        leadingContent = {
                            Text(
                                stringResource(R.string.progress_message),
                                style = MaterialTheme.typography.labelLarge
                            )
                        },
                        /*TODO*/
                        trailingContent = {
                            Text("$progressPercent%", style = MaterialTheme.typography.labelLarge)
                        }
                    )
                }
            }

            if (state.bookJourneys.isNotEmpty()) {
                items(state.bookJourneys.size) { index ->
                    BookJourney(index, state, actions)
                }
            } else {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Outlined.Book,
                            null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            stringResource(R.string.journey_empty_message),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onBackground
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

    val verticalPadding = if (!state.showProgress) {
        32.dp
    } else {
        0.dp
    }

    ListItem(
        modifier = Modifier
            .border(1.dp, Color.Transparent)
            .clip(MaterialTheme.shapes.medium)
            .clickable { actions.toggleStatusExpanded(!state.statusExpanded) }
            .padding(vertical = verticalPadding),
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
                    contentDescription = if (state.statusExpanded) {
                        stringResource(R.string.minimize_options_icon_desc)
                    } else {
                        stringResource(R.string.expand_options_icon_desc)
                    }
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
                                (journey.endDate?.toFormattedDate()
                                    ?: stringResource(R.string.current_message))
                    )
                },
                supportingContent = {
                    Text(stringResource(R.string.number_entries_message) + journey.entries.size)
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
                    Text(
                        stringResource(R.string.pages_read_message) + entry.pagesRead
                    )
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
