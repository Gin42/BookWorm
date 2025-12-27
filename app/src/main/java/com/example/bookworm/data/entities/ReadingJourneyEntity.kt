package com.example.bookworm.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "reading_journey",
    indices = [
        Index(value = ["start_date", "book_id", "user_id"], unique = true)
    ],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["journey_id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = BookEntity::class,
            parentColumns = ["journey_id"],
            childColumns = ["book_id"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class ReadingJourneyEntity (
    @PrimaryKey
    @ColumnInfo(name="journey_id")
    val journeyId: Long,

    @ColumnInfo(name = "start_date")
    val startDate: Date,

    @ColumnInfo(name = "endDate")
    val endDate: Date? = null,

    @ColumnInfo(name = "is_dropped")
    val isDropped: Boolean = false,

    @ColumnInfo(name = "user_id")
    val userId: Long,

    @ColumnInfo(name = "book_id")
    val bookId: Long,
)