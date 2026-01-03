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


            // READING -> PLAN TO READ /*TODO doubt*/
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

            // DROPPED -> READING
            ReadingStatus.DROPPED to ReadingStatus.READING ->
                StatusTransitionResult(
                    newStatus = ReadingStatus.READING,
                    sideEffects = listOf(
                        StatusSideEffect.CreateJourney
                    )
                )

            else -> {
                return null
            }
        }

    }

}