package com.example.bookworm.ui.screens.adddiaryentry


import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AddDiaryEntryState(
    val date: Long? = null,
    val showDatePicker: Boolean = false,
    val pages: String = "",
    val comment: String = ""
)

interface AddDiaryEntryActions {
    fun setDate(date: Long)
    fun setPages(pages: String)
    fun setComment(comment: String)
    fun setShowDatePicker(showDatePicker: Boolean)
}

class AddDiaryEntryViewModel : ViewModel() {
    private val _state = MutableStateFlow(AddDiaryEntryState())
    val state = _state.asStateFlow()

    val actions = object : AddDiaryEntryActions {
        override fun setDate(date: Long) {
            _state.update { it.copy(date = date) }
        }

        override fun setPages(pages: String) {
            _state.update { it.copy(pages = pages) }
        }

        override fun setComment(comment: String) {
            _state.update { it.copy(comment = comment) }
        }

        override fun setShowDatePicker(showDatePicker: Boolean) {
            _state.update { it.copy(showDatePicker = showDatePicker) }
        }


    }
}
