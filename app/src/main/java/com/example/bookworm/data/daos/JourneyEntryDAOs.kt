package com.example.bookworm.data.daos

import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.bookworm.data.entities.JourneyEntryEntity
import com.example.bookworm.data.entities.ReadingJourneyEntity
import kotlinx.coroutines.flow.Flow

interface JourneyEntryDAOs {

    //get all journey's entry ordered from newest to oldest
    @Query("SELECT * FROM journey_entry WHERE journey_id = :journeyId ORDER BY date DESC")
    fun getAllEntries(journeyId: Long): Flow<List<JourneyEntryEntity>>

    @Upsert
    suspend fun upsertEntry(entry: JourneyEntryEntity): Long

    @Delete
    suspend fun deleteEntry(entry: JourneyEntryEntity)
}