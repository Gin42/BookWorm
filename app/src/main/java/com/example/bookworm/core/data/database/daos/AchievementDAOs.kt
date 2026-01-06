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

    @Upsert
    suspend fun upsert(userAchievement: UnlockedAchievementEntity)


    @Query("SELECT * FROM achievements WHERE achievement_id IN " +
            "(SELECT achievement_id FROM unlocked_achievements " +
            "WHERE user_id=:userId)")
    fun getAllUserAchievements(userId: Long) : Flow<List<AchievementEntity>>

    @Query("SELECT * FROM achievements WHERE achievement_id NOT IN " +
            "(SELECT achievement_id FROM unlocked_achievements " +
            "WHERE user_id=:userId)")
    fun getNotUnlockedAchievements(userId: Long) : Flow<List<AchievementEntity>>

}