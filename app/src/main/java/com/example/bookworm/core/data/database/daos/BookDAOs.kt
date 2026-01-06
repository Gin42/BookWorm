package com.example.bookworm.core.data.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.bookworm.core.data.database.entities.BookEntity
import com.example.bookworm.core.data.models.ReadingStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDAOs {

    @Query("SELECT * FROM books WHERE book_id = :bookId ")
    fun getBookById(bookId: Long): Flow<BookEntity?>

    //get all user's books
    @Query("SELECT * FROM books WHERE user_id = :userId ")
    fun getAllBooks(userId: Long): Flow<List<BookEntity>>

    @Query ("UPDATE books SET favourite = NOT favourite WHERE book_id = :bookId ")
    suspend fun toggleFavouriteBook(bookId: Long)

    @Query("UPDATE books SET status = :status WHERE book_id = :bookId")
    suspend fun updateBookStatus(bookId: Long, status: ReadingStatus)

    @Query("SELECT * FROM books WHERE title = :title AND author = :author AND user_id = :userId")
    suspend fun checkValidBook(title: String, author: String, userId: Long): List<BookEntity?>

    @Query("SELECT COUNT(*) FROM books WHERE user_id = :userId")
    suspend fun countBooks(userId: Long): Int

    @Upsert
    suspend fun upsertBook(book: BookEntity): Long

    @Delete
    suspend fun deleteBook(book: BookEntity)
}