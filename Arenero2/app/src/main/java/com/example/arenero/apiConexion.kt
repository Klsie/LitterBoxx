package com.example.arenero

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class ApiConexion {
    private val client = OkHttpClient()
    private val url = "https://areneroiot-dhdbb9hcc0a0g3cs.canadacentral-01.azurewebsites.net/api/registros"

    fun obtenerDatos(onResult: (List<Float>, List<Float>, List<Float>) -> Unit) {
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string() ?: return
                val json = JSONObject(body)
                val lista = json.getJSONArray("registros")

                val pesos = mutableListOf<Float>()
                val distancias = mutableListOf<Float>()
                val limpiezas = mutableListOf<Float>()
                val prediccion = mutableListOf<Float>()

                for (i in 0 until lista.length()) {
                    val item = lista.getJSONObject(i)
                    pesos.add(item.getDouble("peso").toFloat())
                    distancias.add(item.getDouble("distancia").toFloat())
                    limpiezas.add(item.getDouble("limpieza").toFloat())
                }

                // 🔥 Ejecutar en el hilo principal
                android.os.Handler(android.os.Looper.getMainLooper()).post {
                    onResult(pesos, distancias, limpiezas)
                }
            }

        })
    }
    fun activarLimpieza(onResult: (Boolean) -> Unit) {
        val url = "https://areneroiot.azurewebsites.net/api/activar_limpieza"

        val json = """{ "activar": true }"""


        val request = Request.Builder()
            .url(url)
            .post(json.toRequestBody("application/json; charset=utf-8".toMediaType())) // POST vacío
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
