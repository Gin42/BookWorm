package com.example.bookworm.core.data.repositories

import androidx.room.Upsert
import com.example.bookworm.core.data.database.daos.JourneyEntryDAOs
import com.example.bookworm.core.data.database.entities.JourneyEntryEntity
import com.example.bookworm.core.data.database.relationships.ReadingJourneyWithEntries
import kotlinx.coroutines.flow.Flow

class JourneyEntryRepository(private val entryDAO: JourneyEntryDAOs) {

    suspend fun upsertEntry(entry: JourneyEntryEntity): Long = entryDAO.upsertEntry(entry)

    suspend fun deleteEntry(entry: JourneyEntryEntity): Boolean {
        return try {
            entryDAO.deleteEntry(entry)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getAllEntries(journeyId: Long): Flow<List<JourneyEntryEntity>> = entryDAO.getAllEntries(journeyId)
}