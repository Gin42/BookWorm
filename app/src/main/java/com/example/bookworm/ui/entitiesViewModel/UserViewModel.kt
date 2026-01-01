package com.example.bookworm.ui.entitiesViewModel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookworm.core.data.database.entities.UserEntity
import com.example.bookworm.core.data.models.AuthenticationResult
import com.example.bookworm.core.data.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

data class LoggedUserState(
    val user: UserEntity = UserEntity()
) {
    val id get() = user.userId
    val username get() = user.username
    val password get() = user.password
    val image get() = user.image
}

interface UserActions {
    suspend fun registerUser(user: UserEntity): AuthenticationResult
    suspend fun loginUser(username: String, password: String): AuthenticationResult
}

class UserViewModel(
    private val repository: UserRepository
) : ViewModel() {
    private var _state: MutableStateFlow<LoggedUserState> = MutableStateFlow(LoggedUserState())
    val state get() = _state.asStateFlow()

    val actions = object : UserActions {

        override suspend fun registerUser(user: UserEntity): AuthenticationResult {
            val usernameExist = repository.checkUsernameExists(user.username)
            if (!usernameExist) {
                repository.upsert(user)
                _state.value = LoggedUserState(user)
                return AuthenticationResult.Success
            } else {
                return AuthenticationResult.UsernameTaken
            }
        }

        override suspend fun loginUser(username: String, password: String): AuthenticationResult {
            val foundUser = repository.loginUser(username, password)
            return if (foundUser != null) {
                _state.value = LoggedUserState(foundUser)
                viewModelScope.launch {
                    repository.userFlow
                        .onEach { user ->
                            _state.value = LoggedUserState(user ?: UserEntity())
                        }
                        .launchIn(this)
                }
                AuthenticationResult.Success
            } else {
                AuthenticationResult.WrongCredentials
            }
        }
    }

}