package com.example.bookworm.core.data.evaluators

import com.example.bookworm.core.data.database.daos.AchievementDAOs
import com.example.bookworm.core.data.database.daos.BookDAOs
import com.example.bookworm.core.data.database.daos.NotificationDAOs
import com.example.bookworm.core.data.database.daos.ReadingJourneyDAOs
import com.example.bookworm.core.data.database.entities.NotificationEntity
import com.example.bookworm.core.data.database.entities.UnlockedAchievementEntity
import com.example.bookworm.core.data.models.AchievementName
import com.example.bookworm.core.data.models.AchievementType
import com.example.bookworm.utils.TimeUtils

class AchievementEvaluator(
    private val bookDao: BookDAOs,
    private val journeyDao: ReadingJourneyDAOs,
    private val achievementDao: AchievementDAOs,
    private val notificationDAO: NotificationDAOs
) {

    suspend fun evaluateBookAdded(userId: Long) {
        val count = bookDao.countBooks(userId)

        AchievementName.entries
            .filter { it.type == AchievementType.BookAdded }
            .filter { count >= it.number }
            .forEach { unlock(userId, it) }
    }

    suspend fun evaluateBookRead(userId: Long) {
        val count = journeyDao.countFinishedBooks(userId)

        AchievementName.entries
            .filter { it.type == AchievementType.BookRead }
            .filter { count >= it.number }
            .forEach { unlock(userId, it) }
    }


    private suspend fun unlock(userId: Long, achievement: AchievementName) {
        val achievementId = achievementDao
            .getAchievementIdByName(achievement.name)
            ?: return

        val alreadyUnlocked = achievementDao.isAchievementUnlocked(
            userId = userId,
            achievementId = achievementId
        )

        if (alreadyUnlocked) return

        achievementDao.upsert(
            UnlockedAchievementEntity(
                achievementId = achievementId,
                userId = userId,
            )
        )
        notificationDAO.upsertNotification(
            NotificationEntity(
                notificationId = 0L,
                userId = userId,
                achievementId = achievementId,
                title = "You unlocked a NEW achievement:",
                body = achievement.name,
                sendTime = TimeUtils.now(),
                isRead = false
            )
        )
    }

}
