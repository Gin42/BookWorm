package com.example.bookworm.ui.screens.userpage

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Lock
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookworm.R
import com.example.bookworm.core.data.database.entities.AchievementEntity
import com.example.bookworm.core.data.database.entities.BookEntity
import com.example.bookworm.ui.composables.AppBar
import com.example.bookworm.ui.composables.BookItem
import com.example.bookworm.ui.composables.ImageWithPlaceholder
import com.example.bookworm.ui.composables.NavBottom
import com.example.bookworm.ui.composables.Size
import com.example.bookworm.ui.entitiesViewModel.LockedAchievementsState
import com.example.bookworm.ui.entitiesViewModel.LoggedUserState
import com.example.bookworm.ui.entitiesViewModel.NotificationsState
import com.example.bookworm.ui.entitiesViewModel.UnlockedAchievementState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserPageScreen(
    navController: NavController,
    userState: LoggedUserState,
    favourites: List<BookEntity>,
    unlockedAchievementState: UnlockedAchievementState,
    lockedAchievementsState: LockedAchievementsState,
    onSeeFavourites: () -> Unit,
    onBookClick: (Long) -> Unit,
    notificationsState: NotificationsState
) {

    Scaffold(
        topBar = { AppBar(navController) },
        bottomBar = { NavBottom(navController, notificationsState) },
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
                        desc = stringResource(R.string.user_profile_picture_desc),
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
                                stringResource(R.string.favourites_message),
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                            )
                        },
                        trailingContent = {
                            if (favourites.isNotEmpty()) {
                                TextButton(
                                    onClick = {
                                        onSeeFavourites()

                                    }
                                ) {
                                    Text(stringResource(R.string.show_all_button))
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
                            BookItem(item, onBookClick)
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Outlined.Book,
                                null,
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                stringResource(R.string.favourites_empty_message),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onBackground
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
                    ListItem(
                        modifier = Modifier.padding(top = 8.dp, bottom = 0.dp),
                        headlineContent = {
                            Text(
                                stringResource(R.string.achievements_message),
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                            )
                        },
                    )

                    unlockedAchievementState.achievements.forEach {
                        Achievement(it)
                    }
                    lockedAchievementsState.achievements.forEach {
                        Achievement(it, locked = true)
                    }
                }
            }
        }
    }
}


@Composable
fun Achievement(
    item: AchievementEntity,
    locked: Boolean = false
) {

    val strings = achievementStringFinder(item.name)


    ListItem(
        modifier = Modifier
            .padding(8.dp),
        headlineContent = {
            Text(
                strings[AchievementPositions.ACHIEVEMENT_NAME],
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
            )
        },
        supportingContent = {
            Text(
                strings[AchievementPositions.ACHIEVEMENT_CONDITION],
                style = MaterialTheme.typography.bodySmall,
            )
        },
        leadingContent = {
            if (locked) {
                Box(
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Image(
                        modifier = Modifier
                            .height(80.dp)
                            .alpha(0.2F),
                        painter = painterResource(id = item.image),
                        contentDescription = stringResource(R.string.achievement_image_desc),
                        contentScale = ContentScale.FillHeight
                    )
                    Icon(
                        Icons.Outlined.Lock,
                        null,
                        modifier = Modifier.size(30.dp)
                    )
                }
            } else {
                Image(
                    modifier = Modifier
                        .height(80.dp),
                    painter = painterResource(id = item.image),
                    contentDescription = stringResource(R.string.achievement_image_desc),
                    contentScale = ContentScale.FillHeight
                )
            }
        }
    )
}

