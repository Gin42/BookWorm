package com.example.bookworm.ui.screens.addbook

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookworm.R
import com.example.bookworm.core.data.database.entities.BookEntity
import com.example.bookworm.core.data.models.AddBookResults
import com.example.bookworm.ui.BookWormRoute
import com.example.bookworm.ui.composables.ImagePickerBottomSheet
import com.example.bookworm.ui.composables.ImageWithPlaceholder
import com.example.bookworm.ui.composables.Size
import kotlinx.coroutines.runBlocking
import kotlin.reflect.KSuspendFunction1


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookScreen(
    navController: NavController,
    state: AddBookState,
    actions: AddBookActions,
    addBook: KSuspendFunction1<BookEntity, AddBookResults>,
    bookId: Long?,
    onNavigateUp: () -> Unit,
) {

    LaunchedEffect(bookId) {
        bookId?.let { actions.setBook(it) }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        fontFamily = FontFamily(Font(R.font.alegreya_sans_sc_medium)),
                        style = MaterialTheme.typography.headlineMedium,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        actions.setShowAlert(true)
                        actions.setNavDestination(BookWormRoute.Home)
                    }) {
                        Icon(
                            Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = stringResource(R.string.go_back_icon_desc)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            actions.setShowAlert(true)
                            actions.setNavDestination(BookWormRoute.Settings)
                        }
                    ) {
                        Icon(
                            Icons.Filled.Settings,
                            contentDescription = stringResource(R.string.settings_icon_desc)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                onClick = {
                    if (state.canSubmit) {

                        if (state.validPages) {

                            val result = runBlocking {
                                addBook(state.toBook())
                            }
                            when (result) {
                                AddBookResults.BookPresent -> {
                                    actions.setError(true)
                                    actions.setErrorMessage(AddBookResults.BookPresent)
                                }

                                AddBookResults.Success -> {
                                    onNavigateUp()
                                }

                                AddBookResults.CannotSubmit -> {
                                    actions.setError(true)
                                    actions.setPagesError(true)
                                    actions.setErrorMessage(AddBookResults.CannotSubmit)
                                }

                                AddBookResults.InvalidPages -> {}
                            }
                        } else {
                            actions.setPagesError(true)
                            actions.setErrorMessage(AddBookResults.InvalidPages)
                        }
                    } else {
                        actions.setError(true)
                        actions.setPagesError(true)
                        actions.setErrorMessage(AddBookResults.CannotSubmit)
                    }
                }
            ) {
                Icon(Icons.Outlined.Check, stringResource(R.string.confirm_icon_desc))
            }
        }
    ) { contentPadding ->

        if (state.showAlert) {
            BookAlert(actions)
        }

        LaunchedEffect(state.alertConfirmed) {
            if (state.alertConfirmed && state.navDestination != null) {
                navController.navigate(state.navDestination)
                actions.setAlertConfirmed(false)
                actions.setNavDestination(null)
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(contentPadding)
                .padding(8.dp)
                .fillMaxSize()
        ) {

            Text(
                if (bookId != null) {
                    stringResource(R.string.add_book_message)
                } else {
                    stringResource(R.string.modify_book_message)
                },
                style = MaterialTheme.typography.titleLarge
            )

            OutlinedTextField(
                value = state.title,
                onValueChange = {
                    actions.setTitle(it)
                    actions.setError(false)
                },
                label = { Text(stringResource(R.string.title_label)) },
                placeholder = { Text(stringResource(R.string.title_placeholder)) },
                modifier = Modifier
                    .fillMaxWidth(),
                maxLines = 1,
                textStyle = MaterialTheme.typography.bodyMedium,
                supportingText = {
                    if (state.error) {
                        when (state.errorMessage) {
                            AddBookResults.CannotSubmit -> {
                                Text(stringResource(R.string.fill_all_fields_error))
                            }

                            AddBookResults.BookPresent -> {
                                Text(stringResource(R.string.existent_book_error))
                            }
                            else -> {
                            }
                        }
                    }
                },
                isError = state.error
            )

            OutlinedTextField(
                value = state.author,
                onValueChange = {
                    actions.setAuthor(it)
                    actions.setError(false)
                },
                label = { Text(stringResource(R.string.author_label)) },
                placeholder = { Text(stringResource(R.string.author_placeholder)) },
                modifier = Modifier
                    .fillMaxWidth(),
                maxLines = 20,
                textStyle = MaterialTheme.typography.bodyMedium,
                supportingText = {
                    if (state.error) {
                        when (state.errorMessage) {
                            AddBookResults.CannotSubmit -> {
                                Text(stringResource(R.string.fill_all_fields_error))
                            }

                            AddBookResults.BookPresent -> {
                                Text(stringResource(R.string.existent_book_error))
                            }

                            else -> {
                            }
                        }
                    }
                },
                isError = state.error
            )

            OutlinedTextField(
                value = state.pages,
                onValueChange = {
                    actions.setPages(it)
                    actions.setError(false)
                    actions.setPagesError(false)
                },
                label = { Text(stringResource(R.string.tot_pages_label)) },
                placeholder = { Text(stringResource(R.string.tot_pages_placeholder)) },
                modifier = Modifier
                    .fillMaxWidth(),
                maxLines = 20,
                textStyle = MaterialTheme.typography.bodyMedium,
                supportingText = {
                    if (state.pagesError) {
                        when (state.errorMessage) {
                            AddBookResults.CannotSubmit -> {
                                Text(stringResource(R.string.fill_all_fields_error))
                            }

                            AddBookResults.InvalidPages -> {
                                Text(stringResource(R.string.invalid_pages_error))
                            }

                            else -> {
                            }
                        }
                    }
                },
                isError = state.pagesError
            )

            if (state.isImagePickerVisible) {
                ImagePickerBottomSheet(
                    onSelected = { image ->
                        actions.setCover(image)
                    },
                    onDismissRequest = {
                        actions.setPickerVisible(false)
                    }
                )
            }

            Box(
                contentAlignment = Alignment.BottomEnd
            ) {
                ImageWithPlaceholder(
                    state.bookCover, Size.Lg,
                    desc = stringResource(R.string.book_cover_desc),
                    CircleShape
                )
                Button(
                    onClick = { actions.setPickerVisible(true) },
                    shape = CircleShape,
                )  {
                    Icon(
                        Icons.Outlined.Add,
                        contentDescription = stringResource(R.string.add_image_icon_desc),
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                }
            }
        }
    }
}

@Composable
fun BookAlert(actions: AddBookActions) {
    AlertDialog(
        onDismissRequest = {
            actions.setShowAlert(false)
        },
        title = { Text(text = stringResource(R.string.exit_warning_alert_title)) },
        text = { Text(text = stringResource(R.string.exit_warning_alert_body)) },
        dismissButton = {
            TextButton(
                onClick = {
                    actions.setAlertConfirmed(false) //quindi continui a modificare
                    actions.setShowAlert(false)
                }
            ) {
                Text(
                    text = stringResource(R.string.exit_warning_alert_cancel_button),
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    actions.setAlertConfirmed(true)
                    actions.setShowAlert(false) //quindi esci e te ne freghi
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text(
                    text = stringResource(R.string.exit_warning_alert_confirm_button),
                    color = MaterialTheme.colorScheme.onError
                )
            }
        }
    )
}