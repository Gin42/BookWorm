package com.example.bookworm

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.bookworm.core.data.BookWormDatabase
import com.example.bookworm.core.data.evaluators.AchievementEvaluator
import com.example.bookworm.core.data.models.AchievementName
import com.example.bookworm.core.data.models.usecase.ReadingStatusStateMachine
import com.example.bookworm.core.data.repositories.AchievementRepository
import com.example.bookworm.core.data.repositories.BookRepository
import com.example.bookworm.core.data.repositories.JourneyEntryRepository
import com.example.bookworm.core.data.repositories.NotificationRepository
import com.example.bookworm.core.data.repositories.ReadingJourneyRepository
import com.example.bookworm.core.data.repositories.ThemeRepository
import com.example.bookworm.core.data.repositories.UserRepository
import com.example.bookworm.ui.entitiesViewModel.AchievementViewModel
import com.example.bookworm.ui.entitiesViewModel.NotificationViewModel
import com.example.bookworm.ui.entitiesViewModel.UserViewModel
import com.example.bookworm.ui.screens.addbook.AddBookViewModel
import com.example.bookworm.ui.screens.adddiaryentry.AddDiaryEntryViewModel
import com.example.bookworm.ui.screens.authentication.LoginViewModel
import com.example.bookworm.ui.screens.authentication.RegistrationViewModel
import com.example.bookworm.ui.screens.bookdetails.BookDetailsViewModel
import com.example.bookworm.ui.screens.home.LibraryViewModel
import com.example.bookworm.ui.screens.settings.ThemeViewModel
import com.example.bookworm.ui.screens.stats.StatsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val Context.dataStore by preferencesDataStore("theme")

val appModule = module {
    single { get<Context>().dataStore }

    single {
        Room.databaseBuilder(
            get(),
            BookWormDatabase::class.java,
            "bookworm"
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                    createAchievements(db)
                }
            })
            .build()
    }

    single {
        AchievementEvaluator(
            bookDao = get<BookWormDatabase>().bookDao(),
            journeyDao = get<BookWormDatabase>().readingJourneyDao(),
            achievementDao = get<BookWormDatabase>().achievementDao(),
            notificationDAO = get<BookWormDatabase>().notificationDao(),
        )
    }

    single { ThemeRepository(get()) }

    single {
        UserRepository(get<BookWormDatabase>().userDao())
    }

    single {
        BookRepository(
            get<BookWormDatabase>().bookDao(),
            get())
    }

    single {
        ReadingJourneyRepository(
            journeyDAO = get<BookWormDatabase>().readingJourneyDao(),
            entryDAO = get<BookWormDatabase>().journeyEntryDao(),
            achievementEvaluator = get()
        )
    }

    single {
        JourneyEntryRepository(get<BookWormDatabase>().journeyEntryDao())
    }

    single {
        AchievementRepository(get<BookWormDatabase>().achievementDao())
    }

    single {
        NotificationRepository(get<BookWormDatabase>().notificationDao())
    }

    single { ReadingStatusStateMachine() }

    viewModel { UserViewModel(get()) }

    viewModel { (userId: Long) ->
        LibraryViewModel(
            userId = userId,
            repository = get()
        )
    }

    viewModel { ThemeViewModel(get()) }

    viewModel { (
                    bookId: Long,
                    userId: Long,
                ) ->
        AddDiaryEntryViewModel(
            bookId = bookId,
            userId = userId,
            bookRepository = get(),
            journeyRepository = get()
        )
    }

    viewModel { RegistrationViewModel() }

    viewModel { LoginViewModel() }

    viewModel { (
                    bookId: Long,
                    userId: Long,
                ) ->
        BookDetailsViewModel(
            bookId = bookId,
            userId = userId,
            stateMachine = get(),
            bookRepository = get(),
            journeyRepository = get()
        )
    }

    viewModel { (userId: Long) ->
        AddBookViewModel(
            userId = userId,
            repository = get()
        )
    }

    viewModel { (userId: Long) ->
        StatsViewModel(
            userId = userId,
            repository = get()
        )
    }

    viewModel { (userId: Long) ->
        AchievementViewModel(
            userId = userId,
            repository = get()
        )
    }

    viewModel { (userId: Long) ->
        NotificationViewModel(
            userId = userId,
            repository = get(),
        )
    }
}

private fun createAchievements(db: SupportSQLiteDatabase) {
    AchievementName.entries.forEach {
        db.insert(
            "achievements",
            SQLiteDatabase.CONFLICT_IGNORE,
            ContentValues().apply {
                put("name", it.name)
                put("description", "")
                put("image", it.imageResId)
            }
        )
    }
}
