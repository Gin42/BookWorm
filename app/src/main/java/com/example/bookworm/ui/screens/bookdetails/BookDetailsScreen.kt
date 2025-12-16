package com.example.bookworm.ui.screens.bookdetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookworm.ui.composables.AddDiaryFloatingButton
import com.example.bookworm.ui.composables.AppBar
import com.example.bookworm.ui.composables.NavBottom


@Composable
fun BookDetailsScreen(
    navController: NavController,
    bookId: String
) {
    Scaffold(
        topBar = { AppBar(navController = navController, goBack = true) },
        floatingActionButton = { AddDiaryFloatingButton(navController) },
        bottomBar = { NavBottom(navController) }
    ) { contentPadding ->

        val numberOfEntries = 5

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.padding(contentPadding)
        ) {
            item {
                // Image
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .height(400.dp)
                        .width(250.dp)
                        .border(2.dp, Color.Gray, RoundedCornerShape(20.dp)), /*DELETE*/
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        imageVector = Icons.Outlined.Book,
                        contentDescription = "Book cover",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
            }

            item {
                // Divider
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 15.dp),
                    thickness = 2.dp
                )
            }

            item {
                ListItem(
                    modifier = Modifier
                        .padding(bottom = 15.dp),
                    headlineContent = {
                        Text(
                            "Book Title",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    supportingContent = {
                        Text(
                            "Author Name",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    trailingContent = {
                        IconButton(
                            onClick = { /* TODO */ }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.FavoriteBorder,
                                contentDescription = "Favorite Icon",
                            )
                        }
                    },
                )
            }

            item {
                ListItem(
                    modifier = Modifier
                        .padding(bottom = 15.dp),
                    headlineContent = {
                        LinearProgressIndicator(
                            progress = { 0.8f },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    },
                    leadingContent = {
                        Text(
                            "Progress:",
                            style = MaterialTheme.typography.labelLarge,
                        )
                    },
                    trailingContent = {
                        Text(
                            "80%",
                            style = MaterialTheme.typography.labelLarge,
                        )
                    },
                )
            }


            items(numberOfEntries) { index ->
                DiaryEntry(navController)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryEntry(navController: NavController) {

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )

    ListItem(
        modifier = Modifier
            .clickable {
                showBottomSheet = true
            },
        headlineContent = {
            Text(
                "gg/mm/aaaa",
                style = MaterialTheme.typography.bodyLarge
            )
        },
        supportingContent = {
            Text(
                "Sit non officiis aliquam ipsa corporis quidem a id. Iste enim quidem animi aliquam eius. Error porro et suscipit quasi nostrum.",
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    )
    HorizontalDivider(modifier = Modifier.padding(bottom = 8.dp), thickness = 2.dp)

    if (showBottomSheet) {
        ModalBottomSheet(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            sheetState = sheetState,
            onDismissRequest = { showBottomSheet = false }
        ) {
            Column(
                modifier = Modifier.padding(15.dp)
            ) {
                Text(
                    "gg/mm/aaaa",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp)
                )
                Text(
                    "Pages read: 250",
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    "Sit non officiis aliquam ipsa corporis quidem a id. Iste enim quidem animi aliquam eius. Error porro et suscipit quasi nostrum.",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

