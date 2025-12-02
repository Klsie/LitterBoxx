package com.example.arenero.screens

import android.graphics.Color
import android.graphics.Paint
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

@Composable
fun LineChartView(
    title: String,
    values: List<Float>,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            LineChart(context).apply {
                setBackgroundColor(Color.parseColor("#121212"))
                setPadding(30, 30, 30, 30)
                setDrawGridBackground(false)
                setDrawBorders(false)
                extraTopOffset = 10f
                animateX(900)
                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    textColor = Color.parseColor("#B0BEC5")
                    setDrawGridLines(false)
                    setDrawAxisLine(false)
                }
                axisLeft.apply {
                    textColor = Color.parseColor("#B0BEC5")
                    gridColor = Color.parseColor("#263238")
                    gridLineWidth = 0.6f
                    setDrawAxisLine(false)
                }
                axisRight.isEnabled = false
                legend.textColor = Color.WHITE
                description.isEnabled = false
            }
        },
        update = { chart ->
            if (values.isEmpty()) {
                chart.clear()
                return@AndroidView
            }
            val entries = values.mapIndexed { index, value ->
                Entry(index.toFloat(), value)
            }
            val dataSet = LineDataSet(entries, title).apply {
                color = Color.parseColor("#80D8FF")
                lineWidth = 3f
                setDrawCircles(false)
                setDrawValues(false)
                mode = LineDataSet.Mode.CUBIC_BEZIER
                setDrawFilled(true)
                fillDrawable = android.graphics.drawable.GradientDrawable(
                    android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM,
                    intArrayOf(
                        Color.parseColor("#40B3E5FC"),
                        Color.parseColor("#00121212")
                    )
                )
            }
            val lineData = LineData(dataSet)
            chart.data = lineData
            chart.invalidate()
        }
    )
}

