package com.example.bookworm.ui.screens.stats

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookworm.ui.composables.AppBar
import com.example.bookworm.ui.composables.NavBottom
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.Line

@Composable
fun StatsScreen(navController: NavController) {
    Scaffold(
        topBar = { AppBar(navController) },
        bottomBar = { NavBottom(navController) },
    ) { contentPadding ->

        LazyColumn(
            modifier = Modifier.padding(contentPadding)
        ) {
            item {
                Column(
                    modifier = Modifier.padding(vertical = 15.dp)
                ) {
                    Text(
                        "Pages per session",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(8.dp)
                    )


                    Text(
                        "You have an average of 40 pages per session",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(8.dp)
                    )

                    Box(
                        modifier = Modifier
                            .height(300.dp)
                            .padding(horizontal = 22.dp)
                    ) {
                        LineChart(
                            data = remember {
                                listOf(
                                    Line(
                                        label = "Windows",
                                        values = listOf(28.0, 41.0, 5.0, 10.0, 35.0),
                                        color = SolidColor(Color(0xFF23af92)),
                                        firstGradientFillColor = Color(0xFF2BC0A1).copy(alpha = .5f),
                                        secondGradientFillColor = Color.Transparent,
                                        strokeAnimationSpec = tween(2000, easing = EaseInOutCubic),
                                        gradientAnimationDelay = 1000,
                                    )
                                )
                            },
                            animationMode = AnimationMode.Together(delayBuilder = {
                                it * 500L
                            }),
                        )
                    }
                }

            }

            item {
                Column(
                    modifier = Modifier.padding(vertical = 15.dp)
                ) {
                    Text(
                        "Time to finish book",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(8.dp)
                    )

                    Text(
                        "On average, you take 20 days to finish a book",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(8.dp)
                    )

                    Box(
                        modifier = Modifier
                            .height(300.dp)
                    ) {
                        LineChart(
                            data = listOf(
                                Line(
                                    label = "Sample Data",
                                    values = listOf(10.0, 25.0, 15.0, 30.0, 20.0),
                                    color = SolidColor(Color.Blue)
                                )
                            )
                        )
                    }
                }

            }
        }
    }
}