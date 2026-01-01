package com.example.bookworm.core.data.repositories

import com.example.bookworm.core.data.database.daos.UserDAOs
import com.example.bookworm.core.data.database.entities.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.firstOrNull

class UserRepository(private val userDAO: UserDAOs) {

    private val _userFlow = MutableStateFlow<UserEntity?>(null)
    val userFlow: StateFlow<UserEntity?> get() = _userFlow


    suspend fun upsert(user: UserEntity): Long = userDAO.upsertUser(user)

    suspend fun loginUser(username: String, password: String): UserEntity? {
        val foundUser = userDAO.loginUser(username, password).firstOrNull()
        if (foundUser != null) {
            _userFlow.value = foundUser
        }
        return foundUser
    }

    private fun getUserById(userId: Long): Flow<UserEntity?> = userDAO.getUserById(userId)

    suspend fun checkUsernameExists(username: String): Boolean {
        return userDAO.checkUsernameExists(username).firstOrNull() != null
    }
}