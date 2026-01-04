package com.example.bookworm.ui.composables

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavController
import androidx.navigation.navOptions
import com.example.bookworm.ui.BottomNavigation


@Composable
fun NavBottom(navController: NavController) {

    val destinations = listOf(
        BottomNavigation.Library,
        BottomNavigation.Stats,
        BottomNavigation.UserPage
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
                    Icon(
                        destination.icon,
                        contentDescription = "${destination.label} icon"
                    )
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