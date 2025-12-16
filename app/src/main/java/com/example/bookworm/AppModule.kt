package com.example.bookworm

import com.example.bookworm.ui.screens.adddiaryentry.AddDiaryEntryViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val appModule = module {
    viewModel { AddDiaryEntryViewModel() }
}