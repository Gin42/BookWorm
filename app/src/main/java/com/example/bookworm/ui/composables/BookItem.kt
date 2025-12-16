package com.example.bookworm.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookworm.ui.BookWormRoute


@Composable
fun BookItem(item: String, navController: NavController) {
    Card(
        onClick = {navController.navigate(BookWormRoute.BookDetails("Book ID")) },
    ) {
        Column (
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        )
        {
            Image(
                imageVector = Icons.Outlined.Book,
                "Book cover",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .height(200.dp)
            )
            Spacer(Modifier.size(8.dp))
            Text(
                item,
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Left
            )
        }
    }
}