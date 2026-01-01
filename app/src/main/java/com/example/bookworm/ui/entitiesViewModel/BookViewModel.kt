package com.example.bookworm.ui.entitiesViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookworm.core.data.database.entities.BookEntity
import com.example.bookworm.core.data.models.ReadingStatus
import com.example.bookworm.core.data.repositories.BookRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class BookState(
    val books: List<BookEntity>
)

interface BookActions {
    fun addBook(book: BookEntity): Job
    fun deleteBook(book: BookEntity): Job

    fun getAllBooks(userId: Long)

    /*
        *  fun searchBook(searchString: String, userId: Long)
        *  fun updateBookStatus(bookId: Long, status: ReadingStatus)
        * */
    fun getBooksWithStatus(userId: Long, status: ReadingStatus)

    fun toggleFavourite(bookId: Long, userId: Long): Job

}

class BookViewModel(
    private val userId: Long,
    private val repository: BookRepository
) : ViewModel() {

    private val _state = MutableStateFlow(BookState(emptyList()))
    val state: StateFlow<BookState> = _state
        .onStart { actions.getAllBooks(userId) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            BookState(emptyList())
        )

    val actions = object : BookActions {

        override fun addBook(book: BookEntity) = viewModelScope.launch {
            repository.addBook(book)
        }

        override fun deleteBook(book: BookEntity) = viewModelScope.launch {
            repository.deleteBook(book)
        }

        override fun getAllBooks(userId: Long) {
            viewModelScope.launch {
                repository.getAllBooks(userId)
                    .collect { books ->
                        _state.value = BookState(books)
                    }
            }
        }

        override fun getBooksWithStatus(userId: Long, status: ReadingStatus) {
            TODO("Not yet implemented")
        }

        override fun toggleFavourite(bookId: Long, userId: Long): Job {
            TODO("Not yet implemented")
        }

    }
}