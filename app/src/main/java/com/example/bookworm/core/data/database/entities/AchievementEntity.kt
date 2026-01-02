package com.example.bookworm.core.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.bookworm.core.data.models.AchievementType

@Entity(
    tableName = "achievements",
    indices = [Index(value = ["name"], unique = true)]
)
data class AchievementEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "achievement_id")
    val achievementId: Long,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "type")
    val type: AchievementType,

    @ColumnInfo(name = "number")
    val targetNumber: Int,
)