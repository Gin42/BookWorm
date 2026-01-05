package com.example.bookworm.ui.screens.authentication

import android.net.Uri
import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookworm.core.data.database.entities.UserEntity
import com.example.bookworm.core.data.models.AuthenticationResult
import com.example.bookworm.ui.entitiesViewModel.UserViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegistrationState(
    val username: String = "",
    val password: TextFieldState = TextFieldState(initialText = ""),
    val userPhoto: Uri? = Uri.EMPTY,
    val showPassword: Boolean = false,

    val usernameError: Boolean = false,
    val passwordError: Boolean = false,
    val errorMessage: AuthenticationResult = AuthenticationResult.CannotSubmit
) {
    val canSubmit get() = username.isNotBlank() && password.text.isNotBlank()

    fun toUser() = UserEntity(
        userId = 0L,
        username = username,
        password = password.text.toString(),
        image = userPhoto.toString(),
    )
}

interface RegistrationActions {
    fun setUsername(username: String)
    fun setPassword(password: TextFieldState)
    fun setUserPhoto(userPhoto: Uri?)

    fun setUsernameError(value: Boolean)
    fun setPasswordError(value: Boolean)
    fun setErrorMessage(errorMessage: AuthenticationResult)

    fun setShowPassword(showPassword: Boolean)
}

class RegistrationViewModel(
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

        override fun setUserPhoto(userPhoto: Uri?) {
            _state.update { it.copy(userPhoto = userPhoto) }
        }

        override fun setUsernameError(value: Boolean) {
            _state.update { it.copy(usernameError = value) }
        }

        override fun setPasswordError(value: Boolean) {
            _state.update { it.copy(passwordError = value) }
        }

        override fun setErrorMessage(errorMessage: AuthenticationResult) {
            _state.update { it.copy(errorMessage = errorMessage) }
        }

        override fun setShowPassword(showPassword: Boolean) {
            _state.update { it.copy(showPassword = showPassword) }
        }
    }

}

