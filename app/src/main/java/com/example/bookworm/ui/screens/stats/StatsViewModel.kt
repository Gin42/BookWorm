package com.example.bookworm.ui.screens.stats

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookworm.core.data.repositories.ReadingJourneyRepository
import com.example.bookworm.ui.screens.bookdetails.Journey
import com.example.bookworm.utils.mapper.toUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


data class StatsState(
    var allJourneys: List<Journey> = emptyList(),
)

interface StatsActions {
    fun calculatePagesPerMonth(): Pair<Map<String, Double>, Double>
}

class StatsViewModel(
    private val userId: Long,
    private val repository: ReadingJourneyRepository
) : ViewModel() {

    private val _state: MutableStateFlow<StatsState> = MutableStateFlow(
        StatsState()
    )
    val state = _state.asStateFlow()

    val actions = object : StatsActions {

        override fun calculatePagesPerMonth(): Pair<Map<String, Double>, Double> {
            val allEntries = _state.value.allJourneys.flatMap { it.entries }

            val monthNames = listOf(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
            )

            if (allEntries.isEmpty()) {
                val emptyPagesPerMonth = monthNames.associateWith { 0.0 }
                return emptyPagesPerMonth to 0.0
            }

            val calendar = Calendar.getInstance()
            val currentYear = calendar.get(Calendar.YEAR)
            val currentYearEntries = allEntries.filter { entry ->
                calendar.time = Date(entry.date)
                calendar.get(Calendar.YEAR) == currentYear
            }
            val pagesPerMonth: MutableMap<String, Double> =
                monthNames.associateWith { 0.0 }.toMutableMap()

            val monthFormatter = SimpleDateFormat("MMMM", Locale.ENGLISH)
            currentYearEntries.groupBy { monthFormatter.format(Date(it.date)) }
                .forEach { (month, entries) ->
                    val sortedEntries = entries.sortedBy { it.date }
                    pagesPerMonth[month] = sortedEntries.mapIndexed { index, e ->
                        if (index == 0) e.pagesRead.toDouble()
                        else (e.pagesRead - sortedEntries[index - 1].pagesRead).toDouble()
                    }.sum()
                }

            val validMonths = pagesPerMonth.values.filter { it > 0.0 }
            val average = if (validMonths.isNotEmpty()) {
                pagesPerMonth.values.sum() / validMonths.size
            } else {
                0.0
            }

            return pagesPerMonth to average
        }

        init {
            observeJourneys()
            Log.d(TAG, "USER ID: $userId")
        }

        private fun observeJourneys() {
            viewModelScope.launch {
                repository.getAllJourneys(userId = userId)
                    .collect { journeys ->
                        val uiJourneys = journeys.map { it.toUi() }

                        _state.update {
                            it.copy(
                                allJourneys = uiJourneys,
                            )
                        }

                        Log.d(TAG, "JOURNEYS: ${_state.value.allJourneys}")
                    }
            }
        }

    }
}

