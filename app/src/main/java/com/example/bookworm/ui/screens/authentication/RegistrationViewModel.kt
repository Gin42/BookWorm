package com.example.bookworm.ui.screens.authentication

import android.net.Uri
import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookworm.core.data.database.entities.UserEntity
import com.example.bookworm.core.data.models.AuthenticationResult
import com.example.bookworm.core.data.repositories.UserRepository
import com.example.bookworm.ui.EntitiesViewModel.UserViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

data class RegistrationState(
    val username: String = "",
    val password: TextFieldState = TextFieldState(initialText = ""),
    val userPhoto: Uri? = null,
    val showPassword: Boolean = false,
) {
    val canSubmit get() = username.isNotBlank() && password.text.isNotBlank()
}

//val userPhoto: String o URI ?

interface RegistrationActions {
    fun setUsername(username: String)
    fun setPassword(password: TextFieldState)
    fun setShowPassword(showPassword: Boolean)
    fun performRegistration(onRegistrationComplete: (AuthenticationResult) -> Unit)
}

class RegistrationViewModel(
    private val userViewModel: UserViewModel,
) : ViewModel() {
    private val _state = MutableStateFlow(RegistrationState())
    val state = _state.asStateFlow()

    val actions = object : RegistrationActions {
        override fun setUsername(username: String) {
            _state.update { it.copy(username = username) }
        }

        override fun setPassword(password: TextFieldState) {
            _state.update { it.copy(password = password) }
        }

        override fun setShowPassword(showPassword: Boolean) {
            _state.update { it.copy(showPassword = showPassword) }
        }


        override fun performRegistration(onRegistrationComplete: (AuthenticationResult) -> Unit) {
            viewModelScope.launch {
                val user = UserEntity(
                    username = state.value.username,
                    password = state.value.password.text.toString(),
                    image = state.value.userPhoto?.toString()
                )
                val result = userViewModel.actions.registerUser(user)
                onRegistrationComplete(result)
            }
        }
    }

}

