package com.example.bookworm.ui.screens.adddiaryentry


import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookworm.core.data.database.entities.JourneyEntryEntity
import com.example.bookworm.core.data.database.relationships.ReadingJourneyWithEntries
import com.example.bookworm.core.data.models.AddBookResults
import com.example.bookworm.core.data.models.AddEntryResults
import com.example.bookworm.core.data.models.ReadingStatus
import com.example.bookworm.core.data.repositories.BookRepository
import com.example.bookworm.core.data.repositories.ReadingJourneyRepository
import com.example.bookworm.ui.BookWormRoute
import com.example.bookworm.utils.TimeUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AddDiaryEntryState(
    val date: Long = TimeUtils.now(),
    val pages: String = "",
    val comment: String = "",

    val bookId: Long = 0,
    val userId: Long = 0,
    val totalPages: Int = 0,
    val journey: ReadingJourneyWithEntries? = null,

    val showAlert: Boolean = false,
    val alertConfirmed: Boolean = false,
    val navDestination: BookWormRoute? = null,

    val pagesError: Boolean = false,
    val dateError: Boolean = false,
    val errorMessage: AddEntryResults = AddEntryResults.CannotSubmit,

    val showDatePicker: Boolean = false,
) {

    val canSubmit get() = pages.isNotBlank()
    val validPages get () = pages.toInt() >= 0

    fun toEntry(journeyId: Long): JourneyEntryEntity {
        return JourneyEntryEntity(
            entryId = 0L,
            date = date,
            pagesRead = pages.trim().toInt(),
            comment = comment.trim(),
            journeyId = journeyId,
            bookId = bookId
        )
    }
}

interface AddDiaryEntryActions {
    fun setDate(date: Long)
    fun setPages(pages: String)
    fun setComment(comment: String)
    fun setShowDatePicker(showDatePicker: Boolean)
    fun setBookInfo(bookId: Long)
    fun setUserId(userId: Long)
    fun setJourney()

    suspend fun addEntry()

    fun setShowAlert(value: Boolean)
    fun setAlertConfirmed(value: Boolean)
    fun setNavDestination(route: BookWormRoute?)

    fun setPagesError(value: Boolean)
    fun setDateError(value: Boolean)
    fun setErrorMessage(errorMessage: AddEntryResults)

}

class AddDiaryEntryViewModel(
    private val bookId: Long,
    private val userId: Long,
    private val bookRepository: BookRepository,
    private val journeyRepository: ReadingJourneyRepository
) : ViewModel() {
    private val _state = MutableStateFlow(
        AddDiaryEntryState(
        )
    )

    val state = _state.asStateFlow()

    val actions = object : AddDiaryEntryActions {

        override fun setBookInfo(bookId: Long) {
            _state.update { it.copy(bookId = bookId) }
            loadTotalPages(bookId)
        }

        override fun setUserId(userId: Long) {
            _state.update { it.copy(userId = userId) }
        }

        override fun setDate(date: Long) {
            _state.update { it.copy(date = date) }
        }

        override fun setPages(pages: String) {
            _state.update { it.copy(pages = pages) }
        }

        override fun setComment(comment: String) {
            _state.update { it.copy(comment = comment) }
        }

        override fun setShowDatePicker(showDatePicker: Boolean) {
            _state.update { it.copy(showDatePicker = showDatePicker) }
        }

        override fun setJourney() {
            viewModelScope.launch {
                journeyRepository.getLastJourney(bookId).collect { journey ->
                    _state.update { it.copy(journey = journey) }
                }
            }
        }

        override suspend fun addEntry(){
            if (!checkFields()) {
                return
            }

            viewModelScope.launch {
                val currentJourney = _state.value.journey

                // No journey or journey already ended → create new
                val journeyId = currentJourney
                    ?.takeIf { it.journey.endDate == null }
                    ?.journey
                    ?.journeyId
                    ?: run {
                        // Create a new Journey and mark book as READING
                        val newJourneyId =
                            journeyRepository.upsertJourney(bookId, userId, _state.value.date)
                        bookRepository.updateBookStatus(bookId, ReadingStatus.READING)
                        newJourneyId
                    }

                journeyRepository.addEntry(
                    _state.value.toEntry(journeyId)
                )

                /* If the user finished the book it is marked in the status ad FINISHED*/
                if (_state.value.pages.toInt() == _state.value.totalPages) {
                    bookRepository.updateBookStatus(bookId, ReadingStatus.FINISHED)
                    journeyRepository.endJourney(
                        journeyId = journeyId,
                        endDate = _state.value.date,
                        userId
                    )
                }

                val updatedJourney =
                    journeyRepository.observeJourney(journeyId).first()


                _state.update { it.copy(journey = updatedJourney) }
                Log.d("DEBUG LOG", "Fino qui arrivo")
                setErrorMessage(AddEntryResults.Success)
                Log.d("DEBUG LOG", "E anche fino a qui + ${_state.value.errorMessage}")
            }
        }

        override fun setShowAlert(value: Boolean) {
            _state.update { it.copy(showAlert = value) }
        }

        override fun setAlertConfirmed(value: Boolean) {
            _state.update { it.copy(alertConfirmed = value) }
        }

        override fun setNavDestination(route: BookWormRoute?) {
            _state.update { it.copy(navDestination = route) }
        }

        override fun setPagesError(value: Boolean) {
            _state.update { it.copy(pagesError = value) }
        }

        override fun setDateError(value: Boolean) {
            _state.update { it.copy(dateError = value) }
        }

        override fun setErrorMessage(errorMessage: AddEntryResults) {
            _state.update { it.copy(errorMessage = errorMessage) }
        }

    }

    init {
        actions.setBookInfo(bookId)
        actions.setUserId(userId = userId)
        actions.setJourney()
    }

    private fun loadTotalPages(bookId: Long) {
        viewModelScope.launch {
            val book = bookRepository
                .getBookById(bookId)
                .firstOrNull()

            if (book != null) {
                _state.update { it.copy(totalPages = book.pages) }
            }
        }
    }

    private fun checkFields(): Boolean {

        val state = _state.value
        val selectedDate = state.date
        val today = TimeUtils.now()
        val journey = state.journey
        val totalPages = state.totalPages
        val pages = state.pages.toInt()

        if (journey == null) {
            if (pages > totalPages){
                actions.setPagesError(true)
                actions.setErrorMessage(AddEntryResults.InvalidPage)
                return false
            }
            if( selectedDate > today) {
                actions.setDateError(true)
                actions.setErrorMessage(AddEntryResults.InvalidDate)
                return false
            } else {
                return true
            }
        } else {

            val startDate = journey.journey.startDate

            // If journey has no entries
            if (journey.entries.isEmpty()) {
                if (selectedDate >= startDate) {
                    return true
                } else {
                    actions.setDateError(true)
                    actions.setErrorMessage(AddEntryResults.InvalidDate)
                    return false
                }
            } else {
                // If journey has entries

                val lastPage = if (journey.journey.endDate == null) {
                    journey.entries.lastOrNull()?.pagesRead
                } else {
                    null
                }

                if (lastPage != null) {
                    val result = pages > lastPage
                    if (result) {
                        return true
                    } else {
                        actions.setPagesError(true)
                        actions.setErrorMessage(AddEntryResults.InvalidPage)
                        return false
                    }
                } else {
                    return true
                }
            }
        }
    }
}