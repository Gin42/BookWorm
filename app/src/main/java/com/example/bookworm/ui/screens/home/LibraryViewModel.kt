package com.example.bookworm.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookworm.core.data.database.entities.BookEntity
import com.example.bookworm.core.data.models.AddBookResults
import com.example.bookworm.core.data.models.ReadingStatus
import com.example.bookworm.core.data.repositories.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

data class LibraryState(
    val query: String = "",

    val openFilters: Boolean = false,
    val selectedStatus: ReadingStatus? = null,
    val favouritesOnly: Boolean = false,
    val countFiltersActive: Int = 0
)

interface LibraryActions {
    suspend fun addBook(book: BookEntity): AddBookResults
    fun openFilters(value: Boolean)

    fun setQuery(query: String)
    fun filterByFavourites(enabled: Boolean)
    fun filterByStatus(status: ReadingStatus?)
}

class LibraryViewModel(
    private val repository: BookRepository,
    userId: Long
) : ViewModel() {

    private val _state: MutableStateFlow<LibraryState> = MutableStateFlow(
        LibraryState()
    )
    val state = _state.asStateFlow()

    private val books: StateFlow<List<BookEntity>> =
        repository.getAllBooks(userId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    val filteredBooks: StateFlow<List<BookEntity>> =
        combine(
            books,
            state
        ) { books, state ->

            books
                .filterByQuery(state.query)
                .filterByFavourites(state.favouritesOnly)
                .filterByStatus(state.selectedStatus)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )


    val actions = object : LibraryActions {


        override fun setQuery(query: String) {
            _state.update { it.copy(query = query) }
        }

        override fun filterByFavourites(enabled: Boolean) {
            _state.update { it.copy(favouritesOnly = enabled) }
            setFiltersActive()
        }

        override fun filterByStatus(status: ReadingStatus?) {
            _state.update { it.copy(selectedStatus = status) }
            setFiltersActive()
        }

        override fun openFilters(value: Boolean) {
            _state.update { it.copy(openFilters = value) }
        }

        override suspend fun addBook(book: BookEntity): AddBookResults {
            return repository.addBook(book)
        }

    }

    private fun setFiltersActive() {
        var count = 0
        if (_state.value.favouritesOnly) {
            count += 1
        }
        if (_state.value.selectedStatus != null) {
            count += 1
        }
        _state.update { it.copy(countFiltersActive = count) }
    }

    private fun List<BookEntity>.filterByQuery(query: String): List<BookEntity> {
        if (query.isBlank()) return this

        val lower = query.trim().lowercase()
        return filter {
            it.title.lowercase().contains(lower) ||
                    it.author.lowercase().contains(lower)
        }
    }

    private fun List<BookEntity>.filterByFavourites(enabled: Boolean): List<BookEntity> {
        return if (enabled) filter { it.favourite } else this
    }

    private fun List<BookEntity>.filterByStatus(
        status: ReadingStatus?
    ): List<BookEntity> {
        return status?.let { s ->
            filter { it.status == s }
        } ?: this
    }
}

