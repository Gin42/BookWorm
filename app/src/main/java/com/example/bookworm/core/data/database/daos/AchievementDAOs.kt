package com.example.bookworm.core.data.database.daos

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.bookworm.core.data.database.entities.AchievementEntity
import com.example.bookworm.core.data.database.entities.UnlockedAchievementEntity
import com.example.bookworm.core.data.database.relationships.AchievementWithProgress
import com.example.bookworm.core.data.models.AchievementType
import kotlinx.coroutines.flow.Flow

@Dao
interface AchievementDAOs {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAchievements(achievements: List<AchievementEntity>)

    @Upsert
    suspend fun upsert(userAchievement: UnlockedAchievementEntity)

    @Query("SELECT * FROM achievements WHERE type = :type")
    suspend fun getAllAchievementsByType(type: AchievementType): List<AchievementEntity>

    @Query("SELECT * FROM unlocked_achievements WHERE user_id = :userId")
    suspend fun getUserAchievementsOnce(userId: Long): List<UnlockedAchievementEntity>

    // Return achievement + user progress in a single object
    @Transaction
    @Query("SELECT * FROM achievements")
    fun getUserAchievementsWithProgress(): Flow<List<AchievementWithProgress>>
}