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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import android.os.Handler
import android.os.Looper
import android.util.Log


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen() {

    val context = LocalContext.current

    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    var pesos by remember { mutableStateOf(listOf<Float>()) }
    var distancias by remember { mutableStateOf(listOf<Float>()) }
    var limpiezas by remember { mutableStateOf(listOf<Float>()) }

    // Auto actualización cada 10 segundos
    LaunchedEffect(true) {
        while (true) {
            try {
                val (p, d, l) = ApiConexion().obtenerDatos()

                pesos = p
                distancias = d
                limpiezas = l

            } catch (e: Exception) {
                var Mensaje = ("Error en la obtención de datos: ${e.message}")
                showToast(Mensaje)
            }

            // La coroutine espera 30 segundos *después* de que la petición terminó
            delay(30_000)
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
                unidad = "kg",
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
                valor =  limpiezas.firstOrNull()?.toInt()?.toString() ?:"--",
                unidad = "0 No necesaria   1 Necesaria",
                color = if (limpiezas.firstOrNull()?.toInt() == 1) Color(0xFF66BB6A)
                else Color(0xFFEF5350)
            )
        }


        Spacer(modifier = Modifier.height(24.dp))
        var loading by remember { mutableStateOf(false) }

        Button(
            onClick = {
                loading = true

                ApiConexion().activarLimpieza { ok ->
                    Handler(Looper.getMainLooper()).post {

                        loading = false

                        val mensajeToast = if (ok)
                            "Limpieza activada correctamente!"
                        else
                            "Error al activar la limpieza"

                        // showToast AHORA se ejecuta en el Hilo Principal
                        showToast(mensajeToast)
                    }
                }

                ApiConexion().activarLimpieza { exito ->
                    if (exito) {
                        // La solicitud de limpieza fue aceptada por el servidor
                        Log.d("API_CALL", " impieza activada correctamente.")
                    } else {
                        // Fallo de red o el servidor devolvió un código de error (ej. 400, 500)
                        Log.e("API_CALL", "Falló la activación de limpieza.")
                    }
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

        // ----------------------
        // GRÁFICAS
        // ----------------------

       Column(modifier = Modifier

           .verticalScroll(rememberScrollState())
        ) {
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
               "Prediccion de Limpieza",
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
