package com.example.bookworm.data.daos


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.bookworm.data.entities.AchievementEntity
import com.example.bookworm.data.entities.UserAchievementEntity
import com.example.bookworm.data.entities.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDAOs {

    @Query("SELECT * FROM users WHERE user_id = :userId")
    fun getUserById(userId: Long): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE username = :username  AND password = :password")
    suspend fun logInUser(username: String, password: String): UserEntity?

    //si tratta di un UPDATE-INSERT
    @Upsert
    suspend fun upsertUser(user: UserEntity): Long

    @Delete
    suspend fun deleteUser(user: UserEntity)

    @Query("UPDATE users SET image = :image WHERE user_id = :userId")
    suspend fun upsertImage(userId: Long, image: String)


    @Query("SELECT * FROM achievements")
    suspend fun getAllAchievements(): List<AchievementEntity>

    @Insert
    suspend fun insertUserAchievement(achievements: List<UserAchievementEntity>)

    @Transaction
    suspend fun createAccount(user: UserEntity) {
        val userId = upsertUser(user)
        val achievements = getAllAchievements().map { UserAchievementEntity(it.achievementId, userId) }
        insertUserAchievement(achievements)
    }
}