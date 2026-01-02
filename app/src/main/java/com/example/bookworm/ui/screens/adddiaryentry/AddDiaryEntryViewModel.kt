package com.example.bookworm.ui.screens.adddiaryentry


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookworm.core.data.database.entities.JourneyEntryEntity
import com.example.bookworm.core.data.database.entities.ReadingJourneyEntity
import com.example.bookworm.core.data.database.relationships.ReadingJourneyWithEntries
import com.example.bookworm.core.data.models.ReadingStatus
import com.example.bookworm.core.data.repositories.BookRepository
import com.example.bookworm.core.data.repositories.ReadingJourneyRepository
import com.example.bookworm.ui.utils.TimeUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId

data class AddDiaryEntryState(
    val date: Long = TimeUtils.startOfDay(TimeUtils.now()),
    val pages: String = "",
    val comment: String = "",


    val bookId: Long = 0,
    val userId: Long = 0,
    val totalPages: Int = 0,
    val journey: ReadingJourneyWithEntries? = null,

    val showDatePicker: Boolean = false,
) {

    fun toJourney(): ReadingJourneyEntity {
        val normalizedDate = TimeUtils.startOfDay(date)

        return ReadingJourneyEntity(
            journeyId = 0L,
            bookId = bookId,
            userId = userId,
            startDate = normalizedDate,
            endDate = null,
            isDropped = false
        )
    }

    fun toEntry(journeyId: Long): JourneyEntryEntity {
        return JourneyEntryEntity(
            entryId = 0L,
            date = date,
            pagesRead = pages.toInt(),
            comment = comment,
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


    fun checkFields(): Boolean
    fun addEntry()

}

class AddDiaryEntryViewModel(
    bookId: Long,
    userId: Long,
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
                val journey = journeyRepository.getLastJourney(_state.value.bookId).firstOrNull()

                if (journey != null) {
                    _state.update { it.copy(journey = journey) }
                }
            }
        }

        override fun checkFields(): Boolean {
            val selectedDateMillis = _state.value.date

            val todayMillis = LocalDate.now()
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()

            val lastDate = _state.value.journey?.entries?.last()?.date
            val lastPage = _state.value.journey?.entries?.last()?.pagesRead

            if (lastDate != null && lastPage != null) {
                return selectedDateMillis <= todayMillis &&
                        selectedDateMillis > lastDate &&
                        _state.value.pages.toInt() <= _state.value.totalPages &&
                        _state.value.pages.toInt() > lastPage
            } else {
                return selectedDateMillis <= todayMillis &&
                        _state.value.pages.toInt() <= _state.value.totalPages
            }
        }

        override fun addEntry() {
            if (!checkFields()) return

            viewModelScope.launch {
                val currentJourney = _state.value.journey

                // No journey or journey already ended → create new
                val journeyId = currentJourney
                    ?.takeIf { it.journey.endDate == null }
                    ?.journey
                    ?.journeyId
                    ?: run {
                        // Create a new Journey and mark book as READING
                        val newJourneyId = journeyRepository.upsertJourney(
                            _state.value.toJourney()
                        )
                        bookRepository.updateBookStatus(bookId, ReadingStatus.READING)
                        newJourneyId
                    }

                /* If the user finished the book it is marked in the status ad FINISHED*/
                journeyRepository.addEntry(
                    _state.value.toEntry(journeyId)
                )

                if(_state.value.pages.toInt() == _state.value.totalPages ) {
                    bookRepository.updateBookStatus(bookId, ReadingStatus.FINISHED)
                    journeyRepository.endJourney(
                        journeyId = journeyId,
                        endDate = TimeUtils.now()
                    )
                }

                val updatedJourney =
                    journeyRepository.observeJourney(journeyId).first()

                _state.update { it.copy(journey = updatedJourney) }
            }
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
}
