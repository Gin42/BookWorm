package com.example.bookworm.ui.composables

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest

enum class Size { Sm, Lg, BookItem, BookDetail }

@Composable
fun ImageWithPlaceholder(uri: Uri?, size: Size, desc: String, shape: Shape) {

    var modifier = Modifier.clip(shape)

    when (size) {
        Size.Sm -> {
            modifier = modifier.size(72.dp)
        }

        Size.Lg -> {
            modifier = modifier.size(128.dp)
        }

        Size.BookItem -> {
            modifier = modifier
                .height(200.dp)
                .fillMaxWidth()
        }

        Size.BookDetail -> {
            modifier = modifier
                .width(250.dp)
                .height(400.dp)
        }
    }

    if (uri?.path?.isNotEmpty() == true) {
        AsyncImage(
            ImageRequest.Builder(LocalContext.current)
                .data(uri)
                .crossfade(true)
                .build(),
            desc,
            contentScale = ContentScale.Crop,
            modifier = modifier

        )
    } else {
        Image(
            Icons.Outlined.Image,
            desc,
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer),
            modifier = modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(if (size == Size.Sm) 20.dp else 36.dp)
        )
    }
}
