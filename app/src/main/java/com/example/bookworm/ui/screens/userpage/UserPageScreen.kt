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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookworm.core.data.database.entities.AchievementEntity
import com.example.bookworm.core.data.database.entities.BookEntity
import com.example.bookworm.core.data.database.entities.UnlockedAchievementEntity
import com.example.bookworm.ui.composables.AppBar
import com.example.bookworm.ui.composables.BookItem
import com.example.bookworm.ui.composables.ImageWithPlaceholder
import com.example.bookworm.ui.composables.NavBottom
import com.example.bookworm.ui.composables.Size
import com.example.bookworm.ui.entitiesViewModel.LoggedUserState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserPageScreen(
    navController: NavController,
    userState: LoggedUserState,
    favourites: List<BookEntity>,
    userAchievements: List<Pair<AchievementEntity, UnlockedAchievementEntity?>>,
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

                    val imageUriString = userState.image
                    val imageUri = imageUriString?.let { Uri.parse(it) }

                    ImageWithPlaceholder(
                        imageUri,
                        Size.Lg,
                        desc = "User photo",
                        CircleShape
                    )

                    Text(
                        userState.username,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                }
            }

            // User favourites - carousel
            item {

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
                            if (favourites.isNotEmpty()) {
                                TextButton(
                                    onClick = { /*TODO must go to library passing an
                                argument that specify that i want to see
                                only the favourite books*/
                                    }
                                ) {
                                    Text("Show all")
                                }
                            }
                        },
                    )

                    if (favourites.isNotEmpty()) {
                        HorizontalUncontainedCarousel(
                            state = rememberCarouselState { favourites.count() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(top = 16.dp, bottom = 16.dp),
                            itemWidth = 150.dp,
                            itemSpacing = 8.dp,
                        ) { index ->
                            val item = favourites[index]
                            BookItem(item, navController)
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,

                            ) {
                            Icon(
                                imageVector = Icons.Outlined.Book,
                                contentDescription = "Book icon",
                                modifier = Modifier.size(64.dp)
                            )
                            Text(
                                text = "It seems like you haven't found your favourites.\n Go read some.",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
            //user badges
            item {
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

                    Row {
                        userAchievements.forEach{ item ->
                            Badge(item)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Badge(item: Pair<AchievementEntity, UnlockedAchievementEntity?>) {
    val (achievement, userProgress) = item
    val isCompleted = userProgress?.isCompleted ?: false

    Column(
        modifier = Modifier
            .padding(8.dp)
            .width(100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // For simplicity, using a default icon; you can customize per achievement
        Image(
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth(),
            imageVector = if (isCompleted) Icons.Outlined.AutoAwesome else Icons.Outlined.StarOutline,
            contentDescription = achievement.description,
            contentScale = ContentScale.Crop
        )
        Text(
            achievement.name,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            achievement.description,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

