package com.example.bookworm.core.data.repositories

import com.example.bookworm.core.data.database.daos.BookDAOs
import com.example.bookworm.core.data.database.entities.BookEntity
import com.example.bookworm.core.data.models.ReadingStatus
import kotlinx.coroutines.flow.Flow

class BookRepository(private val bookDAO: BookDAOs) {

    suspend fun addBook(book: BookEntity): Long = bookDAO.upsertBook(book)

    suspend fun deleteBook(book: BookEntity): Boolean {
        return try {
            bookDAO.deleteBook(book)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getBookById(bookId: Long): Flow<BookEntity?> = bookDAO.getBookById(bookId)

    fun getAllBooks(userId: Long): Flow<List<BookEntity>> = bookDAO.getAllBooks(userId)

    fun searchBook(searchString: String, userId: Long): Flow<List<BookEntity?>> = bookDAO.searchBook(searchString)

    fun getAllFavouriteBooks(userId: Long): Flow<List<BookEntity?>> = bookDAO.getAllFavouriteBooks(userId)

    fun getBooksByStatus(status: ReadingStatus, userId: Long): Flow<List<BookEntity?>> = bookDAO.getBooksByStatus(status, userId)

    fun toggleFavouriteBook(bookId: Long) = bookDAO.toggleFavouriteBook(bookId)

    fun updateBookStatus(bookId: Long, status: ReadingStatus) = bookDAO.updateBookStatus(bookId, status)

}