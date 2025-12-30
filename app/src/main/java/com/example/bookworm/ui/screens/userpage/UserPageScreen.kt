package com.example.bookworm.ui.screens.userpage

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Diamond
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookworm.ui.entitiesViewModel.LoggedUserState
import com.example.bookworm.ui.composables.AppBar
import com.example.bookworm.ui.composables.BookItem
import com.example.bookworm.ui.composables.ImageWithPlaceholder
import com.example.bookworm.ui.composables.NavBottom
import com.example.bookworm.ui.composables.Size


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserPageScreen(
    navController: NavController,
    userState: LoggedUserState
) {
    Scaffold(
        topBar = { AppBar(navController) },
        bottomBar = { NavBottom(navController) },
    ) { contentPadding ->

        LazyColumn(
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            //user photo and name
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(top = 30.dp)
                        .fillMaxWidth()
                ) {
                    Log.d(TAG, "User Image: ${userState.image}, Username: ${userState.username}")
                    val imageUriString = userState.image
                    val imageUri = imageUriString?.let { Uri.parse(it) }

                    ImageWithPlaceholder(
                        imageUri,
                        Size.Lg,
                        desc = "User photo"
                    )

                    Text(
                        userState.username,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                }
            }

            //user favourites - carousel
            item {

                val books = (1..5).map { "Title" }

                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                ) {
                    ListItem(
                        modifier = Modifier.padding(top = 8.dp, bottom = 0.dp),
                        headlineContent = {
                            Text(
                                "Favourites",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                            )
                        },
                        trailingContent = {
                            TextButton(
                                onClick = { /*TODO must go to library passing an
                                argument that specify that i want to see
                                only the favourite books*/
                                }
                            ) {
                                Text("Show all")
                            }
                        },
                    )
                    HorizontalUncontainedCarousel(
                        state = rememberCarouselState { books.count() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(top = 16.dp, bottom = 16.dp),
                        itemWidth = 150.dp,
                        itemSpacing = 8.dp,
                    ) { i ->
                        val item = books[i]
                        BookItem(item, navController)
                    }
                }
            }
            //user badges
            item {

                val badges = listOf(
                    BadgeItem(0, Icons.Outlined.Diamond),
                    BadgeItem(1, Icons.Outlined.Diamond),
                    BadgeItem(2, Icons.Outlined.Diamond),
                    BadgeItem(3, Icons.Outlined.Diamond),
                    BadgeItem(4, Icons.Outlined.Diamond),
                )

                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        "Badges",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp, bottom = 0.dp),
                    )


                    val columns = 2
                    for (i in badges.indices step columns) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp, bottom = 16.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            for (j in 0 until columns) {
                                if (i + j < badges.size) {
                                    Badge(badges[i + j])
                                }
                            }
                        }
                    }

                }

            }

        }
    }
}

data class BadgeItem(
    val id: Int,
    val icon: ImageVector
)

@Composable
fun Badge(item: BadgeItem) {
    Column(
        modifier = Modifier
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .height(100.dp),
            imageVector = item.icon,
            contentDescription = "Badge description",
            contentScale = ContentScale.Crop
        )
        Text(
            "Badge title",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
        )
        Text(
            "Badge description",
            style = MaterialTheme.typography.bodySmall,
        )
    }
}