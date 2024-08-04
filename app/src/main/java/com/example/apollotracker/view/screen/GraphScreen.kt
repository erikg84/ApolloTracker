package com.example.apollotracker.view.screen

import android.graphics.Color
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.apollotracker.viewmodel.GraphViewModel
import com.example.apollotracker.model.HistoricalDataPoint
import com.example.apollotracker.view.components.ErrorComponent
import com.example.apollotracker.view.components.LoadingComponent
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun GraphScreen() {
    val viewModel: GraphViewModel = hiltViewModel()
    val viewState by viewModel.viewState.collectAsState()

    when {
        viewState.isLoading -> LoadingComponent()
        viewState.isError -> ErrorComponent()
        else -> HistoricalDataGraph(viewState.historicalDataPoints)
    }
}

@Composable
private fun HistoricalDataGraph(historicalDataPoints: List<HistoricalDataPoint>) {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    val entries = historicalDataPoints.mapNotNull {
        val date = it.timestamp?.let { timestamp ->
            try {
                dateFormat.parse(timestamp)?.time?.toFloat()
            } catch (e: Exception) {
                null
            }
        }
        date?.let { timestamp -> Entry(timestamp, it.price ?: 0f) }
    }

    val dataSet = LineDataSet(entries, "Price").apply {
        color = Color.BLUE
        valueTextColor = Color.BLACK
    }
    val lineData = LineData(dataSet)

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { context ->
                LineChart(context).apply {
                    this.data = lineData
                    description.text = "Coin Price Over Time"
                    xAxis.valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            val dateFormatter = SimpleDateFormat("MM/dd", Locale.getDefault())
                            return dateFormatter.format(Date(value.toLong()))
                        }
                    }
                }
            },
            update = { chart ->
                chart.data = lineData
                chart.invalidate()
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}
