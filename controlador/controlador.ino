#include <Stepper.h>

// Número de pasos por vuelta del motor (depende del modelo)
// Ejemplo: 28BYJ-48 = 2048 pasos por revolución, NEMA17 (1.8°) = 200 pasos
const int stepsPerRevolution = 200;  

// Pines de conexión del motor a pasos
// Ajusta estos pines a los que uses en tu Arduino
const int pin1 = 8;  
const int pin2 = 9;  
const int pin3 = 10; 
const int pin4 = 11; 

// Crear objeto motor
Stepper myStepper(stepsPerRevolution, pin1, pin3, pin2, pin4);

void setup() {
  // Velocidad en RPM
  myStepper.setSpeed(60);
  Serial.begin(9600);
}

void loop() {
  Serial.println("Giro en sentido horario...");
  myStepper.step(stepsPerRevolution); // Una vuelta en un sentido
  delay(1000);

  Serial.println("Giro en sentido antihorario...");
  myStepper.step(-stepsPerRevolution); // Una vuelta en sentido contrario
  delay(1000);
}
