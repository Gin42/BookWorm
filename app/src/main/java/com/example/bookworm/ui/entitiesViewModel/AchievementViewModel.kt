package com.example.bookworm.ui.entitiesViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookworm.core.data.database.entities.AchievementEntity
import com.example.bookworm.core.data.database.entities.UnlockedAchievementEntity
import com.example.bookworm.core.data.models.AchievementType
import com.example.bookworm.core.data.repositories.AchievementRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AchievementState(
    val achievementsWithProgress: List<Pair<AchievementEntity, UnlockedAchievementEntity?>> = emptyList()
)

// Actions exposed to the UI
interface AchievementActions {
    fun updateAchievement(type: AchievementType, amount: Int = 1)
}

class AchievementViewModel(
    private val userId: Long,
    private val repository: AchievementRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(AchievementState())
    val state: StateFlow<AchievementState> = _state.asStateFlow()

    val actions = object : AchievementActions {
        override fun updateAchievement(type: AchievementType, amount: Int) {
            viewModelScope.launch {
                repository.updateProgress(userId, type, amount)
            }
        }
    }

    init {
        observeUserAchievements()
    }

    private fun observeUserAchievements() {
        // Observe achievements with progress
        viewModelScope.launch {
            repository.observeUserAchievements(userId)
                .collect { list ->
                    _state.update { AchievementState(list) }
                }
        }
    }
}