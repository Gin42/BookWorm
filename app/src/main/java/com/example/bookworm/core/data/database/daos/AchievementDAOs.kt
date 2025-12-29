package com.example.bookworm.core.data.database.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.bookworm.core.data.database.entities.AchievementEntity
import com.example.bookworm.core.data.database.entities.UnlockedAchievementEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AchievementDAOs {

    @Upsert
    suspend fun upsert(unlockedAchievements: UnlockedAchievementEntity)

    @Query("SELECT * FROM achievements WHERE achievement_id IN " +
            "(SELECT achievement_id FROM unlocked_achievements " +
            "WHERE user_id=:userId)")
    fun getAllUserAchievements(userId: Long) : Flow<List<AchievementEntity>>

    @Query("SELECT * FROM achievements WHERE achievement_id NOT IN " +
            "(SELECT achievement_id FROM unlocked_achievements " +
            "WHERE user_id=:userId)")
    fun getNotUnlockedAchievements(userId: Long) : Flow<List<AchievementEntity>>
}