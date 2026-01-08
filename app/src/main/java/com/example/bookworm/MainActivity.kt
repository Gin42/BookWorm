package com.example.bookworm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bookworm.core.data.models.Theme
import com.example.bookworm.ui.BookWormNavGraph
import com.example.bookworm.ui.BottomNavigation
import com.example.bookworm.ui.composables.NavBottom
import com.example.bookworm.ui.screens.settings.ThemeViewModel
import com.example.bookworm.ui.theme.BookWormTheme
import org.koin.androidx.compose.koinViewModel
import androidx.navigation.NavDestination.Companion.hasRoute

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeViewModel = koinViewModel<ThemeViewModel>()
            val themeState by themeViewModel.state.collectAsStateWithLifecycle()

            val badgeCount = BottomNavigation.entries.map {
                it to remember { mutableIntStateOf(0) }
            }


            BookWormTheme(
                darkTheme = when (themeState.theme) {
                    Theme.Light -> false
                    Theme.Dark -> true
                    Theme.System -> isSystemInDarkTheme()
                }
            ) {

                if (!themeState.isLoaded) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                    return@BookWormTheme
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val backStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = backStackEntry?.destination
                    val showBottomBar = BottomNavigation.entries.any { screen ->
                        currentDestination?.hierarchy?.any {
                            it.hasRoute(
                                route = screen.route::class,
                            )
                        } ?: false
                    }

                    Scaffold(
                        bottomBar = {
                            AnimatedVisibility(showBottomBar) {
                                NavBottom(
                                    navController = navController,
                                    currentDestination = currentDestination,
                                    badgeCount = badgeCount
                                )
                            }
                        }
                    ) { innerPadding ->
                        BookWormNavGraph(
                            navController,
                            updateBadgeCount = { screen: BottomNavigation, count: Int ->
                                badgeCount.first { it.first == screen }.second.intValue = count
                            },
                            modifier = Modifier
                                .padding(innerPadding)
                                .consumeWindowInsets(innerPadding),
                            themeState = themeState,
                            themeViewModel = themeViewModel
                        )
                    }
                }


            }
        }
    }
}