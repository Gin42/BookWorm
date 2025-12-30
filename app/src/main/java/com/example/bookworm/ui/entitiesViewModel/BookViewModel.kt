package com.example.bookworm.ui.entitiesViewModel

import com.example.bookworm.core.data.database.entities.BookEntity
import com.example.bookworm.core.data.models.ReadingStatus
import kotlinx.coroutines.Job

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