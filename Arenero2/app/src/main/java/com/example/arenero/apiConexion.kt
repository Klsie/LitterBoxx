package com.example.arenero

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray

class ApiConexion {
    private val client = OkHttpClient()
    private val url = "https://areneroiot-dhdbb9hcc0a0g3cs.canadacentral-01.azurewebsites.net/api/registros"


    // Asumiendo que esta función se llama desde un CoroutineScope
    suspend fun obtenerDatos(): Triple<List<Float>, List<Float>, List<Float>> =
        withContext(Dispatchers.IO) { // Ejecuta el trabajo de red en el hilo IO
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            response.use {
                if (!response.isSuccessful) {
                    throw IOException("Error HTTP: ${response.code}")
                }
                val body = response.body?.string() ?: throw IOException("Cuerpo de respuesta vacío")
                try {
                    val json = JSONObject(body)
                    val lista = json.optJSONArray("registros") ?: JSONArray()
                    if (lista.length() == 0) {
                        return@withContext Triple(emptyList(), emptyList(), emptyList())
                    }
                    val pesos = mutableListOf<Float>()
                    val distancias = mutableListOf<Float>()
                    val limpiezas = mutableListOf<Float>()
                    val numElementos = lista.length()
                    val maxIteraciones = minOf(numElementos, 10)
                    for (i in 0 until maxIteraciones) {
                        val item = lista.getJSONObject(i)
                        pesos.add(item.optDouble("peso", 0.0).toFloat())
                        distancias.add(item.optDouble("distancia", 0.0).toFloat())
                        limpiezas.add(item.optDouble("prediccion", 0.0).toFloat())
                    }
                    return@withContext Triple(pesos, distancias, limpiezas)
                } catch (e: Exception) {
                    throw RuntimeException("Fallo al parsear JSON", e)
                }
            }
        }
    fun activarLimpieza(onResult: (Boolean) -> Unit) {
        val url = "https://areneroiot-dhdbb9hcc0a0g3cs.canadacentral-01.azurewebsites.net/api/activar_limpieza"
        val json = """{ "activar": true }"""
        val request = Request.Builder()
            .url(url)
            .post(json.toRequestBody("application/json; charset=utf-8".toMediaType()))
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                onResult(false)
            }
            override fun onResponse(call: Call, response: Response) {
                onResult(response.isSuccessful)
            }
        })
    }

}
