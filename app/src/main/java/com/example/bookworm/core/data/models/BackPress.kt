package com.example.bookworm.core.data.models

sealed class BackPress {
    object Idle : BackPress()
    object InitialTouch : BackPress()
}