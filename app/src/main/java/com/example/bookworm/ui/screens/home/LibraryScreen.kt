package com.example.bookworm.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookworm.ui.composables.AddBookFloatingButton
import com.example.bookworm.ui.composables.AppBar
import com.example.bookworm.ui.composables.BookItem
import com.example.bookworm.ui.composables.NavBottom
import com.example.bookworm.ui.entitiesViewModel.BookState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    navController: NavController,
    bookState: BookState,
    state: LibraryState,
    actions: LibraryActions,
) {
    Scaffold(
        floatingActionButton = { AddBookFloatingButton(navController) },
        topBar = { AppBar(navController) },
        bottomBar = { NavBottom(navController) },
    ) { contentPadding ->

        Column( /*TODO HELP ALE*/
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
                    onClick = { /* TODO: filters */ },
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    Icon(Icons.Filled.FilterList, contentDescription = "Filters")
                }
            }

            if (bookState.books.isNotEmpty()) {
                BookList(contentPadding, navController, state)
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Book,
                        contentDescription = "Book icon",
                        modifier = Modifier.size(64.dp)
                    )
                    Text(
                        text = "There seems to be a shortage of books.\nAdd some.",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}


@Composable
fun BookList(
    contentPadding: PaddingValues,
    navController: NavController,
    libraryState: LibraryState
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp),
        modifier = Modifier.padding(contentPadding)
    ) {
        items(libraryState.filteredBooks) { book ->
            BookItem(book, navController)
        }
    }
}
