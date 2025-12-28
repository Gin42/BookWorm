package com.example.bookworm.ui.screens.addbook

import androidx.lifecycle.ViewModel
import com.example.bookworm.ui.BookWormRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


data class AddBookState(
    val title: String = "",
    val author: String = "",
    val pages: String = "",
    val showAlert: Boolean = false,
    val alertConfirmed: Boolean = false,
    val navDestination: BookWormRoute? = null
)

interface AddBookActions {
    fun setTitle(title: String)
    fun setAuthor(author: String)
    fun setPages(pages: String)
    fun setShowAlert(value: Boolean)
    fun setAlertConfirmed(value: Boolean)
    fun setNavDestination(route: BookWormRoute?)
}

class AddBookViewModel : ViewModel() {
    private val _state = MutableStateFlow(AddBookState())
    val state = _state.asStateFlow()

    val actions = object : AddBookActions {
        override fun setTitle(title: String) {
            _state.update { it.copy(title = title) }
        }

        override fun setPages(pages: String) {
            _state.update { it.copy(pages = pages) }
        }

        override fun setAuthor(author: String) {
            _state.update { it.copy(author = author) }
        }

        override fun setShowAlert(value: Boolean) {
            _state.update { it.copy(showAlert = value) }
        }

        override fun setAlertConfirmed(value: Boolean) {
            _state.update { it.copy(alertConfirmed = value) }
        }

        override fun setNavDestination(route: BookWormRoute?) {
            _state.update { it.copy(navDestination = route) }
        }
    }
}