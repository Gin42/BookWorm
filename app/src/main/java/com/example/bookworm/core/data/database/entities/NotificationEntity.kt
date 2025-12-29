package com.example.bookworm.core.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "notifications",
    indices = [
        Index(value = ["user_id"]),
        Index(value = ["achievement_id"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UnlockedAchievementEntity::class,
            parentColumns = ["achievement_id", "user_id"],
            childColumns = ["achievement_id", "user_id"],
            onDelete = ForeignKey.CASCADE
        ),
    ]
)
data class NotificationEntity(

    @PrimaryKey
    @ColumnInfo(name = "notification_id")
    val notificationId: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "body")
    val body: String,

    @ColumnInfo(name = "user_id")
    val userId: Long,

    @ColumnInfo(name = "send_time")
    val sendTime: Long,

    @ColumnInfo(name = "is_read")
    val isRead: Boolean,

    @ColumnInfo(name = "achievement_id")
    val achievementId: Long,

)