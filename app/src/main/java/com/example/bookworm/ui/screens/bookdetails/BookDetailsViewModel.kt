package com.example.bookworm.ui.screens.bookdetails

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


data class BookDetailsState(
    val statusExpanded: Boolean = false,
    val readingStatus: ReadingStatus = ReadingStatus.PLAN_TO_READ,
    val journeyExpanded: List<Boolean> = List(NUMBER_OF_ENTRIES) { false },
    val entryExpanded: List<Boolean> = List(NUMBER_OF_ENTRIES) { false },
)

enum class ReadingStatus {
    READING,
    FINISHED,
    PLAN_TO_READ,
    DROPPED
}

const val NUMBER_OF_ENTRIES = 5

interface BookDetailsAction {
    fun toggleStatusExpanded(statusExpanded: Boolean)
    fun setReadingStatus(readingStatus: ReadingStatus)
    fun toggleJourneyEntry(index: Int)
    fun openEntry(index: Int, value: Boolean)
}

class BookDetailsViewModel: ViewModel() {
    private val _state = MutableStateFlow(BookDetailsState())
    val state = _state.asStateFlow()

    val actions = object : BookDetailsAction {
        override fun toggleStatusExpanded(statusExpanded: Boolean) {
            _state.update { it.copy(statusExpanded = statusExpanded) }
        }

        override fun setReadingStatus(readingStatus: ReadingStatus) {
            _state.update { it.copy(readingStatus = readingStatus) }
        }

        override fun toggleJourneyEntry(index: Int) {
            val currentExpandedState = _state.value.journeyExpanded.toMutableList()
            currentExpandedState[index] = !currentExpandedState[index]
            _state.update { it.copy(journeyExpanded = currentExpandedState) }
        }

        override fun openEntry(index: Int, value:Boolean) {
            val currentExpandedState = _state.value.entryExpanded.toMutableList()
            currentExpandedState[index] = value
            _state.update { it.copy(entryExpanded = currentExpandedState) }
        }
    }
}