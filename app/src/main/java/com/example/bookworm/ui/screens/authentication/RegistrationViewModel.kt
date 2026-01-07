package com.example.bookworm.ui.screens.authentication

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.bookworm.core.data.database.entities.UserEntity
import com.example.bookworm.core.data.models.AuthenticationResults
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class RegistrationState(
    val username: String = "",
    val password: String = "",
    val userPhoto: Uri? = Uri.EMPTY,
    val showPassword: Boolean = false,

    val usernameError: Boolean = false,
    val passwordError: Boolean = false,
    val errorMessage: AuthenticationResults = AuthenticationResults.CannotSubmit,

    val isImagePickerVisible: Boolean = false,
) {
    val canSubmit get() = username.isNotBlank() && password.isNotBlank()

    fun toUser() = UserEntity(
        userId = 0L,
        username = username.trim(),
        password = password.trim(),
        image = userPhoto.toString(),
    )
}

interface RegistrationActions {
    fun setUsername(username: String)
    fun setPassword(password: String)
    fun setUserPhoto(userPhoto: Uri?)

    fun setUsernameError(value: Boolean)
    fun setPasswordError(value: Boolean)
    fun setErrorMessage(errorMessage: AuthenticationResults)

    fun setShowPassword(showPassword: Boolean)
    fun setPickerVisible(value: Boolean)
}

class RegistrationViewModel(
) : ViewModel() {
    private val _state = MutableStateFlow(RegistrationState())
    val state = _state.asStateFlow()

    val actions = object : RegistrationActions {
        override fun setUsername(username: String) {
            _state.update { it.copy(username = username) }
        }

        override fun setPassword(password: String) {
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

        override fun setErrorMessage(errorMessage: AuthenticationResults) {
            _state.update { it.copy(errorMessage = errorMessage) }
        }

        override fun setShowPassword(showPassword: Boolean) {
            _state.update { it.copy(showPassword = showPassword) }
        }

        override fun setPickerVisible(value: Boolean) {
            _state.update { it.copy(isImagePickerVisible = value) }
        }
    }

}

