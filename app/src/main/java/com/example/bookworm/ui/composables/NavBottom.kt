package com.example.bookworm.ui.composables

import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.bookworm.ui.BottomNavigation

@Composable
fun NavBottom(
    navController: NavController,
    currentDestination: NavDestination?,
    badgeCount: List<Pair<BottomNavigation, MutableState<Int>>>
) {
    NavigationBar {
        BottomNavigation.entries.forEach { currentScreen ->
            val isSelected =
                currentDestination?.hierarchy?.any {
                    it.hasRoute(
                        currentScreen.route::class
                    )
                } == true
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    val route = currentScreen.route
                    if (!navController.popBackStack(route, inclusive = false)) {
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    GetBadgeIcon(currentScreen, badgeCount)
                },
                label = {
                    Text(
                        stringResource(currentScreen.labelRes)
                    )
                },
            )
        }
    }
}


@Composable
private fun GetBadgeIcon(
    currentScreen: BottomNavigation,
    badgeCount: List<Pair<BottomNavigation, MutableState<Int>>>
) {
    val count = badgeCount.first { it.first == currentScreen }.second.value
    BadgedBox(
        badge = {
            if (count > 0) {
                Badge {
                    Text(text = count.toString())
                }
            }
        }
    ) {
        Icon(currentScreen.icon, null)
    }
}