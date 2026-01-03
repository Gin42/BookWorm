package com.example.bookworm.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bookworm.core.data.database.daos.BookDAOs
import com.example.bookworm.core.data.database.daos.JourneyEntryDAOs
import com.example.bookworm.core.data.database.daos.NotificationDAOs
import com.example.bookworm.core.data.database.daos.ReadingJourneyDAOs
import com.example.bookworm.core.data.database.daos.AchievementDAOs
import com.example.bookworm.core.data.database.daos.UserDAOs
import com.example.bookworm.core.data.database.entities.BookEntity
import com.example.bookworm.core.data.database.entities.UnlockedAchievementEntity
import com.example.bookworm.core.data.database.entities.UserEntity
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
        UnlockedAchievementEntity::class,
        NotificationEntity::class
    ],
    version = 4
)
abstract class BookWormDatabase : RoomDatabase() {
    abstract fun userDao(): UserDAOs
    abstract fun bookDao(): BookDAOs
    abstract fun readingJourneyDao(): ReadingJourneyDAOs
    abstract fun journeyEntryDao(): JourneyEntryDAOs
    abstract fun achievementDao(): AchievementDAOs
    abstract fun notificationDao(): NotificationDAOs
}