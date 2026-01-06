package com.example.bookworm.core.data.models

import com.example.bookworm.R


enum class AchievementName(val type: AchievementType, val number: Int, val imageResId: Int) {
    BookRead1(AchievementType.BookRead, 1, R.drawable.trophy_1 ),
    BookRead5(AchievementType.BookRead, 5, R.drawable.trophy_5),
    BookRead10(AchievementType.BookRead, 10, R.drawable.trophy_10),
    BookRead20(AchievementType.BookRead, 20, R.drawable.trophy_20),
    BookAdded1(AchievementType.BookAdded, 1, R.drawable.trophy_1),
    BookAdded5(AchievementType.BookAdded, 5, R.drawable.trophy_5),
    BookAdded10(AchievementType.BookAdded, 10, R.drawable.trophy_10),
    BookAdded20(AchievementType.BookAdded, 20, R.drawable.trophy_20),
}

enum class AchievementType {
    BookRead, BookAdded
}