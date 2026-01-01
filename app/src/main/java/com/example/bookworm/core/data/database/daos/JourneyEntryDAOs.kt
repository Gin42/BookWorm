package com.example.bookworm.core.data.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.bookworm.core.data.database.entities.JourneyEntryEntity
import com.example.bookworm.core.data.database.relationships.ReadingJourneyWithEntries
import kotlinx.coroutines.flow.Flow

@Dao
interface JourneyEntryDAOs {

    //get all journey's entry ordered from newest to oldest
    @Query("SELECT * FROM journey_entry WHERE journey_id = :journeyId ORDER BY date DESC")
    fun getAllEntries(journeyId: Long): Flow<List<JourneyEntryEntity>>

    @Upsert
    suspend fun upsertEntry(entry: JourneyEntryEntity): Long

    @Delete
    suspend fun deleteEntry(entry: JourneyEntryEntity)
}