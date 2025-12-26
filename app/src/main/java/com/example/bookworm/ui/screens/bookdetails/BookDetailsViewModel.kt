package com.example.bookworm.ui.screens.bookdetails

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


data class BookDetailsState(
    val statusExpanded: Boolean = false,
    val readingStatus: ReadingStatus = ReadingStatus.PLAN_TO_READ
)

enum class ReadingStatus {
    READING,
    FINISHED,
    PLAN_TO_READ,
    DROPPED
}


interface BookDetailsAction {
    fun setStatusExpanded(statusExpanded: Boolean)
    fun setReadingStatus(readingStatus: ReadingStatus)
}

class BookDetailsViewModel: ViewModel() {
    private val _state = MutableStateFlow(BookDetailsState())
    val state = _state.asStateFlow()

    val actions = object : BookDetailsAction {
        override fun setStatusExpanded(statusExpanded: Boolean) {
            _state.update { it.copy(statusExpanded = statusExpanded) }
        }

        override fun setReadingStatus(readingStatus: ReadingStatus) {
            _state.update { it.copy(readingStatus = readingStatus) }
        }
    }
}