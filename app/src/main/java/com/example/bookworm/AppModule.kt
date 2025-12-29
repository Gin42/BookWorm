package com.example.bookworm

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.bookworm.core.data.BookWormDatabase
import com.example.bookworm.core.data.repositories.ThemeRepository
import com.example.bookworm.core.data.repositories.UserRepository
import com.example.bookworm.ui.EntitiesViewModel.UserViewModel
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

    viewModel { ThemeViewModel(get()) }

    viewModel { AddDiaryEntryViewModel() }

    viewModel { UserViewModel(get()) }

    viewModel { RegistrationViewModel(get()) }


    viewModel { LoginViewModel() }

    viewModel { BookDetailsViewModel() }

    viewModel { AddBookViewModel() }
}