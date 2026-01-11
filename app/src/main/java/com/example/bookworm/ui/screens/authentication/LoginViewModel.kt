package com.example.bookworm.ui.screens.authentication

import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.bookworm.core.data.models.AuthenticationResults
import com.example.bookworm.core.data.models.BackPress
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


data class LoginState(
    val username: String = "",
    val password: String = "",
    val showPassword: Boolean = false,

    val error: Boolean = false,
    val errorMessage: AuthenticationResults = AuthenticationResults.CannotSubmit,

    val showToast: Boolean = false,
    val backPressState: BackPress = BackPress.Idle
) {
    val canSubmit get() = username.isNotBlank() && password.isNotBlank()
}

interface LoginAction {
    fun setUsername(username: String)
    fun setPassword(password: String)

    fun setError(value: Boolean)
    fun setErrorMessage(errorMessage: AuthenticationResults)

    fun setShowPassword(showPassword: Boolean)
    fun setShowToast(value: Boolean)
    fun setBackPressState(value: BackPress)
}

class LoginViewModel : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    val actions = object : LoginAction {
        override fun setUsername(username: String) {
            _state.update { it.copy(username = username) }
        }

        override fun setPassword(password: String) {
            _state.update { it.copy(password = password) }
        }

        override fun setShowPassword(showPassword: Boolean) {
            _state.update { it.copy(showPassword = showPassword) }
        }

        override fun setShowToast(value: Boolean) {
            _state.update { it.copy(showToast = value) }
        }

        override fun setBackPressState(value: BackPress) {
            _state.update { it.copy(backPressState = value) }
        }

        override fun setError(value: Boolean) {
            _state.update { it.copy(error = value) }
        }

        override fun setErrorMessage(errorMessage: AuthenticationResults) {
            _state.update { it.copy(errorMessage = errorMessage) }
        }
    }
}