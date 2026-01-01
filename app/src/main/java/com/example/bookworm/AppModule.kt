package com.example.bookworm

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.bookworm.core.data.BookWormDatabase
import com.example.bookworm.core.data.repositories.BookRepository
import com.example.bookworm.core.data.repositories.ThemeRepository
import com.example.bookworm.core.data.repositories.UserRepository
import com.example.bookworm.ui.entitiesViewModel.BookViewModel
import com.example.bookworm.ui.entitiesViewModel.UserViewModel
import com.example.bookworm.ui.screens.addbook.AddBookViewModel
import com.example.bookworm.ui.screens.adddiaryentry.AddDiaryEntryViewModel
import com.example.bookworm.ui.screens.authentication.LoginViewModel
import com.example.bookworm.ui.screens.authentication.RegistrationViewModel
import com.example.bookworm.ui.screens.bookdetails.BookDetailsViewModel
import com.example.bookworm.ui.screens.settings.ThemeViewModel
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
            .fallbackToDestructiveMigration() //to check with ale
            .build()
    }

    single { ThemeRepository(get()) }

    single {
        UserRepository(get<BookWormDatabase>().userDao())
    }

    single {
        BookRepository(get<BookWormDatabase>().bookDao())
    }

    viewModel { UserViewModel(get()) }

    viewModel { BookViewModel(get(), get()) }


    viewModel { ThemeViewModel(get()) }

    viewModel { AddDiaryEntryViewModel() }

    viewModel { RegistrationViewModel(get()) }

    viewModel { LoginViewModel(get()) }

    viewModel { BookDetailsViewModel(get(), get()) }

    viewModel { AddBookViewModel(get()) }
}