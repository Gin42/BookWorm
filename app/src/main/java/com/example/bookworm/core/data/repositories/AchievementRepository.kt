package com.example.bookworm.core.data.repositories

import com.example.bookworm.core.data.database.daos.AchievementDAOs
import com.example.bookworm.core.data.database.entities.AchievementEntity
import com.example.bookworm.core.data.database.entities.UnlockedAchievementEntity
import kotlinx.coroutines.flow.Flow

class AchievementRepository(
    private val achievementDAO: AchievementDAOs
) {
    suspend fun unlockAchievement(userId: Long, achievementId: Long) =
        achievementDAO.upsert(UnlockedAchievementEntity(userId, achievementId))

    fun getAllUserAchievements(userId: Long): Flow<List<AchievementEntity>> =
        achievementDAO.getAllUserAchievements(userId)

    fun getNotUnlockedAchievements(userId: Long): Flow<List<AchievementEntity>> =
        achievementDAO.getNotUnlockedAchievements(userId)
}