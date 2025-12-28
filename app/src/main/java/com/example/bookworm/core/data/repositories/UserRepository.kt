package com.example.bookworm.core.data.repositories

import com.example.bookworm.core.data.database.daos.UserDAOs
import com.example.bookworm.core.data.database.entities.UserEntity
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDAO: UserDAOs) {

    suspend fun getUserById(userId: Long): Flow<UserEntity?> {
        return userDAO.getUserById(userId)
    }

    suspend fun loginUser(username: String, password: String): UserEntity? {
        return userDAO.loginUser(username, password)
    }

    suspend fun upsertImage(userId: Long, image: String) {
        userDAO.upsertImage(userId, image)
    }

    suspend fun createAccount(user: UserEntity) {
        userDAO.createAccount(user)
    }


}