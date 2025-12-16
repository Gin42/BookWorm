package com.example.bookworm.ui.screens.home

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.bookworm.ui.composables.AddBookFloatingButton
import com.example.bookworm.ui.composables.AppBar
import com.example.bookworm.ui.composables.BookList
import com.example.bookworm.ui.composables.NavBottom


@Composable
fun LibraryScreen(navController : NavController) {
    Scaffold(
        floatingActionButton = { AddBookFloatingButton(navController) },
        topBar = { AppBar(navController) },
        bottomBar = {NavBottom(navController)},
    ) { contentPadding ->
        /*
    SearchBar() {}
    */
        BookList(contentPadding, navController)
    }
}