package com.example.bookworm.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.bookworm.core.data.database.entities.BookEntity
import com.example.bookworm.core.data.models.ReadingStatus
import com.example.bookworm.ui.composables.AddBookFloatingButton
import com.example.bookworm.ui.composables.AppBar
import com.example.bookworm.ui.composables.BookItem
import com.example.bookworm.ui.composables.NavBottom


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    navController: NavController,
    state: LibraryState,
    books: State<List<BookEntity>>,
    actions: LibraryActions,
) {

    Scaffold(
        floatingActionButton = { AddBookFloatingButton(navController) },
        topBar = { AppBar(navController) },
        bottomBar = { NavBottom(navController) },
    ) { contentPadding ->

        if (state.openFilters) {
            FiltersSelection(actions, state)
        }


        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
                .padding(8.dp)
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
                            placeholder = { Text("Search") },
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
                                            contentDescription = "Clear input"
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

                IconButton(
                    onClick = { actions.openFilters(true) },
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    Icon(Icons.Filled.FilterList, contentDescription = "Filters")
                }
            }

            if (books.value.isNotEmpty()) {
                BookList(navController, books.value)
            } else {

                Icon(
                    imageVector = Icons.Outlined.Book,
                    contentDescription = "Book icon",
                    modifier = Modifier
                        .size(64.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
                )
                Text(
                    text = "There seems to be a shortage of books.\nAdd some.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                )

            }
        }
    }
}


@Composable
fun BookList(
    navController: NavController,
    books: List<BookEntity>
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxHeight()
            .padding(top = 16.dp)
    ) {
        items(books) { book ->
            BookItem(book, navController)
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
                    text = "Filter selection",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Start
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        "Favourites"
                    )
                    Checkbox(
                        checked = state.favouritesOnly,
                        onCheckedChange = {
                            actions.filterByFavourites(!state.favouritesOnly)
                            actions.openFilters(false)
                        }
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
