package com.example.bookworm.data.entities


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index
import com.example.bookworm.data.models.ReadingStatus

@Entity(
    tableName = "books",
    indices = [
        Index(value = ["title", "author"], unique = true), /*eventualemente me ne posso fregare*/
        Index(value = ["book_id", "user_id"], unique = true)
    ],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["book_id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class BookEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "book_id")
    val bookId: Long,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "author")
    val author: String,

    @ColumnInfo(name = "pages")
    val pages: Int,

    @ColumnInfo(name = "image")
    val image: String? = null,

    @ColumnInfo(name = "favourite")
    val favourite: Boolean = false,

    @ColumnInfo(name = "status")
    val status: ReadingStatus = ReadingStatus.PLAN_TO_READ,

    @ColumnInfo(name = "user_id")
    val userId: Long,
)