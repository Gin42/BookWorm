package com.example.bookworm.ui.screens.authentication

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookworm.core.data.models.AuthenticationResult
import com.example.bookworm.ui.entitiesViewModel.UserViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class LoginState(
    val username: String = "",
    val password: TextFieldState = TextFieldState(initialText = ""),
    val showPassword: Boolean = false,
) {
    val canSubmit get() = username.isNotBlank() && password.text.isNotBlank()
}

interface LoginAction {
    fun setUsername(username: String)
    fun setPassword(password: TextFieldState)

    fun setShowPassword(showPassword: Boolean)
}

class LoginViewModel(
) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    val actions = object : LoginAction {
        override fun setUsername(username: String) {
            _state.update { it.copy(username = username) }
        }

        override fun setPassword(password: TextFieldState) {
            _state.update { it.copy(password = password) }
        }

        override fun setShowPassword(showPassword: Boolean) {
            _state.update { it.copy(showPassword = showPassword) }
        }
    }
}