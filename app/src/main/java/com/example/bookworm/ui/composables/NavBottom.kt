package com.example.bookworm.ui.composables

/*TODO*/
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavController
import androidx.navigation.navOptions
import com.example.bookworm.ui.BottomNavigation
import com.example.bookworm.ui.entitiesViewModel.NotificationsState


@Composable
fun NavBottom(
    navController: NavController,
    notificationsState: NotificationsState
) {

    val destinations = listOf(
        BottomNavigation.Library,
        BottomNavigation.Stats,
        BottomNavigation.Notifications,
        BottomNavigation.UserPage,
    )

    val selectedNavigationIndex = rememberSaveable {
        mutableIntStateOf(0)
    }

    NavigationBar {
        destinations.forEachIndexed { index, destination ->

            NavigationBarItem(
                selected = selectedNavigationIndex.value == index,
                onClick = {
                    selectedNavigationIndex.value = index
                    navController.navigate(destination.route,
                        navOptions = navOptions {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        })
                },
                icon = {
                    if (destination == BottomNavigation.Notifications) {
                        BadgedBox(
                            badge = {
                                if (notificationsState.unreadNotifications.isNotEmpty()) {
                                    Badge(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = MaterialTheme.colorScheme.onPrimary,
                                        content = {
                                            Text(notificationsState.unreadNotifications.size.toString())
                                        }
                                    )
                                }
                            }
                        ) {
                            Icon(
                                destination.icon,
                                contentDescription = "${destination.label} icon"
                            )
                        }
                    } else {
                        Icon(
                            destination.icon,
                            contentDescription = "${destination.label} icon"
                        )

                    }
                },
                label = {
                    Text(
                        destination.label,
                    )
                },

                )
        }
    }
}