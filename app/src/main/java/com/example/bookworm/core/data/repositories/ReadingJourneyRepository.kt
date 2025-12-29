package com.example.bookworm.core.data.repositories

import com.example.bookworm.core.data.database.daos.ReadingJourneyDAOs
import com.example.bookworm.core.data.database.entities.ReadingJourneyEntity
import kotlinx.coroutines.flow.Flow

class ReadingJourneyRepository (private val journeyDAO: ReadingJourneyDAOs) {

    suspend fun upsertJourney(journey: ReadingJourneyEntity): Long = journeyDAO.upsertJourney(journey)

    suspend fun deleteJourney(journey: ReadingJourneyEntity): Boolean {
        return try {
            journeyDAO.deleteJourney(journey)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getAllJourneys(bookId: Long): Flow<List<ReadingJourneyEntity?>> =  journeyDAO.getAllJourneys(bookId)

    suspend fun dropJourney(bookId: Long) = journeyDAO.dropJourney(bookId)

    suspend fun endJourney(bookId: Long) = journeyDAO.endJourney(bookId)

}