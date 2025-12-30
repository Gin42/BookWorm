package com.example.bookworm.ui


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.bookworm.ui.entitiesViewModel.UserViewModel
import com.example.bookworm.ui.screens.addbook.AddBookScreen
import com.example.bookworm.ui.screens.addbook.AddBookViewModel
import com.example.bookworm.ui.screens.adddiaryentry.AddDiaryEntryScreen
import com.example.bookworm.ui.screens.adddiaryentry.AddDiaryEntryViewModel
import com.example.bookworm.ui.screens.authentication.LoginScreen
import com.example.bookworm.ui.screens.authentication.LoginViewModel
import com.example.bookworm.ui.screens.authentication.RegistrationScreen
import com.example.bookworm.ui.screens.authentication.RegistrationViewModel
import com.example.bookworm.ui.screens.bookdetails.BookDetailsScreen
import com.example.bookworm.ui.screens.bookdetails.BookDetailsViewModel
import com.example.bookworm.ui.screens.home.LibraryScreen
import com.example.bookworm.ui.screens.settings.SettingsScreen
import com.example.bookworm.ui.screens.settings.ThemeViewModel
import com.example.bookworm.ui.screens.stats.StatsScreen
import com.example.bookworm.ui.screens.userpage.UserPageScreen
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

sealed interface BookWormRoute {
    @Serializable
    data object Home : BookWormRoute //library

    @Serializable
    data class BookDetails(val bookId: String) : BookWormRoute

    @Serializable
    data object AddBook : BookWormRoute

    @Serializable
    data object UserPage : BookWormRoute

    @Serializable
    data object Setting : BookWormRoute

    @Serializable
    data object Statistics : BookWormRoute

    @Serializable
    data object AddDiaryEntry : BookWormRoute

    @Serializable
    data object Registration : BookWormRoute

    @Serializable
    data object Login : BookWormRoute
//ToDo
}

sealed class BottomNavigation(val label: String, val icon: ImageVector, val route: BookWormRoute) {
    data object Library : BottomNavigation("Library", Icons.Outlined.Book, BookWormRoute.Home)
    data object Stats : BottomNavigation("Stats", Icons.Outlined.BarChart, BookWormRoute.Statistics)
    data object UserPage : BottomNavigation(
        "User Page", Icons.Outlined.Person,
        BookWormRoute.UserPage
    )
}

@Composable
fun BookWormNavGraph(navController: NavHostController) {

    val userViewModel = koinViewModel<UserViewModel>()
    val userState by userViewModel.state.collectAsStateWithLifecycle()


    NavHost(
        navController = navController,
        startDestination = BookWormRoute.Login
    ) {

        composable<BookWormRoute.Registration> {
            val registrationViewModel = koinViewModel<RegistrationViewModel>()
            val registrationState by registrationViewModel.state.collectAsStateWithLifecycle()
            RegistrationScreen(
                navController,
                state = registrationState,
                actions = registrationViewModel.actions,
                onSignUp = userViewModel.actions::registerUser
            )
        }

        composable<BookWormRoute.Login> {
            val loginViewModel = koinViewModel<LoginViewModel>()
            val loginState by loginViewModel.state.collectAsStateWithLifecycle()
            LoginScreen(navController,
                state = loginState,
                actions = loginViewModel.actions,
                onSignIn = userViewModel.actions::loginUser)
        }

        composable<BookWormRoute.Home> {
            LibraryScreen(navController)
        }
        composable<BookWormRoute.BookDetails> { backStackEntry ->
            val route = backStackEntry.toRoute<BookWormRoute.BookDetails>()
            val bookDetailsViewModel = koinViewModel<BookDetailsViewModel>()
            val bookDetailsState by bookDetailsViewModel.state.collectAsStateWithLifecycle()
            BookDetailsScreen(
                navController,
                route.bookId,
                bookDetailsState,
                bookDetailsViewModel.actions
            )
        }
        composable<BookWormRoute.AddBook> {
            val addBookVm = koinViewModel<AddBookViewModel>()
            val state by addBookVm.state.collectAsStateWithLifecycle()
            AddBookScreen(navController, state, addBookVm.actions)
        }
        composable<BookWormRoute.UserPage> {
            UserPageScreen(navController, userState = userState)
        }

        composable<BookWormRoute.Setting> {
            val themeViewModel = koinViewModel<ThemeViewModel>()
            val themeState by themeViewModel.state.collectAsStateWithLifecycle()
            val settingState by themeViewModel.settingState.collectAsStateWithLifecycle()

            SettingsScreen(
                navController,
                state = themeState,
                settingState = settingState,
                actions = themeViewModel.actions,
                onThemeSelected = themeViewModel::changeTheme
            )
        }

        composable<BookWormRoute.Statistics> {
            StatsScreen(navController)
        }

        composable<BookWormRoute.AddDiaryEntry> {
            val addDiaryEntryVm = koinViewModel<AddDiaryEntryViewModel>()
            val state by addDiaryEntryVm.state.collectAsStateWithLifecycle()
            AddDiaryEntryScreen(navController, state, addDiaryEntryVm.actions)
        }
    }
}

