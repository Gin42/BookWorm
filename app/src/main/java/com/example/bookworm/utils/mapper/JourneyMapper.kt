package com.example.bookworm.utils.mapper

import com.example.bookworm.core.data.database.entities.JourneyEntryEntity
import com.example.bookworm.core.data.database.relationships.ReadingJourneyWithEntries
import com.example.bookworm.ui.screens.bookdetails.Entry
import com.example.bookworm.ui.screens.bookdetails.Journey

fun ReadingJourneyWithEntries.toUi(): Journey =
    Journey(
        journeyId = journey.journeyId,
        bookId = journey.bookId,
        startDate = journey.startDate,
        endDate = journey.endDate,
        entries = entries.map { it.toUi() }
    )

fun JourneyEntryEntity.toUi(): Entry =
    Entry(
        entryId = entryId,
        date = date,
        pagesRead = pagesRead,
        comment = comment
    )