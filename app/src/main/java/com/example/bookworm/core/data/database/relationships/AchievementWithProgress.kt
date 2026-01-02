package com.example.bookworm.core.data.database.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.example.bookworm.core.data.database.entities.AchievementEntity
import com.example.bookworm.core.data.database.entities.UnlockedAchievementEntity

data class AchievementWithProgress(
    @Embedded val achievement: AchievementEntity,

    @Relation(
        parentColumn = "achievement_id",
        entityColumn = "achievement_id"
    )
    val userProgress: List<UnlockedAchievementEntity> = emptyList()
)
