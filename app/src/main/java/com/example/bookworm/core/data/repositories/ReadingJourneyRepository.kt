package com.example.bookworm.core.data.repositories

import com.example.bookworm.core.data.database.daos.JourneyEntryDAOs
import com.example.bookworm.core.data.database.daos.ReadingJourneyDAOs
import com.example.bookworm.core.data.database.entities.JourneyEntryEntity
import com.example.bookworm.core.data.database.entities.ReadingJourneyEntity
import com.example.bookworm.core.data.database.relationships.ReadingJourneyWithEntries
import kotlinx.coroutines.flow.Flow

class ReadingJourneyRepository (
    private val journeyDAO: ReadingJourneyDAOs,
    private val entryDAO: JourneyEntryDAOs
) {

    suspend fun upsertJourney(journey: ReadingJourneyEntity): Long = journeyDAO.upsertJourney(journey)

    suspend fun deleteJourney(journey: ReadingJourneyEntity): Boolean {
        return try {
            journeyDAO.deleteJourney(journey)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun observeJourneys (bookId: Long): Flow<List<ReadingJourneyWithEntries>> = journeyDAO.getJourneysWithEntries(bookId)

    fun observeJourney(journeyId: Long): Flow<ReadingJourneyWithEntries> = journeyDAO.getJourneyWithEntries(journeyId)

    suspend fun addEntry(entry: JourneyEntryEntity) = entryDAO.upsertEntry(entry)

    fun getAllJourneys(bookId: Long): Flow<List<ReadingJourneyEntity?>> =  journeyDAO.getAllJourneys(bookId)

    fun getLastJourney(bookId: Long): Flow<ReadingJourneyWithEntries> = journeyDAO.getLastJourney(bookId)

    suspend fun dropJourney(
        journeyId: Long,
        endDate: Long
    ) = journeyDAO.dropJourney(journeyId, endDate)

    suspend fun endJourney(
        journeyId: Long,
        endDate: Long
    ) = journeyDAO.endJourney(journeyId, endDate)

}