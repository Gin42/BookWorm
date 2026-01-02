package com.example.bookworm.ui.screens.stats

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookworm.ui.composables.AppBar
import com.example.bookworm.ui.composables.NavBottom
import com.example.bookworm.ui.screens.bookdetails.toFormattedDate
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.BarProperties
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.DotProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.IndicatorCount
import ir.ehsannarmani.compose_charts.models.IndicatorPosition
import ir.ehsannarmani.compose_charts.models.IndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun StatsScreen(
    navController: NavController,
    state: StatsState,
    actions: StatsActions
) {
    Scaffold(
        topBar = { AppBar(navController) },
        bottomBar = { NavBottom(navController) },
    ) { contentPadding ->

        LazyColumn(
            modifier = Modifier.padding(contentPadding)
        ) {
            item {
                val (pagesPerMonth, monthAverage) = actions.calculatePagesPerMonth()
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    // Title
                    Text(
                        "Pages per month",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    // Chart
                    Column(
                        modifier = Modifier
                            .height(400.dp)
                            .fillMaxWidth()
                    ) {
                        PagesPerMonthChart(pagesPerMonth, monthAverage)
                    }

                }
            }
        }
    }
}

@Composable
fun PagesPerMonthChart(
    pagesPerMonth: Map<String, Double>,
    monthAverage: Double
) {

    val hasData = pagesPerMonth.values.any { it > 0.0 }
    if (!hasData) {
        Text(
            text = "No reading data for this year yet",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        return
    }

    val primary = MaterialTheme.colorScheme.primary
    val secondary = MaterialTheme.colorScheme.secondary

    Text(
        "You have an average of ${monthAverage.toInt()} pages read in a month",
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(bottom = 16.dp)
    )

    LineChart(
        data = remember {
            listOf(
                Line(
                    label = "Pages per month",
                    values = pagesPerMonth.values.toList(),
                    color = Brush.verticalGradient(
                        colors = listOf(secondary, primary)
                    ),
                    curvedEdges = true,
                    firstGradientFillColor = primary.copy(alpha = 0.35f),
                    secondGradientFillColor = Color.Transparent,
                    strokeAnimationSpec = tween(
                        durationMillis = 2000,
                        easing = EaseInOutCubic
                    ),
                    gradientAnimationDelay = 1000,
                )
            )
        },
        animationMode = AnimationMode.Together(delayBuilder = { it * 500L }),
        dotsProperties = DotProperties(enabled = true),
        labelProperties = LabelProperties(
            enabled = true,
            textStyle = MaterialTheme.typography.labelSmall,
            padding = 16.dp,
            labels = pagesPerMonth.keys.toList(),
            rotation = LabelProperties.Rotation(
                mode = LabelProperties.Rotation.Mode.Force,
                degree = -45f
            )
        )
    )
}

