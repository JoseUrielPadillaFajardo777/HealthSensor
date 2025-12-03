// viewmodels/PersonalDataViewModel.kt

package com.example.healthsensor.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Estado de la UI para la pantalla de Datos Personales
data class PersonalDataUiState(
    val weightKg: String = "",
    val heightCm: String = "",
    val chronicDiseases: Map<String, Boolean> = mapOf(
        "Diabetes" to false,
        "Hipertensión" to false,
        "Cardiopatías" to false,
        "Cáncer" to false
        // ... (Agrega más según la Pág 6)
    ),
    val bloodType: String = "",
    val isLoading: Boolean = false,
    val saveSuccess: Boolean = false,
    val errorMessage: String? = null
)

class PersonalDataViewModel(
    private val db: FirebaseFirestore = Firebase.firestore,
    private val auth: FirebaseAuth = Firebase.auth
) : ViewModel() {

    private val _uiState = MutableStateFlow(PersonalDataUiState())
    val uiState: StateFlow<PersonalDataUiState> = _uiState

    init {
        fetchPersonalData()
    }

    // --- 1. READ (Lectura de Datos Personales) ---
    private fun fetchPersonalData() = viewModelScope.launch {
        _uiState.value = _uiState.value.copy(isLoading = true)
        val userId = auth.currentUser?.uid ?: return@launch

        db.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                val data = document.data
                if (data != null) {
                    _uiState.value = _uiState.value.copy(
                        weightKg = (data["weightKg"] as? Double)?.toString() ?: "",
                        heightCm = (data["heightCm"] as? Double)?.toString() ?: "",
                        bloodType = data["bloodType"] as? String ?: "",
                        chronicDiseases = data["chronicDiseases"] as? Map<String, Boolean> ?: _uiState.value.chronicDiseases
                    )
                }
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
            .addOnFailureListener { e ->
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = "Error al cargar datos: ${e.message}")
            }
    }

    // --- 2. UPDATE (Actualización/Guardado de Datos Personales) ---
    fun savePersonalData(
        weight: String, height: String, diseases: Map<String, Boolean>, bloodType: String
    ) = viewModelScope.launch {
        _uiState.value = _uiState.value.copy(isLoading = true, saveSuccess = false)
        val userId = auth.currentUser?.uid ?: return@launch

        // Mapeo de datos a guardar en Firestore
        val dataToSave = mapOf(
            "weightKg" to weight.toDoubleOrNull(),
            "heightCm" to height.toDoubleOrNull(),
            "chronicDiseases" to diseases,
            "bloodType" to bloodType
        )

        db.collection("users").document(userId)
            .set(dataToSave, SetOptions.merge()) // Usa merge para no sobrescribir otros campos
            .addOnSuccessListener {
                _uiState.value = _uiState.value.copy(isLoading = false, saveSuccess = true)
            }
            .addOnFailureListener { e ->
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = "Fallo al guardar: ${e.message}")
            }
    }

    fun updateDiseaseSelection(disease: String, isChecked: Boolean) {
        val updatedMap = _uiState.value.chronicDiseases.toMutableMap()
        updatedMap[disease] = isChecked
        _uiState.value = _uiState.value.copy(chronicDiseases = updatedMap)
    }

    fun updateBloodType(newType: String) {
        _uiState.value = _uiState.value.copy(bloodType = newType)
    }
}