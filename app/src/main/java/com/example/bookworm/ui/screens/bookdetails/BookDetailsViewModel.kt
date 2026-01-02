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
import com.example.bookworm.ui.utils.mapper.toUi
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
    var selectedBook: BookEntity = BookEntity(),
    var bookJourneys: List<Journey> = emptyList(),


    val statusExpanded: Boolean = false,
    val journeyExpanded: List<Boolean> = emptyList(),
    val entryExpanded: Map<Long, Boolean> = emptyMap(),
)

data class Journey(
    val journeyId: Long,
    val bookId: Long,
    val startDate: Long,
    val endDate: Long?,
    val entries: List<Entry>
)

data class Entry(
    val entryId: Long,
    val date: Long,
    val pagesRead: Int,
    val comment: String?
)

interface BookDetailsAction {
    fun updateReadingStatus(status: ReadingStatus)
    fun updateFavourite()

    fun toggleStatusExpanded(statusExpanded: Boolean)
    fun toggleJourneyEntry(index: Int)
    fun openEntry(entryId: Long, open: Boolean)
}

class BookDetailsViewModel(
    private val bookId: Long,
    private val bookRepository: BookRepository,
    private val journeyRepository: ReadingJourneyRepository,
) : ViewModel() {

    private val _state: MutableStateFlow<BookDetailsState> = MutableStateFlow(
        BookDetailsState()
    )
    val state = _state.asStateFlow()

    val actions = object : BookDetailsAction {

        override fun updateReadingStatus(status: ReadingStatus) {

            viewModelScope.launch {
                bookRepository.updateBookStatus(bookId, status)

                val currentJourney = currentJourney() ?: return@launch
                val now = System.currentTimeMillis()

                when (status) {
                    ReadingStatus.FINISHED -> {
                        journeyRepository.endJourney(
                            journeyId = currentJourney.journeyId,
                            endDate = now
                        )
                    }

                    ReadingStatus.DROPPED -> {
                        journeyRepository.dropJourney(
                            journeyId = currentJourney.journeyId,
                            endDate = now
                        )
                    }

                    else -> Unit
                }
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
            _state.update { state ->
                val expanded = state.journeyExpanded.toMutableList()
                if (index in expanded.indices) {
                    expanded[index] = !expanded[index]
                }
                state.copy(journeyExpanded = expanded)
            }
        }

        override fun openEntry(entryId: Long, open: Boolean) {
            _state.update { state ->
                state.copy(
                    entryExpanded = state.entryExpanded.toMutableMap().apply {
                        this[entryId] = open
                    }
                )
            }
        }
    }

    init {
        observeBook()
        observeJourneys()
    }

    private fun observeBook() {
        viewModelScope.launch {
            bookRepository.getBookById(bookId).collect { book ->
                book?.let {
                    _state.update { it.copy(selectedBook = book) }
                }
            }
        }
    }

    private fun observeJourneys() {
        viewModelScope.launch {
            journeyRepository.observeJourneys(bookId)
                .collect { journeys ->
                    val uiJourneys = journeys.map { it.toUi() }

                    _state.update {
                        it.copy(
                            bookJourneys = uiJourneys,
                            journeyExpanded = List(uiJourneys.size) { false }
                        )
                    }
                }
        }
    }

    private fun currentJourney(): Journey? {
        return _state.value.bookJourneys.lastOrNull { it.endDate == null }
    }
}

