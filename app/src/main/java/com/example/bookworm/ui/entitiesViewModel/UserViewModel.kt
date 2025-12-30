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
import kotlinx.coroutines.flow.map
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
    fun loginUser(username: String, password: String): AuthenticationResult
}

class UserViewModel(
    private val repository: UserRepository
) : ViewModel() {
    private var _state: MutableStateFlow<LoggedUserState> = MutableStateFlow(LoggedUserState())
    val state get() = _state.asStateFlow()

    val actions = object : UserActions {

        override suspend fun registerUser(user: UserEntity): AuthenticationResult {

            val usernameExist = runBlocking {
                repository.checkUsernameExists(user.username)
            }
            return if (!usernameExist) {
                runBlocking {  repository.upsert(user) }
                _state.value = LoggedUserState(user)
                Log.d(
                    TAG,
                    "New User Created: ID = ${_state.value.id}, Username = ${_state.value.username}, Image = ${_state.value.image}, Password = ${_state.value.password}"
                )
                AuthenticationResult.Success
            } else AuthenticationResult.UsernameTaken
        }

        override fun loginUser(username: String, password: String): AuthenticationResult {
            val res =
                runBlocking() { repository.loginUser(username = username, password = password) }
            return if (res != null) {
                viewModelScope.launch {
                    repository.userFlow.map {
                        LoggedUserState(it ?: UserEntity())
                    }.collect {_state.value = it}
                }
                Log.d(
                    TAG,
                    "User Logged: ID = ${_state.value.id}, Username = ${_state.value.username}, Image = ${_state.value.image}, Password = ${_state.value.password}"
                )
                AuthenticationResult.Success
            } else AuthenticationResult.WrongCredentials
        }
    }

}