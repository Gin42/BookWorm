package com.example.bookworm

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.example.bookworm.data.repositories.ThemeRepository
import com.example.bookworm.ui.screens.adddiaryentry.AddDiaryEntryViewModel
import com.example.bookworm.ui.screens.authentication.LoginViewModel
import com.example.bookworm.ui.screens.authentication.RegistrationScreen
import com.example.bookworm.ui.screens.authentication.RegistrationViewModel
import com.example.bookworm.ui.screens.bookdetails.BookDetailsViewModel
import com.example.bookworm.ui.screens.settings.ThemeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val Context.dataStore by preferencesDataStore("theme")

val appModule = module {
    single { get<Context>().dataStore }
    single { ThemeRepository(get()) }

    viewModel { ThemeViewModel(get()) }

    viewModel { AddDiaryEntryViewModel() }

    viewModel { RegistrationViewModel() }

    viewModel { LoginViewModel() }

    viewModel { BookDetailsViewModel() }
}