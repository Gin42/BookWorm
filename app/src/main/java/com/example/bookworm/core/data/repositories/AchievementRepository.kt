package com.example.bookworm.core.data.repositories

import com.example.bookworm.core.data.database.daos.AchievementDAOs
import com.example.bookworm.core.data.database.entities.AchievementEntity
import com.example.bookworm.core.data.database.entities.UnlockedAchievementEntity
import com.example.bookworm.core.data.models.AchievementType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AchievementRepository(private val dao: AchievementDAOs) {

    suspend fun insertAchievements(achievements: List<AchievementEntity>) {
        dao.insertAchievements(achievements)
    }

    suspend fun upsertUserAchievement(userAchievement: UnlockedAchievementEntity) {
        dao.upsert(userAchievement)
    }

    suspend fun getAllAchievementsByType(type: AchievementType): List<AchievementEntity> {
        return dao.getAllAchievementsByType(type)
    }

    suspend fun getUserAchievementsOnce(userId: Long): List<UnlockedAchievementEntity> {
        return dao.getUserAchievementsOnce(userId)
    }

    // Returns a list of pairs: (achievement, user's progress or null if not started)
    fun observeUserAchievements(userId: Long): Flow<List<Pair<AchievementEntity, UnlockedAchievementEntity?>>> {
        return dao.getUserAchievementsWithProgress().map { list ->
            list.map { achievementWithProgress ->
                val userProgress = achievementWithProgress.userProgress.firstOrNull { it.userId == userId }
                achievementWithProgress.achievement to userProgress
            }
        }
    }

    // Update progress for all achievements of a given type
    suspend fun updateProgress(userId: Long, type: AchievementType, amount: Int = 1) {
        val allAchievements = getAllAchievementsByType(type)
        val userAchievements = getUserAchievementsOnce(userId)

        allAchievements.forEach { achievement ->
            val currentProgress =
                userAchievements.find { it.achievementId == achievement.achievementId }?.progress
                    ?: 0
            val newProgress = (currentProgress + amount).coerceAtMost(achievement.targetNumber)
            val completed = newProgress >= achievement.targetNumber

            upsertUserAchievement(
                UnlockedAchievementEntity(
                    achievementId = achievement.achievementId,
                    userId = userId,
                    progress = newProgress,
                    isCompleted = completed,
                    isNotified = false
                )
            )
        }
    }
}