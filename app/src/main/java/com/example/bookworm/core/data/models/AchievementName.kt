package com.example.bookworm.core.data.models


enum class AchievementName(val type: AchievementType, val number: Int) {
    BookRead1(AchievementType.BookRead, 1),
    BookRead5(AchievementType.BookRead, 5),
    BookRead10(AchievementType.BookRead, 10),
    BookRead20(AchievementType.BookRead, 20),
    BookAdded1(AchievementType.BookAdded, 1),
    BookAdded5(AchievementType.BookAdded, 5),
    BookAdded10(AchievementType.BookAdded, 10),
    BookAdded20(AchievementType.BookAdded, 20),
}

enum class AchievementType {
    BookRead, BookAdded
}