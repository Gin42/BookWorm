package com.example.bookworm.core.data.models.usecase

import com.example.bookworm.core.data.models.ReadingStatus

sealed interface StatusSideEffect {
    data object CreateJourney : StatusSideEffect
    data object CloseJourney : StatusSideEffect
    data object DropJourney : StatusSideEffect
}

data class StatusTransitionResult(
    val newStatus: ReadingStatus,
    val sideEffects: List<StatusSideEffect>
)

class ReadingStatusStateMachine {

    fun transition(
        current: ReadingStatus,
        target: ReadingStatus,
    ): StatusTransitionResult? {

        if (current == target) return null

        return when (current to target) {

            //PLAN_TO_READ -> READING
            ReadingStatus.PLAN_TO_READ to ReadingStatus.READING ->
                StatusTransitionResult(
                    newStatus = ReadingStatus.READING,
                    sideEffects = listOf(
                        StatusSideEffect.CreateJourney
                    )
                )

            //PLAN_TO_READ -> FINISHED
            ReadingStatus.PLAN_TO_READ to ReadingStatus.FINISHED ->
                StatusTransitionResult(
                    newStatus = ReadingStatus.FINISHED,
                    sideEffects = emptyList()
                )
            //PLAN_TO_READ -> DROPPED
            ReadingStatus.PLAN_TO_READ to ReadingStatus.DROPPED ->
                StatusTransitionResult(
                    newStatus = ReadingStatus.DROPPED,
                    sideEffects = emptyList()
                )

            // READING -> FINISHED
            ReadingStatus.READING to ReadingStatus.FINISHED ->
                StatusTransitionResult(
                    newStatus = ReadingStatus.FINISHED,
                    sideEffects = listOf(
                        StatusSideEffect.CloseJourney
                    )
                )

            // READING -> DROPPED
            ReadingStatus.READING to ReadingStatus.DROPPED ->
                StatusTransitionResult(
                    newStatus = ReadingStatus.DROPPED,
                    sideEffects = listOf(
                        StatusSideEffect.DropJourney
                    )
                )



            ReadingStatus.READING to ReadingStatus.PLAN_TO_READ ->
                StatusTransitionResult(
                    newStatus = ReadingStatus.PLAN_TO_READ,
                    sideEffects = listOf(
                        StatusSideEffect.CloseJourney
                    )
                )

            // FINISHED -> READING
            ReadingStatus.FINISHED to ReadingStatus.READING ->
                StatusTransitionResult(
                    newStatus = ReadingStatus.READING,
                    sideEffects = listOf(
                        StatusSideEffect.CreateJourney
                    )
                )

            // FINISHED -> PLAN_TO_READ
            ReadingStatus.FINISHED to ReadingStatus.PLAN_TO_READ ->
                StatusTransitionResult(
                    newStatus = ReadingStatus.PLAN_TO_READ,
                    sideEffects = emptyList()
                )

            // FINISHED -> DROPPED
            ReadingStatus.FINISHED to ReadingStatus.DROPPED ->
                StatusTransitionResult(
                    newStatus = ReadingStatus.DROPPED,
                    sideEffects = emptyList()
                )

            // DROPPED -> READING
            ReadingStatus.DROPPED to ReadingStatus.READING ->
                StatusTransitionResult(
                    newStatus = ReadingStatus.READING,
                    sideEffects = listOf(
                        StatusSideEffect.CreateJourney
                    )
                )

            // DROPPED -> FINISHED
            ReadingStatus.DROPPED to ReadingStatus.FINISHED ->
                StatusTransitionResult(
                    newStatus = ReadingStatus.FINISHED,
                    sideEffects = emptyList()
                )

            // DROPPED -> PLAN_TO_READ
            ReadingStatus.DROPPED to ReadingStatus.PLAN_TO_READ ->
                StatusTransitionResult(
                    newStatus = ReadingStatus.PLAN_TO_READ,
                    sideEffects = emptyList()
                )

            else -> {
                return null
            }
        }

    }

}