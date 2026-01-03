package com.example.bookworm.ui.screens.home

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.bookworm.core.data.database.entities.BookEntity
import com.example.bookworm.ui.entitiesViewModel.BookState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class LibraryState(
    val query: String = "",
    val onSearch: String? = null,
    val filteredBooks: List<BookEntity> = emptyList()
)

interface LibraryActions {
    fun setQuery(query: String)
    fun setOnSearch(onSearch: String?)
}

class LibraryViewModel(
    private val bookState: BookState
) : ViewModel() {

    private val _state: MutableStateFlow<LibraryState> = MutableStateFlow(
        LibraryState()
    )
    val state = _state.asStateFlow()

    val actions = object : LibraryActions {
        override fun setQuery(query: String) {
            _state.update { it.copy(query = query) }
            setFilteredBooksByQuery()
        }

        override fun setOnSearch(onSearch: String?) {
            _state.update { it.copy(onSearch = onSearch) }
        }

    }

    init {
        setFilteredBooksByQuery()
    }

    private fun setFilteredBooksByQuery() {
        val lowerQuery = _state.value.query.trim().lowercase()
        _state.update {
            it.copy(
                filteredBooks = if (lowerQuery.isNotBlank()) {
                    bookState.books.filter { book ->
                        book.title.lowercase().contains(lowerQuery) ||
                                book.author.lowercase().contains(lowerQuery)
                    }
                } else {
                    bookState.books
                }
            )
        }
        Log.d(TAG, "HEYLA: ${_state.value.filteredBooks} +  ${_state.value.query} + ${bookState.books}")
    }
}

