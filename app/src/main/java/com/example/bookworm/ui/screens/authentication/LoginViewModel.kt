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


//val userPhoto: String o URI ?

interface LoginAction {
    fun setUsername(username: String)
    fun setPassword(password: TextFieldState)

    fun setShowPassword(showPassword: Boolean)
    //fun performLogin(onLoginComplete: (AuthenticationResult) -> Unit)
}

class LoginViewModel(
    private val userViewModel: UserViewModel
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

        /*override fun performLogin(onLoginComplete: (AuthenticationResult) -> Unit) {
            viewModelScope.launch {
                val result = userViewModel.actions.loginUser(
                    state.value.username,
                    state.value.password.text.toString()
                )
                onLoginComplete(result)
            }
        }*/
    }
}