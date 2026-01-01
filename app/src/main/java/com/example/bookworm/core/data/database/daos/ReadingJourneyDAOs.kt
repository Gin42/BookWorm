package com.example.bookworm.core.data.database.daos

import androidx.core.location.LocationRequestCompat.Quality
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.bookworm.core.data.database.entities.ReadingJourneyEntity
import com.example.bookworm.core.data.database.relationships.ReadingJourneyWithEntries
import kotlinx.coroutines.flow.Flow


@Dao
interface ReadingJourneyDAOs {

    //get all book's journeys ordered from newest to oldest
    @Query("SELECT * FROM reading_journey WHERE book_id = :bookId ORDER BY start_date DESC")
    fun getAllJourneys(bookId: Long): Flow<List<ReadingJourneyEntity>>

    @Transaction
    @Query("SELECT * FROM reading_journey WHERE book_id= :bookId ORDER BY start_date DESC")
    fun getJourneysWithEntries (bookId: Long): Flow<List<ReadingJourneyWithEntries>>

    @Transaction
    @Query("SELECT * FROM reading_journey WHERE journey_id = :journeyId")
    fun getJourneyWithEntries(journeyId: Long): Flow<ReadingJourneyWithEntries>

    @Transaction
    @Query("SELECT * FROM reading_journey WHERE book_id= :bookId ORDER BY start_date DESC LIMIT 1")
    fun getLastJourney(bookId: Long): Flow<ReadingJourneyWithEntries>

    //drop journey
    @Query("UPDATE reading_journey SET is_dropped = 1 WHERE journey_id = :journeyId")
    suspend fun dropJourney(journeyId: Long)

    //finish journey
    @Query("UPDATE reading_journey SET end_date = CURRENT_TIMESTAMP WHERE journey_id = :journeyId")
    suspend fun endJourney(journeyId: Long)

    @Upsert
    suspend fun upsertJourney(journey: ReadingJourneyEntity): Long

    @Delete
    suspend fun deleteJourney(journey: ReadingJourneyEntity)
}