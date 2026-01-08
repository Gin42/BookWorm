package com.example.bookworm.ui.screens.notifications

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.CircleNotifications
import androidx.compose.material.icons.outlined.Stars
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookworm.R
import com.example.bookworm.ui.composables.AppBar
import com.example.bookworm.ui.composables.NavBottom
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
    ) { contentPadding ->

        LazyColumn(
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            item {
                if (state.notifications.isNotEmpty()) {
                    state.notifications.forEach {

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
                                    stringResource(R.string.achievement_notification_icon_desc)
                                )

                            },
                            trailingContent = {
                                if (!it.isRead) {
                                    OutlinedIconButton(onClick = { actions.readNotification(it.notificationId) }) {
                                        Icon(
                                            Icons.Outlined.Check,
                                            stringResource(R.string.read_notification_icon_desc)
                                        )
                                    }
                                } else {
                                    Text(
                                        stringResource(R.string.notification_read_message),
                                        color = MaterialTheme.colorScheme.primary,
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold,
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
                            contentDescription = stringResource(R.string.notification_icon_desc),
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = stringResource(R.string.notification_empty_message),
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }

        }
    }
}
