package com.example.bookworm.ui.entitiesViewModel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookworm.core.data.database.entities.BookEntity
import com.example.bookworm.core.data.models.ReadingStatus
import com.example.bookworm.core.data.repositories.BookRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class BookState(
    val books: List<BookEntity>
)

interface BookActions {
    fun addBook(book: BookEntity): Job
    fun deleteBook(book: BookEntity): Job

    /* fun getAllBooks(userId: Long)
    *  fun getBookById(bookId: Long)
    *  fun searchBook(searchString: String, userId: Long)
    *  fun updateBookStatus(bookId: Long, status: ReadingStatus)
    * */
    fun getFavourites(userId: Long)
    fun getBooksWithStatus(userId: Long, status: ReadingStatus)

    fun toggleFavourite(bookId: Long, userId: Long) : Job

}

class BookViewModel(
    private val repository: BookRepository
) : ViewModel() {
    private var _state: MutableStateFlow<BookState> = MutableStateFlow(BookState(emptyList()))
    val state: StateFlow<BookState> get() = _state.asStateFlow()

    val actions = object : BookActions {

        override fun addBook(book: BookEntity) = viewModelScope.launch {
            repository.addBook(book)
        }

        override fun deleteBook(book: BookEntity)= viewModelScope.launch {
            repository.deleteBook(book)
        }

        override fun getFavourites(userId: Long) {
            TODO("Not yet implemented")
        }

        override fun getBooksWithStatus(userId: Long, status: ReadingStatus) {
            TODO("Not yet implemented")
        }

        override fun toggleFavourite(bookId: Long, userId: Long): Job {
            TODO("Not yet implemented")
        }

    }
}