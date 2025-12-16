package com.example.bookworm.ui.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.navigation.NavController
import com.example.bookworm.R
import com.example.bookworm.ui.BookWormRoute


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(navController: NavController, goBack: Boolean = false) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "BookWorm",
                fontFamily = FontFamily(Font(R.font.alegreya_sans_sc_medium)),
                style = MaterialTheme.typography.headlineMedium,
            )
        },
        navigationIcon = {
            if (goBack && navController.previousBackStackEntry != null) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Go Back")
                }
            }
        },
        actions = {
            IconButton(
                onClick = { navController.navigate(BookWormRoute.Setting) }
            ) {
                Icon(Icons.Filled.Settings, contentDescription = "App Settings")
            }
        }
    )
}
