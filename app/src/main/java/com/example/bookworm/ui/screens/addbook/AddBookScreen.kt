package com.example.bookworm.ui.screens.addbook

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.FilledIconButton
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookworm.R
import com.example.bookworm.core.data.database.entities.BookEntity
import com.example.bookworm.core.data.models.AddBookResults
import com.example.bookworm.ui.BookWormRoute
import com.example.bookworm.ui.composables.ImagePickerBottomSheet
import com.example.bookworm.ui.composables.ImageWithPlaceholder
import com.example.bookworm.ui.composables.Size
import com.example.bookworm.ui.screens.adddiaryentry.EntryAlert
import kotlinx.coroutines.launch
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

    val coroutineScope = rememberCoroutineScope()

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
                        actions.setNavDestination(null)
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
                            actions.setNavDestination(null)
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

                            coroutineScope.launch {
                                val result = addBook(state.toBook())
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
            BookAlert(onConfirm = {
                actions.setShowAlert(false)
                if (state.navDestination == BookWormRoute.Home) {
                    onNavigateUp()
                } else if (state.navDestination != null) {
                    navController.navigate(state.navDestination) {
                        popUpTo(BookWormRoute.AddBook(bookId)) {
                            inclusive = true
                        }
                    }
                }
            }, onDismiss = {
                actions.setShowAlert(false)
                actions.setNavDestination(null)
            })
        }

        BackHandler(enabled = true) {
            actions.setNavDestination(null)
            actions.setShowAlert(true)
            actions.setNavDestination(BookWormRoute.Home)
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .padding(contentPadding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                if (bookId == null) {
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
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
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
                    } else {
                        Text(stringResource(R.string.required_field_message))
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
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
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
                    } else {
                        Text(stringResource(R.string.required_field_message))
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
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword,
                    imeAction = ImeAction.Next
                ),
                visualTransformation = VisualTransformation.None,
                modifier = Modifier
                    .fillMaxWidth(),
                maxLines = 1,
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
                    } else {
                        Text(stringResource(R.string.required_field_message))
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
                FilledIconButton(
                    onClick = { actions.setPickerVisible(true) },
                ) {
                    Icon(
                        Icons.Outlined.Add,
                        contentDescription = stringResource(R.string.add_image_icon_desc),
                    )
                }
            }
        }
    }
}

@Composable
fun BookAlert(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        title = { Text(text = stringResource(R.string.exit_warning_alert_title)) },
        text = { Text(text = stringResource(R.string.exit_warning_alert_body)) },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
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
                    onConfirm()
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