package com.example.bookworm.ui


import android.content.ContentValues.TAG
import android.util.Log
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
import com.example.bookworm.core.data.models.usecase.ReadingStatusStateMachine
import com.example.bookworm.ui.entitiesViewModel.BookViewModel
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
import com.example.bookworm.ui.screens.stats.StatsViewModel
import com.example.bookworm.ui.screens.userpage.UserPageScreen
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf


sealed interface BookWormRoute {
    @Serializable
    data object Home : BookWormRoute //library

    @Serializable
    data class BookDetails(val bookId: Long) : BookWormRoute

    @Serializable
    data class AddBook(val bookId: Long?) : BookWormRoute

    @Serializable
    data object UserPage : BookWormRoute

    @Serializable
    data object Setting : BookWormRoute

    @Serializable
    data object Statistics : BookWormRoute

    @Serializable
    data class AddDiaryEntry(val bookId: Long) : BookWormRoute

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

    val bookViewModel: BookViewModel = koinViewModel(
        parameters = { parametersOf(userState.id) },
        key = "book_vm_user_${userState.id}"
    )
    val bookState by bookViewModel.state.collectAsStateWithLifecycle()


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
            LoginScreen(
                navController,
                state = loginState,
                actions = loginViewModel.actions,
                onSignIn = userViewModel.actions::loginUser
            )
        }

        composable<BookWormRoute.Home> {
            Log.println(
                Log.DEBUG,
                TAG,
                "HELLO: user -> ${userState.user}"
            )
            LibraryScreen(
                navController,
                bookState = bookState,
                userState = userState
            )
        }

        composable<BookWormRoute.AddBook> { backStackEntry ->
            val route = backStackEntry.toRoute<BookWormRoute.AddBook>() // bookId

            val addBookVm =
                koinViewModel<AddBookViewModel>(parameters = { parametersOf(userState.id) })
            val state by addBookVm.state.collectAsStateWithLifecycle()

            AddBookScreen(
                navController,
                state = state,
                actions = addBookVm.actions,
                addBook = {
                    bookViewModel.actions.addBook(
                        state.toBook()
                    )
                },
                bookId = route.bookId,
            )
        }

        composable<BookWormRoute.BookDetails> { backStackEntry ->
            val route = backStackEntry.toRoute<BookWormRoute.BookDetails>()
            val bookDetailsViewModel =
                koinViewModel<BookDetailsViewModel>(parameters = {
                    parametersOf(
                        route.bookId,
                        userState.user.userId
                    )
                })
            val bookDetailsState by bookDetailsViewModel.state.collectAsStateWithLifecycle()

            BookDetailsScreen(
                navController,
                bookDetailsState,
                bookDetailsViewModel.actions,
            )
        }

        composable<BookWormRoute.UserPage> {
            UserPageScreen(
                navController,
                userState = userState,
                favourites = bookState.books.filter { it.favourite }
            )
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
            val statsVm = koinViewModel<StatsViewModel>(
                parameters = {
                    parametersOf(
                        userState.id
                    )
                }
            )
            val state by statsVm.state.collectAsStateWithLifecycle()

            StatsScreen(navController, state, statsVm.actions)
        }

        composable<BookWormRoute.AddDiaryEntry> { backStackEntry ->

            val route = backStackEntry.toRoute<BookWormRoute.AddDiaryEntry>()

            val addDiaryEntryVm = koinViewModel<AddDiaryEntryViewModel>(
                parameters = {
                    parametersOf(
                        route.bookId,
                        userState.id
                    )
                }
            )

            val state by addDiaryEntryVm.state.collectAsStateWithLifecycle()

            AddDiaryEntryScreen(
                navController = navController,
                state = state,
                actions = addDiaryEntryVm.actions
            )
        }
    }
}

