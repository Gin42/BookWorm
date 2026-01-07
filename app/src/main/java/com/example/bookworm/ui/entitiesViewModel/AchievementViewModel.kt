package com.example.bookworm.ui.entitiesViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookworm.core.data.database.entities.AchievementEntity
import com.example.bookworm.core.data.repositories.AchievementRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


data class UnlockedAchievementState(
    val achievements: List<AchievementEntity>
)

data class LockedAchievementsState(
    val achievements: List<AchievementEntity>
)

interface AchievementActions {
}

class AchievementViewModel(
    private val userId: Long,
    private val repository: AchievementRepository
) : ViewModel() {
    private var _unlockedAchievementsState = MutableStateFlow<UnlockedAchievementState>(
        UnlockedAchievementState(emptyList())
    )
    val unlockedAchievementsState get() = _unlockedAchievementsState.asStateFlow()

    private var _lockedAchievementsState = MutableStateFlow<LockedAchievementsState>(
        LockedAchievementsState(emptyList())
    )
    val lockedAchievementsState get() = _lockedAchievementsState.asStateFlow()

    val actions = object : AchievementActions {

    }

    init {
        getAllAchievements(userId)
    }

    private fun getAllAchievements(userId: Long) {
            viewModelScope.launch {
                repository.getAllUserAchievements(userId).map { UnlockedAchievementState(it) }
                    .collect { _unlockedAchievementsState.value = it }
            }
            viewModelScope.launch {
                repository.getNotUnlockedAchievements(userId).map {
                    LockedAchievementsState(it)
                }.collect { _lockedAchievementsState.value = it }
            }
        }

}

