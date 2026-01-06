package com.example.bookworm.ui


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import androidx.navigation.toRoute
import com.example.bookworm.ui.entitiesViewModel.AchievementViewModel
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
import com.example.bookworm.ui.screens.home.LibraryViewModel
import com.example.bookworm.ui.screens.settings.SettingsScreen
import com.example.bookworm.ui.screens.settings.ThemeViewModel
import com.example.bookworm.ui.screens.stats.StatsScreen
import com.example.bookworm.ui.screens.stats.StatsViewModel
import com.example.bookworm.ui.screens.userpage.UserPageScreen
import com.example.bookworm.ui.screens.userpage.UserPageViewModel
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
    data object Settings : BookWormRoute

    @Serializable
    data object Statistics : BookWormRoute

    @Serializable
    data class AddDiaryEntry(val bookId: Long) : BookWormRoute

    @Serializable
    data object Registration : BookWormRoute

    @Serializable
    data object Login : BookWormRoute
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

    val libraryViewModel: LibraryViewModel = koinViewModel(
        parameters = { parametersOf(userState.id) },
        key = "book_vm_user_${userState.id}"
    )
    val libraryState by libraryViewModel.state.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = BookWormRoute.Login
    ) {

        composable<BookWormRoute.Registration> {
            val registrationViewModel = koinViewModel<RegistrationViewModel>()
            val registrationState by registrationViewModel.state.collectAsStateWithLifecycle()
            RegistrationScreen(
                state = registrationState,
                actions = registrationViewModel.actions,
                onSignUp = userViewModel.actions::registerUser,
                onNavigateToHome = {
                    navController.navigate(route = BookWormRoute.Home,
                        navOptions = navOptions {
                            popUpTo(BookWormRoute.Registration) { inclusive = true }
                        })
                },
                onNavigateToLogin = {
                    navController.navigate(BookWormRoute.Login,
                        navOptions = navOptions {
                            popUpTo(BookWormRoute.Registration) { inclusive = true }
                        }
                    )
                },
            )
        }

        composable<BookWormRoute.Login> {
            val loginViewModel = koinViewModel<LoginViewModel>()
            val loginState by loginViewModel.state.collectAsStateWithLifecycle()
            LoginScreen(
                navController,
                state = loginState,
                actions = loginViewModel.actions,
                onSignIn = userViewModel.actions::loginUser,
                onNavigateToHome = {
                    navController.navigate(BookWormRoute.Home,
                        navOptions = navOptions {
                            popUpTo(BookWormRoute.Login) { inclusive = true }
                        })
                },
                onNavigateToRegistration = {
                    navController.navigate(BookWormRoute.Registration,
                        navOptions = navOptions {
                            popUpTo(BookWormRoute.Login) { inclusive = true }
                        })
                },
            )
        }

        /*TODO navigation
        *  To navigate to the details -> popUpTo("home") launchSingleTop = true
        * */
        composable<BookWormRoute.Home> {
            LibraryScreen(
                navController,
                state = libraryState,
                books = libraryViewModel.filteredBooks.collectAsState(),
                actions = libraryViewModel.actions,
                onBookClick = { bookId ->
                    navController.navigate(BookWormRoute.BookDetails(bookId))
                },
            )
        }

        composable<BookWormRoute.AddBook> { backStackEntry ->
            val route = backStackEntry.toRoute<BookWormRoute.AddBook>()

            val addBookVm =
                koinViewModel<AddBookViewModel>(parameters = { parametersOf(userState.id) })
            val state by addBookVm.state.collectAsStateWithLifecycle()

            AddBookScreen(
                navController,
                state = state,
                actions = addBookVm.actions,
                addBook = libraryViewModel.actions::addBook,
                bookId = route.bookId,
                onNavigateUp = { navController.navigateUp() },
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
                onNavigateToAddBook = {
                    navController.navigate(
                        BookWormRoute.AddBook(bookDetailsState.selectedBook.bookId)
                    )
                }
            )
        }

        composable<BookWormRoute.UserPage> {

            val achievementViewModel: AchievementViewModel = koinViewModel<AchievementViewModel>(parameters = {
                parametersOf(userState.user.userId)
            })
            val userPageViewModel: UserPageViewModel = koinViewModel<UserPageViewModel>()

            val unlockedAchievementState by achievementViewModel.unlockedAchievementsState.collectAsStateWithLifecycle()
            val lockedAchievementsState by achievementViewModel.lockedAchievementsState.collectAsStateWithLifecycle()

            val state by userPageViewModel.state.collectAsStateWithLifecycle()

            UserPageScreen(
                navController,
                userState = userState,
                favourites = libraryViewModel.filteredBooks.collectAsState().value.filter { it.favourite },
                state = state,
                actions = userPageViewModel.actions,
                unlockedAchievementState = unlockedAchievementState,
                lockedAchievementsState = lockedAchievementsState,
                onSeeFavourites = {
                    libraryViewModel.actions.filterByFavourites(true)
                    navController.navigate(BookWormRoute.Home)
                },
                onBookClick = { bookId ->
                    navController.navigate(BookWormRoute.BookDetails(bookId))
                },
                onGetAchievementImage = { achievementId ->
                    achievementViewModel.actions.getAchievementImage(achievementId)
                }
            )
        }

        composable<BookWormRoute.Settings> {
            val themeViewModel = koinViewModel<ThemeViewModel>()
            val themeState by themeViewModel.state.collectAsStateWithLifecycle()
            val settingsState by themeViewModel.settingsState.collectAsStateWithLifecycle()

            SettingsScreen(
                navController,
                state = themeState,
                settingsState = settingsState,
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
                actions = addDiaryEntryVm.actions,
                onNavigateUp = {
                    navController.navigateUp()
                }
            )
        }
    }
}

