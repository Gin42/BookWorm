package com.example.bookworm.core.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "reading_journey",
    indices = [
        Index(value = ["user_id"]),
        Index(value = ["book_id"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = BookEntity::class,
            parentColumns = ["book_id"],
            childColumns = ["book_id"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class ReadingJourneyEntity (
    @PrimaryKey
    @ColumnInfo(name="journey_id")
    val journeyId: Long,

    @ColumnInfo(name = "start_date")
    val startDate: Long,

    @ColumnInfo(name = "end_date")
    val endDate: Long? = null,

    @ColumnInfo(name = "is_dropped")
    val isDropped: Boolean = false,

    @ColumnInfo(name = "user_id")
    val userId: Long,

    @ColumnInfo(name = "book_id")
    val bookId: Long,
)