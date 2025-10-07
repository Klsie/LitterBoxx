#include <WiFi.h>
#include <HTTPClient.h>

// Credenciales WiFi
const char* ssid = "Fell_dragon_grima";
const char* password = "Peni_parker_b3st";

// Dirección de tu API en Azure
String serverName = "https://areneroiot-dhdbb9hcc0a0g3cs.canadacentral-01.azurewebsites.net/api/datos";

void setup() {
  Serial.begin(115200);

  // Conectar a WiFi
  WiFi.begin(ssid, password);
  Serial.println("Conectando a WiFi...");
  
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("\nConectado a WiFi");
}

void loop() {
  if (WiFi.status() == WL_CONNECTED) {
    HTTPClient http;

    http.begin(serverName);  // Inicia conexión HTTP
    http.addHeader("Content-Type", "application/json"); // Indicamos que mandamos JSON

    // JSON con datos simulados
    String json = "{\"peso\": 3.5, \"distancia\": 15, \"estado\": \"OK\"}";

    // Enviar POST
    int httpResponseCode = http.POST(json);

    if (httpResponseCode > 0) {
      Serial.print("Código de respuesta: ");
      Serial.println(httpResponseCode);
      String respuesta = http.getString();
      Serial.println("Respuesta del servidor: " + respuesta);
    } else {
      Serial.print("Error en POST: ");
      Serial.println(httpResponseCode);
    }

    http.end();  // Terminar conexión
  } else {
    Serial.println("WiFi desconectado");
  }

  delay(10000); // Espera 10 seg antes de mandar otra petición
}
