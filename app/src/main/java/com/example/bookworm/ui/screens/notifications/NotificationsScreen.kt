package com.example.bookworm.ui.screens.notifications

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.CircleNotifications
import androidx.compose.material.icons.outlined.Stars
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookworm.ui.composables.AppBar
import com.example.bookworm.ui.composables.NavBottom
import com.example.bookworm.ui.entitiesViewModel.LoggedUserState
import com.example.bookworm.ui.entitiesViewModel.NotificationActions
import com.example.bookworm.ui.entitiesViewModel.NotificationsState
import com.example.bookworm.ui.screens.userpage.AchievementPositions
import com.example.bookworm.ui.screens.userpage.achievementStringFinder

@Composable
fun NotificationsScreen(
    navController: NavController,
    state: NotificationsState,
    actions: NotificationActions,
    onNavigateToUserPage: () -> Unit,
) {
    Scaffold(
        topBar = { AppBar(navController) },
        bottomBar = { NavBottom(navController, state) },
    ) { contentPadding ->

        LazyColumn(
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            item {
                if (state.notifications.isNotEmpty()) {
                    state.notifications.forEach { it ->

                        val strings = achievementStringFinder(it.body)

                        ListItem(
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable { onNavigateToUserPage() },
                            headlineContent = {
                                Text(
                                    it.title,
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                )
                            },
                            supportingContent = {
                                Text(
                                    strings[AchievementPositions.ACHIEVEMENT_NAME],
                                    style = MaterialTheme.typography.bodySmall,
                                )
                            },
                            leadingContent = {
                                Icon(
                                    Icons.Outlined.Stars,
                                    "Award notification icon"
                                )

                            },
                            trailingContent = {
                                if (!it.isRead) {
                                    IconButton(onClick = { actions.readNotification(it.notificationId) }) {
                                        Icon(Icons.Outlined.Check, "Read notification icon")
                                    }
                                } else {
                                    Text(
                                        "Read",
                                        color = Color.Green
                                    )
                                }
                            },
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,

                        ) {
                        Icon(
                            imageVector = Icons.Outlined.CircleNotifications,
                            contentDescription = "Notification Icon",
                            modifier = Modifier.size(64.dp)
                        )
                        Text(
                            text = "You have no notifications",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

        }
    }
}
