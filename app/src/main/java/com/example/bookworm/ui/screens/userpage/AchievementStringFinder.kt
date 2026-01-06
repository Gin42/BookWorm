package com.example.bookworm.ui.screens.userpage

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.bookworm.R
import com.example.bookworm.core.data.models.AchievementName

class AchievementPositions(
) {
    companion object {
        const val ACHIEVEMENT_NAME: Int = 0
        const val ACHIEVEMENT_CONDITION = 1
    }
}

@Composable
fun achievementStringFinder(name: String): Array<String> {
    when (AchievementName.valueOf(name)) {
        AchievementName.BookRead1 -> {
            return arrayOf(
                stringResource(R.string.Read1_ach_name),
                stringResource(R.string.Read1_ach_desc)
            )
        }
        AchievementName.BookRead5 -> {
            return arrayOf(
                stringResource(R.string.Read5_ach_name),
                stringResource(R.string.Read5_ach_desc)
            )
        }
        AchievementName.BookRead10 -> {
            return arrayOf(
                stringResource(R.string.Read10_ach_name),
                stringResource(R.string.Read10_ach_desc)
            )
        }
        AchievementName.BookRead20 -> {
            return arrayOf(
                stringResource(R.string.Read20_ach_name),
                stringResource(R.string.Read20_ach_desc)
            )
        }
        AchievementName.BookAdded1 -> {
            return arrayOf(
                stringResource(R.string.Add1_ach_name),
                stringResource(R.string.Add1_ach_desc)
            )
        }
        AchievementName.BookAdded5 -> {
            return arrayOf(
                stringResource(R.string.Add5_ach_name),
                stringResource(R.string.Add5_ach_desc)
            )
        }
        AchievementName.BookAdded10 -> {
            return arrayOf(
                stringResource(R.string.Add10_ach_name),
                stringResource(R.string.Add10_ach_desc)
            )
        }
        AchievementName.BookAdded20 -> {
            return arrayOf(
                stringResource(R.string.Add20_ach_name),
                stringResource(R.string.Add20_ach_desc)
            )
        }
    }
}