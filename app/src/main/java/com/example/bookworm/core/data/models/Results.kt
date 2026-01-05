package com.example.bookworm.core.data.models

enum class AuthenticationResults {
    UsernameTaken,
    CannotSubmit,
    WrongCredentials,
    Success,
}

enum class AddBookResults {
    Success,
    BookPresent,
    CannotSubmit,
    InvalidPages
}

enum class AddEntryResults {
    InvalidDate,
    InvalidPage,
    Success,
    CannotSubmit,
}