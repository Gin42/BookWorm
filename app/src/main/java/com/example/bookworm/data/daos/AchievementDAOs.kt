package com.example.bookworm.data.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.bookworm.data.entities.UserAchievementEntity

@Dao
interface AchievementDAOs {

    @Query("SELECT * FROM user_achievements WHERE user_id = :userId ")
    suspend fun getUserAchievements(userId: Long): List<UserAchievementEntity>
}