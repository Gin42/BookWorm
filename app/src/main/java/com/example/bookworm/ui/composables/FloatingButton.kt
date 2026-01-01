package com.example.bookworm.ui.composables

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.AddComment
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.bookworm.ui.BookWormRoute


@Composable
fun AddBookFloatingButton(navController: NavController) {
    FloatingActionButton(
        onClick = { navController.navigate(BookWormRoute.AddBook (null)) },
        shape = CircleShape,
    ) {
        Icon(Icons.Filled.Add, "Add item")
    }
}

@Composable
fun AddDiaryFloatingButton(navController: NavController, bookId: Long) {
    FloatingActionButton(
        onClick = { navController.navigate(BookWormRoute.AddDiaryEntry(bookId)) },
        shape = CircleShape,
    ) {
        Icon(Icons.Outlined.AddComment, "Add diary entry")
    }
}