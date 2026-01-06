package com.example.bookworm

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.bookworm.core.data.BookWormDatabase
import com.example.bookworm.core.data.models.AchievementName
import com.example.bookworm.core.data.models.AchievementType
import com.example.bookworm.core.data.models.usecase.ReadingStatusStateMachine
import com.example.bookworm.core.data.repositories.AchievementRepository
import com.example.bookworm.core.data.repositories.BookRepository
import com.example.bookworm.core.data.repositories.JourneyEntryRepository
import com.example.bookworm.core.data.repositories.ReadingJourneyRepository
import com.example.bookworm.core.data.repositories.ThemeRepository
import com.example.bookworm.core.data.repositories.UserRepository
import com.example.bookworm.ui.entitiesViewModel.AchievementViewModel
import com.example.bookworm.ui.entitiesViewModel.UserViewModel
import com.example.bookworm.ui.screens.addbook.AddBookViewModel
import com.example.bookworm.ui.screens.adddiaryentry.AddDiaryEntryViewModel
import com.example.bookworm.ui.screens.authentication.LoginViewModel
import com.example.bookworm.ui.screens.authentication.RegistrationViewModel
import com.example.bookworm.ui.screens.bookdetails.BookDetailsViewModel
import com.example.bookworm.ui.screens.home.LibraryViewModel
import com.example.bookworm.ui.screens.settings.ThemeViewModel
import com.example.bookworm.ui.screens.stats.StatsViewModel
import com.example.bookworm.ui.screens.userpage.UserPageViewModel
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
            .fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    AchievementName.entries.forEach {
                        val values = ContentValues().apply {
                            put("name", it.name)
                            put("description", "")
                            put("image", it.imageResId)
                        }
                        db.insert(
                            "achievements",
                            SQLiteDatabase.CONFLICT_IGNORE,
                            values
                        )
                    }
                    AchievementName.entries.forEach {
                        val table = when (it.type) {
                            AchievementType.BookRead -> "reading_journey"
                            AchievementType.BookAdded -> "books"
                        }
                        when (it.type) {
                            AchievementType.BookRead -> {
                                db.execSQL(
                                    """
                                    CREATE TRIGGER IF NOT EXISTS ${it.name}_trigger
                                    AFTER UPDATE ON $table
                                    FOR EACH ROW
                                    WHEN
                                        NEW.end_date IS NOT NULL
                                        AND OLD.end_date IS NULL
                                        AND NEW.is_dropped = 0
                                        AND (
                                            SELECT COUNT(*)
                                            FROM $table
                                            WHERE userId = NEW.userId
                                              AND end_date IS NOT NULL
                                              AND is_dropped = 0
                                        ) = ${it.number}
                                    BEGIN
                                        INSERT OR IGNORE INTO unlocked_achievements (userId, achievementId)
                                        VALUES (
                                            NEW.userId,
                                            (SELECT achievement_id FROM achievements WHERE name = '${it.name}')
                                        );
                                    END;
                                    """.trimIndent()
                                )
                            }

                            AchievementType.BookAdded -> {
                                db.execSQL(
                                    """
                                    CREATE TRIGGER IF NOT EXISTS ${it.name}_trigger
                                    AFTER INSERT ON $table
                                    FOR EACH ROW
                                    WHEN (
                                        SELECT COUNT(*)
                                        FROM $table
                                        WHERE userId = NEW.userId
                                    ) = ${it.number}
                                    BEGIN
                                        INSERT OR IGNORE INTO unlocked_achievements (userId, achievementId)
                                        VALUES (
                                            NEW.userId,
                                            (SELECT achievement_id FROM achievements WHERE name = '${it.name}')
                                        );
                                    END;
                                    """.trimIndent()
                                )
                            }
                        }

                    }
                }
            })
            .build()
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

    viewModel { UserPageViewModel() }

    viewModel { (userId: Long) ->
        AchievementViewModel(
            userId = userId,
            repository = get()
        )
    }
}