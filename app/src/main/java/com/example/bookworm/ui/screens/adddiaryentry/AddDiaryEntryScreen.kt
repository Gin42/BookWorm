package com.example.bookworm.ui.screens.adddiaryentry

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookworm.R
import com.example.bookworm.core.data.models.AddEntryResults
import com.example.bookworm.ui.BookWormRoute
import com.example.bookworm.utils.TimeUtils.convertMillisToDate
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDiaryEntryScreen(
    navController: NavController,
    state: AddDiaryEntryState,
    actions: AddDiaryEntryActions,
    onNavigateUp: () -> Unit
) {

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
                        actions.setShowAlert(true)
                        actions.setNavDestination(BookWormRoute.BookDetails(state.bookId))
                    }) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = stringResource(R.string.go_back_icon_desc))
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            actions.setShowAlert(true)
                            actions.setNavDestination(BookWormRoute.Settings)
                        }
                    ) {
                        Icon(Icons.Filled.Settings, contentDescription = stringResource(R.string.settings_icon_desc))
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (state.canSubmit) {
                        if (state.validPages) {
                            coroutineScope.launch {
                                val result = actions.addEntry()
                                if (result == AddEntryResults.Success) {
                                    onNavigateUp()
                                }
                            }
                        } else {
                            actions.setPagesError(true)
                            actions.setErrorMessage(AddEntryResults.InvalidPage)
                        }
                    } else {
                        actions.setErrorMessage(AddEntryResults.CannotSubmit)
                        actions.setPagesError(true)
                        actions.setDateError(true)
                    }
                }
            ) {
                Icon(Icons.Outlined.Check, stringResource(R.string.confirm_icon_desc))
            }
        }
    ) { contentPadding ->

        if (state.showAlert) {
            EntryAlert(actions)
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
                stringResource(R.string.add_entry_message),
                style = MaterialTheme.typography.titleLarge
            )
            //entry date
            OutlinedTextField(
                value = convertMillisToDate(state.date),
                onValueChange = {
                    actions.setDateError(false)
                    actions.setPagesError(false)
                },
                label = { Text(stringResource(R.string.date_label)) },
                placeholder = { Text(stringResource(R.string.date_placeholder)) },
                trailingIcon = {
                    Icon(Icons.Default.DateRange, contentDescription = stringResource(R.string.date_picker_icon_desc))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(state.date) {
                        awaitEachGesture {
                            awaitFirstDown(pass = PointerEventPass.Initial)
                            val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                            if (upEvent != null) run {
                                (actions::setShowDatePicker)(true)
                            }
                        }
                    },
                supportingText = {
                    if (state.dateError) {
                        when (state.errorMessage) {
                            AddEntryResults.CannotSubmit -> {
                                Text(stringResource(R.string.fill_all_fields_error))
                            }

                            AddEntryResults.InvalidDate ->  {
                                Text(stringResource(R.string.invalid_date_error))
                            }
                            else -> {
                            }
                        }
                    }
                },
                isError = state.dateError
            )

            if (state.showDatePicker) {
                DatePickerModal(
                    onDateSelected = {
                        if (it != null) {
                            (actions::setDate)(it)
                        }
                    },
                    onDismiss = { (actions::setShowDatePicker)(false) }
                )
            }

            OutlinedTextField(
                value = state.pages,
                onValueChange = {
                    actions.setPages(it)
                    actions.setDateError(false)
                    actions.setPagesError(false)
                },
                label = { Text(stringResource(R.string.pages_read_label)) },
                placeholder = { Text(stringResource(R.string.pages_read_placeholder)) },
                modifier = Modifier
                    .fillMaxWidth(),
                maxLines = 1,
                textStyle = MaterialTheme.typography.bodyMedium,
                supportingText = {
                    if (state.pagesError) {
                        when (state.errorMessage) {
                            AddEntryResults.InvalidPage -> {
                                Text(stringResource(R.string.invalid_pages_error))
                            }
                            AddEntryResults.CannotSubmit -> {
                                Text(stringResource(R.string.fill_all_fields_error))
                            }
                            else -> {

                            }
                        }
                    }
                },
                isError = state.pagesError
            )

            OutlinedTextField(
                value = state.comment,
                onValueChange = actions::setComment,
                label = { Text(stringResource(R.string.comment_label)) },
                placeholder = { Text(stringResource(R.string.comment_placeholder)) },
                modifier = Modifier
                    .fillMaxWidth(),
                maxLines = 20,
                textStyle = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text(stringResource(R.string.date_picker_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.date_picker_cancel))
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun EntryAlert(actions: AddDiaryEntryActions) {
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
