package com.example.bookworm.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bookworm.data.daos.BookDAOs
import com.example.bookworm.data.daos.JourneyEntryDAOs
import com.example.bookworm.data.daos.NotificationDAOs
import com.example.bookworm.data.daos.ReadingJourneyDAOs
import com.example.bookworm.data.daos.UserAchievementDAOs
import com.example.bookworm.data.daos.UserDAOs
import com.example.bookworm.data.entities.AchievementEntity
import com.example.bookworm.data.entities.BookEntity
import com.example.bookworm.data.entities.JourneyEntryEntity
import com.example.bookworm.data.entities.NotificationEntity
import com.example.bookworm.data.entities.ReadingJourneyEntity
import com.example.bookworm.data.entities.UserAchievementEntity
import com.example.bookworm.data.entities.UserEntity
import com.example.bookworm.data.views.UserAchievementView

@Database(
    entities = [
        UserEntity::class,
        BookEntity::class,
        ReadingJourneyEntity::class,
        JourneyEntryEntity::class,
        AchievementEntity::class,
        UserAchievementEntity::class,
        NotificationEntity::class
    ],
    views = [
        UserAchievementView::class
    ],
    version = 1
)
abstract class BookWormDatabase : RoomDatabase() {
    abstract fun userDao(): UserDAOs
    abstract fun bookDao(): BookDAOs
    abstract fun readingJourneyDao(): ReadingJourneyDAOs
    abstract fun journeyEntryDao(): JourneyEntryDAOs
    abstract fun achievementDao(): UserAchievementDAOs
    abstract fun notificationDao(): NotificationDAOs
}