package com.example.bookworm.core.data.repositories

import com.example.bookworm.core.data.database.daos.UserDAOs
import com.example.bookworm.core.data.database.entities.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.firstOrNull

class UserRepository(private val userDAO: UserDAOs) {

    private var _userFlow: Flow<UserEntity?> = emptyFlow()
    val userFlow: Flow<UserEntity?>
        get() = _userFlow

    suspend fun upsert(user: UserEntity): Long = userDAO.upsertUser(user)

    suspend fun loginUser(username: String, password: String): Boolean {
        val foundUser = userDAO.loginUser(username, password).firstOrNull()
        return if (foundUser != null) {
            _userFlow = getUserById(foundUser.userId)
            true
        } else false
    }

    private fun getUserById(userId: Long): Flow<UserEntity?> = userDAO.getUserById(userId)

    suspend fun upsertImage(userId: Long, image: String) = userDAO.upsertImage(userId, image)

    suspend fun createAccount(user: UserEntity) {
        userDAO.createAccount(user)
    }
}