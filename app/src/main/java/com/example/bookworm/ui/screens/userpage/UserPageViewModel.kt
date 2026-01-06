package com.example.bookworm.ui.screens.userpage

import androidx.lifecycle.ViewModel
import com.example.bookworm.ui.screens.addbook.AddBookState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


data class UserPageState(
    val seeAllAchievements: Boolean = false
)


interface UserPageActions {
    fun setSeeAllAchievements(value: Boolean)
}

class UserPageViewModel(
) : ViewModel() {
    private val _state = MutableStateFlow(UserPageState())
    val state = _state.asStateFlow()

    val actions = object : UserPageActions {
        override fun setSeeAllAchievements(value: Boolean) {
            _state.update { it.copy(seeAllAchievements = value) }
        }
    }
}

