package com.example.arenero.screens

import android.graphics.Color
import android.graphics.Color.WHITE
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BarChartView(
    title: String,
    values: List<Float>,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            BarChart(context).apply {

                // Fondo moderno oscuro
                setBackgroundColor(Color.parseColor("#121212"))
                setPadding(30, 30, 30, 30)

                setDrawGridBackground(false)
                setDrawBorders(false)

                // Animación
                animateY(900)

                // Eje X
                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    textColor = Color.parseColor("#B0BEC5")
                    setDrawGridLines(false)
                    setDrawAxisLine(false)
                    granularity = 1f
                }

                // Eje Y
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
                BarEntry(index.toFloat(), value)
            }

            val dataSet = BarDataSet(entries, title).apply {

                // Barra naranja moderna
                color = Color.parseColor("#02801C")
                valueTextColor = Color.valueOf(WHITE).toArgb()
                valueTextSize = 12f

                // Selección suave
                highLightAlpha = 60

                // Bordes redondeados (se ve más bonito)
                barBorderWidth = 0f
            }

            val barData = BarData(dataSet).apply {
                barWidth = 0.45f
            }

            chart.data = barData

            // Deja espacio entre barras para mejor estética
            chart.setFitBars(true)

            chart.invalidate()
        }
    )
}
