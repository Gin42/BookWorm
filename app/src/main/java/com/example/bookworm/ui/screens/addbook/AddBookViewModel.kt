package com.example.bookworm.ui.screens.addbook

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookworm.core.data.database.entities.BookEntity
import com.example.bookworm.ui.BookWormRoute
import com.example.bookworm.ui.entitiesViewModel.UserViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class AddBookState(
    val title: String = "",
    val author: String = "",
    val pages: String = "",
    val bookCover: Uri? = Uri.EMPTY,
    val userId: Long = 0,

    val showAlert: Boolean = false,
    val alertConfirmed: Boolean = false,
    val navDestination: BookWormRoute? = null
) {
    val canSubmit get() = title.isNotBlank() && author.isNotBlank() && pages.isNotBlank()

    fun toBook() = BookEntity(
        title = title,
        author = author,
        pages = pages.toInt(),
        image = bookCover.toString(),
        userId = userId,
    )
}

interface AddBookActions {
    fun setTitle(title: String)
    fun setAuthor(author: String)
    fun setPages(pages: String)
    fun setCover(bookCover: Uri?)
    fun setUserId(userId: Long)

    fun setShowAlert(value: Boolean)
    fun setAlertConfirmed(value: Boolean)
    fun setNavDestination(route: BookWormRoute?)
}

class AddBookViewModel (private val userViewModel: UserViewModel) : ViewModel() {
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

        override fun setCover(bookCover: Uri?) {
            _state.update { it.copy(bookCover = bookCover) }
        }

        override fun setUserId(userId: Long) {
            _state.update{ it.copy(userId = userId)}
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