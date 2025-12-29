package com.example.bookworm.core.data.database.daos


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.bookworm.core.data.database.entities.AchievementEntity
import com.example.bookworm.core.data.database.entities.UnlockedAchievementEntity
import com.example.bookworm.core.data.database.entities.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDAOs {

    @Query("SELECT * FROM users WHERE user_id = :userId")
    fun getUserById(userId: Long): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE username = :username  AND password = :password")
    fun loginUser(username: String, password: String): Flow<UserEntity?>

    @Upsert
    suspend fun upsertUser(user: UserEntity): Long

    @Delete
    suspend fun deleteUser(user: UserEntity)

    @Query("UPDATE users SET image = :image WHERE user_id = :userId")
    suspend fun upsertImage(userId: Long, image: String)

    @Query("SELECT * FROM achievements")
    suspend fun getAllAchievements(): List<AchievementEntity>

    @Insert
    suspend fun insertUserAchievement(achievements: List<UnlockedAchievementEntity>)

    @Transaction
    suspend fun createAccount(user: UserEntity) {
        val userId = upsertUser(user)
        val achievements = getAllAchievements().map {
            UnlockedAchievementEntity(
                it.achievementId,
                userId
            )
        }
        insertUserAchievement(achievements)
    }
}