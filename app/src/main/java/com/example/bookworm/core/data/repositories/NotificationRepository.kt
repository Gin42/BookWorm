package com.example.bookworm.core.data.repositories

import com.example.bookworm.core.data.database.daos.NotificationDAOs
import com.example.bookworm.core.data.database.entities.NotificationEntity
import kotlinx.coroutines.flow.Flow

class NotificationRepository(private val notificationDAO: NotificationDAOs) {

    suspend fun upsertNotification(notification: NotificationEntity): Long =
        notificationDAO.upsertNotification(notification)

    suspend fun deleteNotification(notificationId: Long): Boolean {
        return try {
            val notification = getNotificationById(notificationId)
            notificationDAO.deleteNotification(notification)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getNotificationById(notificationId: Long): NotificationEntity =
        notificationDAO.getNotificationById(notificationId)

    fun getAllNotifications(userId: Long): Flow<List<NotificationEntity>> =
        notificationDAO.getAllNotifications(userId)

    suspend fun readNotification(notificationId: Long) {
        notificationDAO.readNotification(notificationId)
    }
}