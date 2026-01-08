package com.example.bookworm.ui


import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.example.bookworm.R
import com.example.bookworm.ui.entitiesViewModel.AchievementViewModel
import com.example.bookworm.ui.entitiesViewModel.NotificationViewModel
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
import com.example.bookworm.ui.screens.notifications.NotificationsScreen
import com.example.bookworm.ui.screens.settings.SettingsScreen
import com.example.bookworm.ui.screens.settings.ThemeState
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
    data object Settings : BookWormRoute

    @Serializable
    data object Statistics : BookWormRoute

    @Serializable
    data class AddDiaryEntry(val bookId: Long) : BookWormRoute

    @Serializable
    data object Registration : BookWormRoute

    @Serializable
    data object Login : BookWormRoute

    @Serializable
    data object Notifications : BookWormRoute

    @Serializable
    data object AUTH : BookWormRoute

    @Serializable
    data object APP : BookWormRoute
}

enum class BottomNavigation(
    @StringRes val labelRes: Int,
    val icon: ImageVector,
    val route: BookWormRoute
) {
    Library(R.string.library_label, Icons.Outlined.Book, BookWormRoute.Home),
    Stats(R.string.stats_label, Icons.Outlined.BarChart, BookWormRoute.Statistics),
    Notifications(R.string.inbox_label, Icons.Outlined.Inbox, BookWormRoute.Notifications),
    UserPage(R.string.user_page_label, Icons.Outlined.Person, BookWormRoute.UserPage),
}

fun NavHostController.navigateSingleTopTo(route: BookWormRoute) {
    navigate(route) {
        popUpTo(BookWormRoute.APP) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

fun NavHostController.enterApp() {
    navigate(BookWormRoute.APP) {
        popUpTo(BookWormRoute.AUTH) {
            inclusive = true
        }
        launchSingleTop = true
    }
}


@Composable
fun BookWormNavGraph(
    navController: NavHostController,
    updateBadgeCount: (BottomNavigation, Int) -> Unit,
    modifier: Modifier = Modifier,
    themeState: ThemeState,
    themeViewModel: ThemeViewModel
) {

    val userViewModel = koinViewModel<UserViewModel>()
    val userState by userViewModel.state.collectAsStateWithLifecycle()

    val libraryViewModel: LibraryViewModel = koinViewModel(
        parameters = { parametersOf(userState.id) },
        key = "book_vm_user_${userState.id}"
    )
    val libraryState by libraryViewModel.state.collectAsStateWithLifecycle()

    val notificationsViewModel = koinViewModel<NotificationViewModel>(
        parameters = { parametersOf(userState.id) },
        key = "notification_vm_user_${userState.id}"
    )
    val notificationsState by notificationsViewModel.state.collectAsStateWithLifecycle()


    LaunchedEffect(notificationsState.notifications) {
        updateBadgeCount(
            BottomNavigation.Notifications,
            notificationsState.notifications.count { !it.isRead }
        )
    }

    NavHost(
        navController = navController,
        startDestination = BookWormRoute.AUTH,
        modifier = modifier
    ) {

        navigation<BookWormRoute.AUTH>(
            startDestination = BookWormRoute.Login
        ) {
            composable<BookWormRoute.Registration> {
                val registrationViewModel = koinViewModel<RegistrationViewModel>()
                val registrationState by registrationViewModel.state.collectAsStateWithLifecycle()
                RegistrationScreen(
                    state = registrationState,
                    actions = registrationViewModel.actions,
                    themeState = themeState,
                    onSignUp = userViewModel.actions::registerUser,
                    onNavigateToHome = {
                        navController.enterApp()
                    },
                    onNavigateToLogin = {
                        navController.popBackStack()
                    },
                )
            }

            composable<BookWormRoute.Login> {
                val loginViewModel = koinViewModel<LoginViewModel>()
                val loginState by loginViewModel.state.collectAsStateWithLifecycle()
                LoginScreen(
                    state = loginState,
                    actions = loginViewModel.actions,
                    onSignIn = userViewModel.actions::loginUser,
                    themeState = themeState,
                    onNavigateToHome = {
                        navController.enterApp()
                    },
                    onNavigateToRegistration = {
                        navController.navigate(BookWormRoute.Registration) {
                            launchSingleTop = true
                        }
                    }
                )
            }
        }

        navigation<BookWormRoute.APP>(
            startDestination = BookWormRoute.Home
        ) {
            composable<BookWormRoute.Home> {
                LibraryScreen(
                    modifier = modifier,
                    navController,
                    state = libraryState,
                    books = libraryViewModel.filteredBooks.collectAsState(),
                    actions = libraryViewModel.actions,
                    onBookClick = { bookId ->
                        navController.navigate(BookWormRoute.BookDetails(bookId))
                    }
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
                    onNavigateUp = { navController.popBackStack() },
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
                val achievementViewModel: AchievementViewModel =
                    koinViewModel<AchievementViewModel>(parameters = {
                        parametersOf(userState.user.userId)
                    })

                val unlockedAchievementState by achievementViewModel.unlockedAchievementsState.collectAsStateWithLifecycle()
                val lockedAchievementsState by achievementViewModel.lockedAchievementsState.collectAsStateWithLifecycle()

                UserPageScreen(
                    navController,
                    userState = userState,
                    favourites = libraryViewModel.filteredBooks.collectAsState().value.filter { it.favourite },
                    unlockedAchievementState = unlockedAchievementState,
                    lockedAchievementsState = lockedAchievementsState,
                    onSeeFavourites = {
                        libraryViewModel.actions.filterByFavourites(true)
                        navController.popBackStack(BookWormRoute.Home, inclusive = false)
                    },
                    onBookClick = { bookId ->
                        navController.navigate(BookWormRoute.BookDetails(bookId))
                    }
                )
            }

            composable<BookWormRoute.Settings> {

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

                StatsScreen(
                    navController,
                    state,
                    statsVm.actions,
                )
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
                        navController.popBackStack()
                    }
                )
            }

            composable<BookWormRoute.Notifications> {
                NotificationsScreen(
                    navController = navController,
                    state = notificationsState,
                    actions = notificationsViewModel.actions,
                    onNavigateToUserPage = {
                        navController.navigateSingleTopTo(BookWormRoute.UserPage)
                    }
                )
            }
        }
    }
}

