package com.example.arenero.screens

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.arenero.ApiConexion
import kotlinx.coroutines.delay
import androidx.compose.foundation.background
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen() {

    var pesos by remember { mutableStateOf(listOf<Float>()) }
    var distancias by remember { mutableStateOf(listOf<Float>()) }
    var limpiezas by remember { mutableStateOf(listOf<Float>()) }

    // Auto actualización cada 10 segundos
    LaunchedEffect(true) {
        while (true) {
            ApiConexion().obtenerDatos { p, d, l ->
                pesos = p
                distancias = d
                limpiezas = l
            }
            delay(10_000) // cada 10 segundos
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF101010))
            .padding(16.dp)
    ) {

        // ----------------------
        // HEADER
        // ----------------------
        Text(
            "Panel del Arenero IoT",
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        // ----------------------
        // CARDS DE INDICADORES
        // ----------------------
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {

            SensorCard(
                titulo = "Peso",
                valor = pesos.firstOrNull()?.toString() ?: "--",
                unidad = "g",
                color = Color(0xFF42A5F5)
            )

            SensorCard(
                titulo = "Distancia",
                valor = distancias.firstOrNull()?.toString() ?: "--",
                unidad = "cm",
                color = Color(0xFFFFC107)
            )

            SensorCard(
                titulo = "Limpieza",
                valor = limpiezas.firstOrNull()?.toString() ?:"--",
                unidad = "0 No necesaria   1 Necesaria",
                color = if (limpiezas.firstOrNull() == 1f) Color(0xFF66BB6A)
                else Color(0xFFEF5350)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        var loading by remember { mutableStateOf(false) }
        var mensaje by remember { mutableStateOf("") }

        Button(
            onClick = {
                loading = true
                mensaje = ""

                ApiConexion().activarLimpieza { ok ->
                    loading = false
                    mensaje = if (ok)
                        "Limpieza activada correctamente!"
                    else
                        "Error al activar la limpieza"
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF80D8FF)
            )
        ) {
            Text(text = if (loading) "Activando..." else "Iniciar limpieza")
        }

        if (mensaje.isNotEmpty()) {
        }


        // ----------------------
        // GRÁFICAS
        // ----------------------

        Text(
            "Historial de Peso",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium
        )
        LineChartView(
            title = "Peso del gato (g)",
            values = pesos,
            modifier = Modifier
                .height(220.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            "Distancia Detectada",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium
        )
        LineChartView(
            title = "Distancia (cm)",
            values = distancias,
            modifier = Modifier
                .height(220.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            "Eventos de Limpieza",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium
        )
        BarChartView(
            title = "Eventos de limpieza",
            values = limpiezas,
            modifier = Modifier
                .height(220.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
fun Limpieza(
    titulo: String
){

}


@Composable
fun SensorCard(
    titulo: String,
    valor: String,
    unidad: String,
    color: Color
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1C1C1C)
        ),
        modifier = Modifier
            .width(110.dp)
            .height(110.dp)
    ) {
        Column(
            Modifier.padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                titulo,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                valor,
                color = color,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                unidad,
                color = Color(0xFFB0BEC5),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
