package com.example.bookworm.ui


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.bookworm.ui.screens.addbook.AddBookScreen
import com.example.bookworm.ui.screens.adddiaryentry.AddDiaryEntryScreen
import com.example.bookworm.ui.screens.adddiaryentry.AddDiaryEntryViewModel
import com.example.bookworm.ui.screens.bookdetails.BookDetailsScreen
import com.example.bookworm.ui.screens.home.LibraryScreen
import com.example.bookworm.ui.screens.settings.SettingsScreen
import com.example.bookworm.ui.screens.settings.SettingsViewModel
import com.example.bookworm.ui.screens.stats.StatsScreen
import com.example.bookworm.ui.screens.userpage.UserPageScreen
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

sealed interface BookWormRoute {
    @Serializable data object Home : BookWormRoute //library
    @Serializable data class BookDetails(val bookId: String) : BookWormRoute
    @Serializable data object AddBook : BookWormRoute
    @Serializable data object UserPage : BookWormRoute
    @Serializable data object Setting : BookWormRoute
    @Serializable data object Statistics : BookWormRoute
    @Serializable data class DiaryDetails(val entryId: String) : BookWormRoute
    @Serializable data object AddDiaryEntry : BookWormRoute
//ToDo
}

sealed class BottomNavigation(val label: String, val icon: ImageVector, val route: BookWormRoute) {
    data object Library : BottomNavigation("Library", Icons.Outlined.Book, BookWormRoute.Home)
    data object Stats : BottomNavigation("Stats", Icons.Outlined.BarChart, BookWormRoute.Statistics)
    data object UserPage : BottomNavigation("User Page", Icons.Outlined.Person,
        BookWormRoute.UserPage
    )
}

@Composable
fun BookWormNavGraph(navController :  NavHostController, isDarkMode: MutableState<Boolean>) {
    NavHost(
        navController = navController,
        startDestination = BookWormRoute.Home
    ) {
        composable<BookWormRoute.Home> {
            LibraryScreen(navController)
        }
        composable<BookWormRoute.BookDetails> { backStackEntry ->
            val route = backStackEntry.toRoute<BookWormRoute.BookDetails>()
            BookDetailsScreen(navController, route.bookId)
        }
        composable<BookWormRoute.AddBook> {
            AddBookScreen(navController)
        }
        composable<BookWormRoute.UserPage> {
            UserPageScreen(navController)
        }

        composable<BookWormRoute.Setting> {
            val settingsVM = koinViewModel<SettingsViewModel>()
            val state by settingsVM.state.collectAsStateWithLifecycle()
            SettingsScreen(navController, state, settingsVM.actions)
        }

        composable<BookWormRoute.Statistics> {
            StatsScreen(navController)
        }

        composable<BookWormRoute.AddDiaryEntry> {
            val addDiaryEntryVm = koinViewModel<AddDiaryEntryViewModel>()
            val state by addDiaryEntryVm.state.collectAsStateWithLifecycle()
            AddDiaryEntryScreen( navController, state, addDiaryEntryVm.actions)
        }
    }
}

