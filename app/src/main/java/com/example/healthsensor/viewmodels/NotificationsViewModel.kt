// viewmodels/NotificationsViewModel.kt

package com.example.healthsensor.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Modelo para el estado de las notificaciones
data class NotificationUiState(
    val notifications: List<String> = listOf("No hay notificaciones"), // Pág 10
    val emergencyNumber: String = "4491234567" // Número de ejemplo. Idealmente, sería el de un contacto.
)

class NotificationsViewModel(
    // Podrías usar Firestore para leer alertas críticas en tiempo real
    private val db: FirebaseFirestore = Firebase.firestore,
    private val auth: FirebaseAuth = Firebase.auth
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationUiState())
    val uiState: StateFlow<NotificationUiState> = _uiState

    init {
        // Simulación: Cargar el número de contacto principal (si lo hubiera)
        // y generar notificaciones de prueba.
        generateDummyAlerts()
    }

    private fun generateDummyAlerts() {
        // En un caso real, esto vendría de Firestore o de una lógica de backend/sensores.
        _uiState.value = _uiState.value.copy(
            notifications = listOf(
                "Alerta: Ritmo cardíaco bajo (45 BPM) - 09:30 AM",
                "Alerta: Nivel de glucosa elevado (190 mg/dL) - 08:00 AM"
            )
        )
    }

    // Función para manejar la llamada de emergencia (la lógica de la llamada se hace en la UI/Activity)
    fun callEmergency() {
        // Aquí puedes registrar el evento en Firebase Analytics o Firestore.
        println("Evento registrado: Iniciando llamada de emergencia a ${_uiState.value.emergencyNumber}")
    }
}