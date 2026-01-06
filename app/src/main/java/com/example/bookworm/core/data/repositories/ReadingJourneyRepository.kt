package com.example.bookworm.core.data.repositories

import com.example.bookworm.core.data.database.daos.JourneyEntryDAOs
import com.example.bookworm.core.data.database.daos.ReadingJourneyDAOs
import com.example.bookworm.core.data.database.entities.JourneyEntryEntity
import com.example.bookworm.core.data.database.entities.ReadingJourneyEntity
import com.example.bookworm.core.data.database.relationships.ReadingJourneyWithEntries
import com.example.bookworm.core.data.evaluators.AchievementEvaluator
import com.example.bookworm.utils.TimeUtils
import kotlinx.coroutines.flow.Flow

class ReadingJourneyRepository (
    private val journeyDAO: ReadingJourneyDAOs,
    private val entryDAO: JourneyEntryDAOs,
    private val achievementEvaluator: AchievementEvaluator
) {

    suspend fun upsertJourney(bookId: Long, userId: Long, startDate: Long): Long {
        val journey = toJourney(bookId, userId, startDate)
        val journeyId = journeyDAO.upsertJourney(journey)
        return journeyId
    }

    suspend fun dropJourney(
        journeyId: Long,
        endDate: Long
    ) = journeyDAO.dropJourney(journeyId, endDate)

    suspend fun endJourney(
        journeyId: Long,
        endDate: Long,
        userId: Long
    )  {
        journeyDAO.endJourney(journeyId, endDate)
        achievementEvaluator.evaluateBookRead(userId)
    }

    fun observeJourneys (bookId: Long): Flow<List<ReadingJourneyWithEntries>> = journeyDAO.getJourneysWithEntries(bookId)

    fun observeJourney(journeyId: Long): Flow<ReadingJourneyWithEntries> = journeyDAO.getJourneyWithEntries(journeyId)

    suspend fun addEntry(entry: JourneyEntryEntity) = entryDAO.upsertEntry(entry)

    fun getAllJourneys(userId: Long): Flow<List<ReadingJourneyWithEntries>> =  journeyDAO.getAllJourneys(userId)

    fun getLastJourney(bookId: Long): Flow<ReadingJourneyWithEntries> = journeyDAO.getLastJourney(bookId)

    suspend fun deleteJourney(journey: ReadingJourneyEntity): Boolean {
        return try {
            journeyDAO.deleteJourney(journey)
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun countFinishedBooks(userId: Long): Int = journeyDAO.countFinishedBooks(userId)

    private fun toJourney(bookId: Long, userId: Long, startDate: Long): ReadingJourneyEntity {
        return ReadingJourneyEntity(
            journeyId = 0L,
            bookId = bookId,
            userId = userId,
            startDate = startDate,
            endDate = null,
            isDropped = false
        )
    }

}