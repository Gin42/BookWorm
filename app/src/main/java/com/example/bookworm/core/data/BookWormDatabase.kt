package com.example.bookworm.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bookworm.core.data.database.daos.BookDAOs
import com.example.bookworm.core.data.database.daos.JourneyEntryDAOs
import com.example.bookworm.core.data.database.daos.NotificationDAOs
import com.example.bookworm.core.data.database.daos.ReadingJourneyDAOs
import com.example.bookworm.core.data.database.daos.UserAchievementDAOs
import com.example.bookworm.core.data.database.daos.UserDAOs
import com.example.bookworm.core.data.database.entities.BookEntity
import com.example.bookworm.core.data.database.entities.UserAchievementEntity
import com.example.bookworm.core.data.database.entities.UserEntity
import com.example.bookworm.core.data.database.views.UserAchievementView
import com.example.bookworm.core.data.database.entities.AchievementEntity
import com.example.bookworm.core.data.database.entities.JourneyEntryEntity
import com.example.bookworm.core.data.database.entities.NotificationEntity
import com.example.bookworm.core.data.database.entities.ReadingJourneyEntity

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