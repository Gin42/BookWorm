package com.example.bookworm.core.data.repositories

import com.example.bookworm.core.data.database.daos.AchievementDAOs
import com.example.bookworm.core.data.database.daos.NotificationDAOs
import com.example.bookworm.core.data.database.entities.NotificationEntity
import kotlinx.coroutines.flow.Flow

class NotificationRepository ( private val notificationDAOs: NotificationDAOs) {

    suspend fun upsertNotification(notification: NotificationEntity): Long = notificationDAOs.upsertNotification(notification)

    suspend fun deleteNotification(notification: NotificationEntity): Boolean {
        return try {
            notificationDAOs.deleteNotification(notification)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getNotificationById(notificationId: String): NotificationEntity = notificationDAOs.getNotificationById(notificationId)

    fun getAllNotifications(userId: Long): Flow<List<NotificationEntity>> = notificationDAOs.getAllNotifications(userId)
}