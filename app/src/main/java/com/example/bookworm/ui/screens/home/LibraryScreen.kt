package com.example.bookworm.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.bookworm.R
import com.example.bookworm.core.data.database.entities.BookEntity
import com.example.bookworm.core.data.models.ReadingStatus
import com.example.bookworm.ui.composables.AddBookFloatingButton
import com.example.bookworm.ui.composables.AppBar
import com.example.bookworm.ui.composables.BookItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    modifier: Modifier,
    navController: NavController,
    onBookClick: (Long) -> Unit,
    state: LibraryState,
    books: State<List<BookEntity>>,
    actions: LibraryActions,
) {

    Scaffold(
        floatingActionButton = { AddBookFloatingButton(navController) },
        topBar = { AppBar(navController) },
        contentWindowInsets = WindowInsets()
    ) { contentPadding ->

        if (state.openFilters) {
            FiltersSelection(actions, state)
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(horizontal = 16.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .semantics { isTraversalGroup = true },
                verticalAlignment = Alignment.CenterVertically
            ) {

                DockedSearchBar(
                    modifier = Modifier.weight(1f),
                    expanded = false,
                    onExpandedChange = {},
                    inputField = {
                        SearchBarDefaults.InputField(
                            query = state.query,
                            onQueryChange = { actions.setQuery(it) },
                            placeholder = { Text(stringResource(R.string.search_message)) },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.Search,
                                    contentDescription = null
                                )
                            },
                            trailingIcon = {
                                if (state.query.isNotEmpty()) {
                                    IconButton(onClick = { actions.setQuery("") }) {
                                        Icon(
                                            Icons.Outlined.Close,
                                            contentDescription = stringResource(R.string.clear_input_icon_desc)
                                        )
                                    }
                                }
                            },
                            expanded = false,
                            onExpandedChange = {},
                            onSearch = { actions.setQuery(it) },
                        )
                    },
                    content = {}
                )

                BadgedBox(
                    badge = {
                        if (state.countFiltersActive > 0) {
                            Badge {
                                Text(text = state.countFiltersActive.toString())
                            }
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    IconButton(
                        onClick = { actions.openFilters(true) },
                        modifier = Modifier
                            .size(40.dp)
                    ) {
                        Icon(
                            Icons.Filled.FilterList,
                            contentDescription = stringResource(R.string.filters_icon_desc)
                        )
                    }
                }
            }

            if (books.value.isNotEmpty()) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(top = 16.dp)
                ) {
                    items(books.value) { book ->
                        BookItem(book, onBookClick)
                    }
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Book,
                        contentDescription = stringResource(R.string.book_icon_desc),
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .size(64.dp)
                    )
                    Text(
                        text = stringResource(R.string.library_empty_message),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth(),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}


@Composable
fun FiltersSelection(actions: LibraryActions, state: LibraryState) {
    Dialog(
        onDismissRequest = { actions.openFilters(false) }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 200.dp, max = 400.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.filters_selection_message),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Start
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        stringResource(R.string.favourites_option)
                    )
                    Checkbox(
                        checked = state.favouritesOnly,
                        onCheckedChange = {
                            actions.filterByFavourites(!state.favouritesOnly)
                            actions.openFilters(false)
                        },
                        modifier = Modifier.padding(start = 20.dp)
                    )
                }


                ReadingStatus.entries.forEach { status ->

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            status.name.replace('_', ' ')
                                .lowercase()
                                .replaceFirstChar { it.titlecase() }
                        )
                        Checkbox(
                            checked = (state.selectedStatus == status),
                            onCheckedChange = { isChecked ->
                                if (isChecked) {
                                    actions.filterByStatus(status)
                                } else {
                                    actions.filterByStatus(null)
                                }
                                actions.openFilters(false)
                            }
                        )
                    }
                }
            }
        }
    }
}
