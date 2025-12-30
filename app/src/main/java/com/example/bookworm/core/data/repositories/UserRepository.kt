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

    suspend fun loginUser(username: String, password: String): UserEntity? {
        val foundUser = userDAO.loginUser(username, password).firstOrNull()
        if (foundUser != null) {
            _userFlow = getUserById(foundUser.userId)
           return foundUser
        } else {
            return null
        }
    }

    private fun getUserById(userId: Long): Flow<UserEntity?> = userDAO.getUserById(userId)

    suspend fun checkUsernameExists(username: String): Boolean {
        return userDAO.checkUsernameExists(username).firstOrNull() != null
    }
}