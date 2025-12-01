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

                // Background moderno
                setBackgroundColor(Color.parseColor("#121212"))
                setPadding(30, 30, 30, 30)
                setDrawGridBackground(false)
                setDrawBorders(false)
                extraTopOffset = 10f

                // Animación suave
                animateX(900)

                // ---- Eje X ----
                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    textColor = Color.parseColor("#B0BEC5")
                    setDrawGridLines(false)
                    setDrawAxisLine(false)
                }

                // ---- Eje Y ----
                axisLeft.apply {
                    textColor = Color.parseColor("#B0BEC5")
                    gridColor = Color.parseColor("#263238")
                    gridLineWidth = 0.6f
                    setDrawAxisLine(false)
                }

                axisRight.isEnabled = false

                // Leyenda moderna
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

            // --- LineDataSet moderno ---
            val dataSet = LineDataSet(entries, title).apply {

                // Color línea celeste moderno
                color = Color.parseColor("#80D8FF")
                lineWidth = 3f

                // Sin puntos (más profesional)
                setDrawCircles(false)

                // Texto de valores desactivado
                setDrawValues(false)

                // Curva suave
                mode = LineDataSet.Mode.CUBIC_BEZIER

                // Gradiente debajo de la línea
                setDrawFilled(true)
                fillDrawable = android.graphics.drawable.GradientDrawable(
                    android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM,
                    intArrayOf(
                        Color.parseColor("#40B3E5FC"), // arriba semi-transparente
                        Color.parseColor("#00121212")  // abajo casi invisible
                    )
                )
            }

            val lineData = LineData(dataSet)

            chart.data = lineData
            chart.invalidate()
        }
    )
}

