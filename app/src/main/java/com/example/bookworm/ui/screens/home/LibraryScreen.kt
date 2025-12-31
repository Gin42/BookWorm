package com.example.bookworm.ui.screens.home

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.bookworm.ui.composables.AddBookFloatingButton
import com.example.bookworm.ui.composables.AppBar
import com.example.bookworm.ui.composables.BookList
import com.example.bookworm.ui.composables.Library
import com.example.bookworm.ui.composables.NavBottom
import com.example.bookworm.ui.entitiesViewModel.BookState
import com.example.bookworm.ui.entitiesViewModel.LoggedUserState


@Composable
fun LibraryScreen(navController: NavController, bookState: BookState, userState: LoggedUserState) {
    Scaffold(
        floatingActionButton = { AddBookFloatingButton(navController) },
        topBar = { AppBar(navController) },
        bottomBar = { NavBottom(navController) },
    ) { contentPadding ->
        Library(contentPadding, navController, bookState, userState)
    }
}