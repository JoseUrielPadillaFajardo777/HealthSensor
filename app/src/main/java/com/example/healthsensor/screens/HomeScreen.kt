// screens/HomeScreen.kt

package com.example.healthsensor.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.healthsensor.Routes
import com.example.healthsensor.viewmodels.HomeViewModel

@Composable
fun HomeScreen(
    mainNavController: NavController,
    homeViewModel: HomeViewModel = viewModel()
) {
    val state by homeViewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        // --- 1. Header (Datos) ---
        Text(text = "Datos", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))

        // --- 2. Tarjeta de Métricas de Salud ---
        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Métricas Dinámicas (Ritmo Cardiaco, Oxígeno) 
                Text(text = "Ritmo Cardiaco: ${state.heartRate} BPM")
                Text(text = "Saturación de Oxígeno: ${state.oxygenSaturation}%")
                Text(text = "Presión Arterial: ${state.bloodPressure}")
                Text(text = "Temperatura Corporal: ${state.bodyTemperature}")
                Text(text = "Nivel de Glucosa: ${state.glucoseLevel}")

                Spacer(modifier = Modifier.height(12.dp))

                // Botón "Escanear Sensor de Glucosa"
                Button(onClick = { homeViewModel.scanGlucoseSensor() }, modifier = Modifier.fillMaxWidth()) {
                    Text("Escanear Sensor de Glucosa")
                }
            }
        }

        // --- 3. Monitoreo de Temperatura (Raspberry Pi) ---
        Text(text = "Monitoreo de Temperatura", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(vertical = 8.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "IP de Raspberry Pi: ${state.raspberryIP}", modifier = Modifier.weight(1f))
            Text(text = state.raspberryStatus, color = if (state.raspberryStatus == "Desconectado") Color.Red else Color.Green)
        }

        // Botón "Iniciar Monitoreo" / "Detener Monitoreo"
        Button(
            onClick = { homeViewModel.toggleRaspberryMonitoring() },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (state.raspberryStatus == "Desconectado") Color.Green else Color.Red
            )
        ) {
            Text(if (state.raspberryStatus == "Desconectado") "Iniciar Monitoreo" else "Detener Monitoreo")
        }

        // Botón "VER RESUMEN DE SALUD" (Navega a la Pág 9: HealthSummary)
        Button(
            onClick = { mainNavController.navigate(Routes.HealthSummary.route) },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Text("VER RESUMEN DE SALUD")
        }

        // --- 4. Gráfico (Monitoreo Semanal) ---
        Text(text = "Monitoreo Semanal", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(vertical = 16.dp))

        // Aquí iría tu componente de gráfico
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color.LightGray)
        ) {
            Text("Placeholder para Gráfico de Barras", modifier = Modifier.align(Alignment.Center))
            // En un proyecto real, usarías una librería de gráficos aquí, por ejemplo:
            // BarChart(data = state.weeklyMonitoringData)
        }
    }
}
