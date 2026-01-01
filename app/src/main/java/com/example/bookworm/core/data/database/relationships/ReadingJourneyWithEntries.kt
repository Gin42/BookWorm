package com.example.bookworm.core.data.database.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.example.bookworm.core.data.database.entities.JourneyEntryEntity
import com.example.bookworm.core.data.database.entities.ReadingJourneyEntity

data class ReadingJourneyWithEntries(
    @Embedded val journey: ReadingJourneyEntity,

    @Relation(
        parentColumn = "journey_id",
        entityColumn = "journey_id"
    )
    val entries: List<JourneyEntryEntity>
)