package com.example.bookworm.ui.composables

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.bookworm.R
import com.example.bookworm.core.data.database.entities.BookEntity


@Composable
fun BookItem(book: BookEntity, onBookClick: (Long) -> Unit) {
    Card(
        onClick = { onBookClick(book.bookId) },
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        )
        {
            val imageUriString = book.image
            val imageUri = imageUriString?.let { Uri.parse(it) }

            ImageWithPlaceholder(
                imageUri,
                Size.BookItem,
                stringResource(R.string.book_cover_desc),
                RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 0.dp, bottomEnd = 0.dp)
            )
            Text(
                book.title,
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Left,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}