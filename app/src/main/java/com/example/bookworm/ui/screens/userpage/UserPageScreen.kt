package com.example.bookworm.ui.screens.userpage

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Diamond
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
import com.example.bookworm.ui.entitiesViewModel.UnlockedAchievementState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserPageScreen(
    navController: NavController,
    userState: LoggedUserState,
    state: UserPageState,
    actions: UserPageActions,
    favourites: List<BookEntity>,
    unlockedAchievementState: UnlockedAchievementState,
    lockedAchievementsState: LockedAchievementsState,
    onGetAchievementImage: (Long) -> Int?,
    onSeeFavourites: () -> Unit,
    onBookClick: (Long) -> Unit,
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
                                    onClick = {
                                        onSeeFavourites()

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
                            BookItem(item, onBookClick)
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
                    ListItem(
                        modifier = Modifier.padding(top = 8.dp, bottom = 0.dp),
                        headlineContent = {
                            Text(
                                "Achievements",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                            )
                        },
                        trailingContent = {
                            if (lockedAchievementsState.achievements.isNotEmpty()) {
                                TextButton(
                                    onClick = {
                                        actions.setSeeAllAchievements(!state.seeAllAchievements)
                                    }
                                ) {
                                    Text(
                                        text = if (state.seeAllAchievements) {
                                            "Hide locked ones"
                                        } else {
                                            "Show locked ones"
                                        }
                                    )
                                }
                            }
                        },
                    )

                    /** If not see all, then see only the unlocked ones,
                     * else show all achievements.*/

                    unlockedAchievementState.achievements.forEach { it ->
                        Achievement(it, onGetAchievementImage)
                    }

                    if (state.seeAllAchievements) {
                        val filteredAchievements =
                            lockedAchievementsState.achievements.filterNot { it in unlockedAchievementState.achievements }
                        filteredAchievements.forEach {
                            Achievement(it, onGetAchievementImage, locked = true)
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
fun Achievement(
    item: AchievementEntity,
    onGetAchievementImage: (Long) -> Int?,
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
            val achievementImageResId = onGetAchievementImage(item.achievementId)
            if (achievementImageResId != null) {
                Image(
                    modifier = Modifier
                        .height(100.dp),
                    painter = painterResource(id = achievementImageResId),
                    contentDescription = "Achievement image",
                    contentScale = ContentScale.FillHeight
                )
            }
        },
        trailingContent = {
            Text(
                text = if (locked) {
                    "LOCKED!"
                } else {
                    "UNLOCKED!"
                },
                color = if (locked) {
                    Color.Red
                } else {
                    Color.Green
                },
            )
        },
    )
}

