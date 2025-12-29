package com.example.bookworm.core.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "unlocked_achievements",
    indices = [
        Index(value = ["user_id"]),
    ],
    primaryKeys = ["achievement_id", "user_id"],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = AchievementEntity::class,
            parentColumns = ["achievement_id"],
            childColumns = ["achievement_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UnlockedAchievementEntity(

    @ColumnInfo(name = "achievement_id")
    val achievementId: Long,

    @ColumnInfo(name = "user_id")
    val userId: Long,

    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean = false,

    @ColumnInfo(name = "is_notified")
    val isNotified: Boolean = false,

)