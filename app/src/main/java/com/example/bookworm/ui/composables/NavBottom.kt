package com.example.bookworm.ui.composables

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.bookworm.ui.BottomNavigation


@Composable
fun NavBottom(navController: NavController) {
    val destinations = listOf(
        BottomNavigation.Library,
        BottomNavigation.Stats,
        BottomNavigation.UserPage
    )

    //val navBackStackEntry = navController.currentBackStackEntryAsState().value
    //val currentRoute = navBackStackEntry?.toRoute<BookWormRoute>()

    BottomAppBar (windowInsets = NavigationBarDefaults.windowInsets) {
        destinations.forEachIndexed { index, destination ->
            //val isSelected = currentRoute == destination.route
            NavigationBarItem(
                selected = false,
                onClick = {
                    navController.navigate(destination.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        destination.icon,
                        contentDescription = "${destination.label} icon"
                    )
                },
                label = { Text(destination.label) }
            )
        }
    }
}