// viewmodels/HomeViewModel.kt

package com.example.healthsensor.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

// Modelo de datos para la UI de la Home Screen
data class HealthUiState(
    val heartRate: Int = 0,
    val oxygenSaturation: Int = 0,
    val bloodPressure: String = "No Disponible",
    val bodyTemperature: String = "No Disponible",
    val glucoseLevel: String = "No Disponible",
    val raspberryStatus: String = "Desconectado",
    val raspberryIP: String = "192.33.234.44", // IP fija de la imagen
    val weeklyMonitoringData: List<Float> = emptyList(), // Para el gráfico
    val errorMessage: String? = null
)

class HomeViewModel(
    private val db: FirebaseFirestore = Firebase.firestore
) : ViewModel() {

    private val _uiState = MutableStateFlow(HealthUiState())
    val uiState: StateFlow<HealthUiState> = _uiState

    // Simulación de monitoreo de temperatura de la Raspberry Pi
    private var isMonitoring = false

    init {
        // Iniciar la escucha de datos de salud desde Firestore (CRUD - Read)
        fetchRealtimeHealthData()
        // Cargar los datos para el gráfico inicial (simulado)
        loadWeeklyData()
    }

    private fun fetchRealtimeHealthData() {
        // En un proyecto real, buscarías el documento del usuario actual
        val userId = Firebase.auth.currentUser?.uid ?: return

        db.collection("health_metrics")
            .document(userId) // O un documento que contenga los datos en tiempo real
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    _uiState.value = _uiState.value.copy(errorMessage = "Error al cargar datos de salud: ${e.message}")
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    // Mapear los datos de Firestore al UiState
                    val data = snapshot.data
                    _uiState.value = _uiState.value.copy(
                        heartRate = (data?.get("heartRate") as? Long)?.toInt() ?: 0,
                        oxygenSaturation = (data?.get("oxygenSaturation") as? Long)?.toInt() ?: 0,
                        // ... actualizar otros campos
                    )
                }
            }
    }

    private fun loadWeeklyData() {
        // Simulación de carga de datos para el gráfico de Monitoreo Semanal (Pág 5)
        val simulatedData = (1..4).map { Random.nextFloat() * 100 }
        _uiState.value = _uiState.value.copy(weeklyMonitoringData = simulatedData)
    }

    // Lógica para el botón "Iniciar Monitoreo" (de la Raspberry Pi)
    fun toggleRaspberryMonitoring() {
        isMonitoring = !isMonitoring
        _uiState.value = _uiState.value.copy(
            raspberryStatus = if (isMonitoring) "Conectado y Monitoreando" else "Desconectado"
        )

        // Simulación de flujo de datos de temperatura (similar a la Pág 11)
        if (isMonitoring) {
            viewModelScope.launch {
                while(isMonitoring) {
                    val temp = String.format("%.1f", 35.0 + Random.nextFloat() * 2) // Simula 35.0°C - 37.0°C
                    _uiState.value = _uiState.value.copy(
                        bodyTemperature = "$temp °C"
                    )
                    delay(5000L) // Actualiza cada 5 segundos
                }
            }
        } else {
            _uiState.value = _uiState.value.copy(bodyTemperature = "No Disponible")
        }
    }

    fun scanGlucoseSensor() {
        // Lógica para iniciar el escaneo del sensor de glucosa (BLE, NFC, etc.)
        _uiState.value = _uiState.value.copy(glucoseLevel = "Escaneando...")
        viewModelScope.launch {
            delay(3000L)
            _uiState.value = _uiState.value.copy(glucoseLevel = "105 mg/dL")
        }
    }
}