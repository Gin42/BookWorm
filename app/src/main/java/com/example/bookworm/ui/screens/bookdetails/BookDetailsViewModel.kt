package com.example.bookworm.ui.screens.bookdetails

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookworm.core.data.database.entities.BookEntity
import com.example.bookworm.core.data.database.entities.ReadingJourneyEntity
import com.example.bookworm.core.data.models.ReadingStatus
import com.example.bookworm.core.data.repositories.BookRepository
import com.example.bookworm.core.data.repositories.ReadingJourneyRepository
import com.example.bookworm.ui.entitiesViewModel.BookState
import com.example.bookworm.ui.entitiesViewModel.LoggedUserState
import com.example.bookworm.ui.mapper.toUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class BookDetailsState(
    var selectedBook: BookEntity,
    var bookJourneys: List<Journey> = emptyList(),


    val statusExpanded: Boolean = false,
    val journeyExpanded: List<Boolean> = List(NUMBER_OF_ENTRIES) { false },
    val entryExpanded: List<Boolean> = List(NUMBER_OF_ENTRIES) { false },
)

data class Journey(
    val journeyId: Long,
    val bookId: Long,
    val startDate: Long,
    val endDate: Long?,
    val entries: List<Entry>
)

data class Entry (
    val entryId: Long,
    val date: Long,
    val pagesRead: Int,
    val comment: String?
)


const val NUMBER_OF_ENTRIES = 5

interface BookDetailsAction {
    fun updateReadingStatus(status: ReadingStatus)
    fun updateFavourite()

    fun toggleStatusExpanded(statusExpanded: Boolean)
    fun toggleJourneyEntry(index: Int)
    fun openEntry(index: Int, value: Boolean)
}

class BookDetailsViewModel(
    bookId: Long,
    private val bookRepository: BookRepository,
    private val journeyRepository: ReadingJourneyRepository,
) : ViewModel() {
    private val _state: MutableStateFlow<BookDetailsState> = MutableStateFlow(
        BookDetailsState(
            selectedBook = BookEntity(),
            bookJourneys = emptyList()
        )
    )

    val state = _state.asStateFlow()

    val actions = object : BookDetailsAction {

        override fun updateReadingStatus(status: ReadingStatus) {
            viewModelScope.launch {
                bookRepository.updateBookStatus(bookId, status)
            }
        }

        override fun updateFavourite() {
            viewModelScope.launch {
                bookRepository.toggleFavouriteBook(bookId)
            }
        }

        override fun toggleStatusExpanded(statusExpanded: Boolean) {
            _state.update { it.copy(statusExpanded = statusExpanded) }
        }


        override fun toggleJourneyEntry(index: Int) {
            val currentExpandedState = _state.value.journeyExpanded.toMutableList()
            currentExpandedState[index] = !currentExpandedState[index]
            _state.update { it.copy(journeyExpanded = currentExpandedState) }
        }

        override fun openEntry(index: Int, value: Boolean) {
            val currentExpandedState = _state.value.entryExpanded.toMutableList()
            currentExpandedState[index] = value
            _state.update { it.copy(entryExpanded = currentExpandedState) }
        }
    }

    init {
        // Collect the selected book
        viewModelScope.launch {
            bookRepository.getBookById(bookId)
                .collect { book ->
                    book?.let {
                        _state.update { it.copy(selectedBook = book) }
                    }
                }
        }

        // Collect the journeys
        viewModelScope.launch {
            journeyRepository.observeJourneys(bookId)
                .map { journeys ->
                    journeys.map { it.toUi() }
                }
                .collect { journeyList ->
                    _state.update { it.copy(bookJourneys = journeyList) }
                }
        }
    }
}

