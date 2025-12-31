package com.example.bookworm.ui.screens.bookdetails

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookworm.core.data.database.entities.BookEntity
import com.example.bookworm.core.data.models.ReadingStatus
import com.example.bookworm.core.data.repositories.BookRepository
import com.example.bookworm.ui.entitiesViewModel.BookState
import com.example.bookworm.ui.entitiesViewModel.LoggedUserState
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
    var favourite: Boolean = false,
    var status: ReadingStatus = ReadingStatus.PLAN_TO_READ,

    val statusExpanded: Boolean = false,
    val journeyExpanded: List<Boolean> = List(NUMBER_OF_ENTRIES) { false },
    val entryExpanded: List<Boolean> = List(NUMBER_OF_ENTRIES) { false },
)


const val NUMBER_OF_ENTRIES = 5

interface BookDetailsAction {
    fun setBook(bookId: Long)
    fun updateReadingStatus(status: ReadingStatus)
    fun updateFavourite()

    fun toggleStatusExpanded(statusExpanded: Boolean)
    fun toggleJourneyEntry(index: Int)
    fun openEntry(index: Int, value: Boolean)
}

class BookDetailsViewModel(
    private val bookId: Long,
    private val repository: BookRepository
) : ViewModel() {
    private val _state: MutableStateFlow<BookDetailsState> = MutableStateFlow(
        BookDetailsState(
            BookEntity(),
        )
    )
    val state: StateFlow<BookDetailsState>
        get() = _state.onStart { actions.setBook(bookId) }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L), BookDetailsState(
                BookEntity(),
            )
        )

    val actions = object : BookDetailsAction {

        override fun setBook(bookId: Long) {
            viewModelScope.launch {
                repository.getBookById(bookId).collect { bookEntity ->
                    if (bookEntity != null) {
                        _state.update {
                            it.copy(
                                selectedBook = bookEntity,
                                favourite = bookEntity.favourite,
                                status = bookEntity.status
                            )
                        }
                    }
                }
            }
        }

        override fun updateReadingStatus(status: ReadingStatus) {
            val bookId = _state.value.selectedBook.bookId
            viewModelScope.launch {
                repository.updateBookStatus(bookId, status)
            }
        }

        override fun updateFavourite() {
            val bookId = _state.value.selectedBook.bookId
            viewModelScope.launch {
                repository.toggleFavouriteBook(bookId)
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
}