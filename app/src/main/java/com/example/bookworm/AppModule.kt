package com.example.bookworm

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.bookworm.core.data.BookWormDatabase
import com.example.bookworm.core.data.database.entities.AchievementEntity
import com.example.bookworm.core.data.models.AchievementName
import com.example.bookworm.core.data.models.AchievementType
import com.example.bookworm.core.data.repositories.AchievementRepository
import com.example.bookworm.core.data.repositories.BookRepository
import com.example.bookworm.core.data.repositories.JourneyEntryRepository
import com.example.bookworm.core.data.repositories.ReadingJourneyRepository
import com.example.bookworm.core.data.repositories.ThemeRepository
import com.example.bookworm.core.data.repositories.UserRepository
import com.example.bookworm.ui.entitiesViewModel.AchievementViewModel
import com.example.bookworm.ui.entitiesViewModel.BookViewModel
import com.example.bookworm.ui.entitiesViewModel.UserViewModel
import com.example.bookworm.ui.screens.addbook.AddBookViewModel
import com.example.bookworm.ui.screens.adddiaryentry.AddDiaryEntryViewModel
import com.example.bookworm.ui.screens.authentication.LoginViewModel
import com.example.bookworm.ui.screens.authentication.RegistrationViewModel
import com.example.bookworm.ui.screens.bookdetails.BookDetailsViewModel
import com.example.bookworm.ui.screens.settings.ThemeViewModel
import com.example.bookworm.ui.screens.stats.StatsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val Context.dataStore by preferencesDataStore("theme")

val appModule = module {
    single { get<Context>().dataStore }

    single {
        // Build the Room database
        val database = Room.databaseBuilder(
            get(), // context from Koin
            BookWormDatabase::class.java,
            "bookworm"
        )
            .fallbackToDestructiveMigration() // for dev/testing; use proper migrations in production
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    // Insert initial achievements in background
                    CoroutineScope(Dispatchers.IO).launch {
                        // Get DAO from the built database
                        val dao = get<BookWormDatabase>().achievementDao()

                        val achievements = AchievementName.entries.map {
                            AchievementEntity(
                                name = it.name,
                                description = when (it.type) {
                                    AchievementType.BookRead ->
                                        "Read ${it.number} book${if (it.number > 1) "s" else ""}"

                                    AchievementType.BookAdded ->
                                        "Add ${it.number} book${if (it.number > 1) "s" else ""}"
                                },
                                type = it.type,
                                targetNumber = it.number,
                                achievementId = 0L
                            )
                        }
                        dao.insertAchievements(achievements)
                    }
                }
            })
            .build()
        database
    }



    single { ThemeRepository(get()) }

    single {
        UserRepository(get<BookWormDatabase>().userDao())
    }

    single {
        BookRepository(get<BookWormDatabase>().bookDao())
    }

    single {
        ReadingJourneyRepository(
            journeyDAO = get<BookWormDatabase>().readingJourneyDao(),
            entryDAO = get<BookWormDatabase>().journeyEntryDao()
        )
    }

    single {
        JourneyEntryRepository(get<BookWormDatabase>().journeyEntryDao())
    }

    single {
        AchievementRepository(get<BookWormDatabase>().achievementDao())
    }

    viewModel { UserViewModel(get()) }

    viewModel { BookViewModel(get(), get()) }


    viewModel { ThemeViewModel(get()) }

    viewModel { AddDiaryEntryViewModel(get(), get(), get(), get()) }

    viewModel { RegistrationViewModel(get()) }

    viewModel { LoginViewModel(get()) }

    viewModel { BookDetailsViewModel(get(), get(), get()) }

    viewModel { AddBookViewModel(get(), get()) }

    viewModel { StatsViewModel(get(), get()) }

    viewModel { AchievementViewModel(get(), get()) }
}