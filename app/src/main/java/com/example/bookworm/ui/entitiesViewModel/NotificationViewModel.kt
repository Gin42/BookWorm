package com.example.bookworm.ui.entitiesViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookworm.core.data.database.entities.NotificationEntity
import com.example.bookworm.core.data.repositories.NotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


data class NotificationsState(
    val notifications: List<NotificationEntity>,
    val unreadNotifications: List<NotificationEntity>
)

interface NotificationActions {
    fun readNotification(notificationId: Long)
}

class NotificationViewModel(
    private val userId: Long,
    private val repository: NotificationRepository,
) : ViewModel() {

    private var _state = MutableStateFlow(NotificationsState(emptyList(), emptyList()))
    val state get() = _state.asStateFlow()

    val actions = object : NotificationActions {
        override fun readNotification(notificationId: Long) {
            viewModelScope.launch {
                repository.readNotification(notificationId = notificationId)
                refreshNotifications()
            }
        }
    }

    init {
        refreshNotifications()
    }

    private fun refreshNotifications() {
        viewModelScope.launch {
            repository.getAllNotifications(userId)
                .map { notifications ->
                    val unreadNotifications = notifications.filter { !it.isRead }
                    NotificationsState(
                        notifications = notifications,
                        unreadNotifications = unreadNotifications
                    )
                }
                .collect { _state.value = it }
        }
    }
}

