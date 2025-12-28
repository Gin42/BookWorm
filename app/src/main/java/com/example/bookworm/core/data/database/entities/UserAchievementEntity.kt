package com.example.bookworm.core.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "user_achievements",
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
        )
    ]
)
data class UserAchievementEntity(

    @ColumnInfo(name = "achievement_id")
    val achievementId: Long,

    @ColumnInfo(name = "user_id")
    val userId: Long,

    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean = false,

    @ColumnInfo(name = "is_notified")
    val isNotified: Boolean = false,

    @ColumnInfo(name = "number")
    val number: Int = 0

)