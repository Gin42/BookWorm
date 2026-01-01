package com.example.bookworm.core.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "journey_entry",
    indices = [
        Index(value = ["entry_id", "journey_id", "book_id"], unique = true),
        Index(value = ["journey_id"]),
        Index(value = ["book_id"]),
    ],
    foreignKeys = [
        ForeignKey(
            entity = ReadingJourneyEntity::class,
            parentColumns = ["journey_id"],
            childColumns = ["journey_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = BookEntity::class,
            parentColumns = ["book_id"],
            childColumns = ["book_id"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class JourneyEntryEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="entry_id")
    val entryId: Long,

    @ColumnInfo(name = "date")
    val date: Long,

    @ColumnInfo(name = "pages_read")
    val pagesRead: Int,

    @ColumnInfo(name = "comment")
    val comment: String?,

    @ColumnInfo(name = "journey_id")
    val journeyId: Long,

    @ColumnInfo(name = "book_id")
    val bookId: Long,
)