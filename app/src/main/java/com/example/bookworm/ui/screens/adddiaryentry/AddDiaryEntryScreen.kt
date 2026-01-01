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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookworm.ui.composables.AppBar
import com.example.bookworm.ui.composables.NavBottom
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun AddDiaryEntryScreen(
    navController: NavController,
    state: AddDiaryEntryState,
    actions: AddDiaryEntryActions
) {
    Scaffold(
        topBar = { AppBar(navController, goBack = true) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    actions.addEntry()
                    navController.navigateUp()
                }
            ) {
                Icon(Icons.Outlined.Check, "Add Diary entry")
            }
        }
    ) { contentPadding ->

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(contentPadding)
                .padding(8.dp)
                .fillMaxSize()
        ) {
            Text(
                "New Diary entry",
                style = MaterialTheme.typography.titleLarge
            )
            //entry date
            OutlinedTextField(
                value = state.date?.let { convertMillisToDate(it) } ?: "",
                onValueChange = { },
                label = { Text("Date") },
                placeholder = { Text("DD/MM/YYYY") },
                trailingIcon = {
                    Icon(Icons.Default.DateRange, contentDescription = "Select date")
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
                    }
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
                onValueChange = actions::setPages,
                label = { Text("Pages read") },
                placeholder = { Text("Enter the page you arrived at") },
                modifier = Modifier
                    .fillMaxWidth(),
                maxLines = 1,
                textStyle = MaterialTheme.typography.bodyMedium
            )

            OutlinedTextField(
                value = state.comment,
                onValueChange = actions::setComment,
                label = { Text("Comment") },
                placeholder = { Text("Enter your comment here") },
                modifier = Modifier
                    .fillMaxWidth(),
                maxLines = 20,
                textStyle = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
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
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}
