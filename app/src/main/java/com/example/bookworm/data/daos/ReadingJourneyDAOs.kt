package com.example.bookworm.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.bookworm.data.entities.BookEntity
import com.example.bookworm.data.entities.JourneyEntryEntity
import com.example.bookworm.data.entities.ReadingJourneyEntity
import com.example.bookworm.data.models.ReadingStatus
import kotlinx.coroutines.flow.Flow


@Dao
interface ReadingJourneyDAOs {

    //get all book's journeys ordered from newest to oldest
    @Query("SELECT * FROM reading_journey WHERE book_id = :bookId ORDER BY start_date DESC")
    fun getAllJourneys(bookId: Long): Flow<List<ReadingJourneyEntity>>

    //drop journey
    @Query("UPDATE reading_journey SET is_dropped = 1 WHERE book_id = :bookId")
    suspend fun dropJourney(bookId: Long)

    //finish journey
    @Query("UPDATE reading_journey SET endDate = CURRENT_TIMESTAMP WHERE book_id = :bookId")
    suspend fun endJourney(bookId: Long)

    @Upsert
    suspend fun upsertJourney(journey: ReadingJourneyEntity): Long
}