package com.example.bookworm.core.data.repositories

import android.content.ContentValues.TAG
import android.util.Log
import com.example.bookworm.core.data.database.daos.BookDAOs
import com.example.bookworm.core.data.database.entities.BookEntity
import com.example.bookworm.core.data.evaluators.AchievementEvaluator
import com.example.bookworm.core.data.models.AddBookResults
import com.example.bookworm.core.data.models.ReadingStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class BookRepository(
    private val bookDAO: BookDAOs,
    private val achievementEvaluator: AchievementEvaluator
) {

    suspend fun addBook(book: BookEntity): AddBookResults {
        if (book.bookId == 0L) {
            /** If the book does not exist */
            if (checkValidBook(book.title, book.author, book.userId)) {
                bookDAO.upsertBook(book)
                achievementEvaluator.evaluateBookAdded(book.userId)
                return AddBookResults.Success
            } else {
                return AddBookResults.BookPresent
            }
        } else {
            /** If the book does exist */
            bookDAO.upsertBook(book)
            return AddBookResults.Success
        }
    }

    suspend fun deleteBook(book: BookEntity): Boolean {
        return try {
            bookDAO.deleteBook(book)
            true
        } catch (e: Exception) {
            false
        }
    }

    private suspend fun checkValidBook(title: String, author: String, userId: Long): Boolean {
        val sameBook = bookDAO.checkValidBook(title, author, userId).firstOrNull()
        return sameBook == null
    }

    fun getBookById(bookId: Long): Flow<BookEntity?> = bookDAO.getBookById(bookId)

    fun getAllBooks(userId: Long): Flow<List<BookEntity>> = bookDAO.getAllBooks(userId)

    suspend fun toggleFavouriteBook(bookId: Long) = bookDAO.toggleFavouriteBook(bookId)

    suspend fun updateBookStatus(bookId: Long, status: ReadingStatus) =
        bookDAO.updateBookStatus(bookId, status)

    suspend fun countBooks(userId: Long): Int = bookDAO.countBooks(userId)

}