package com.example.healthsensor.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Modelo de datos para guardar en Firestore
data class UserProfile(
    val email: String = "",
    val name: String = "",
    val lastName: String = "",
    val phone: String = ""
)

class RegisterViewModel(
    private val auth: FirebaseAuth = Firebase.auth,
    private val db: FirebaseFirestore = Firebase.firestore
) : ViewModel() {

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Initial)
    val registerState: StateFlow<RegisterState> = _registerState

    fun signUpAndSave(
        email: String,
        password: String,
        name: String,
        lastName: String,
        phone: String
    ) {
        _registerState.value = RegisterState.Loading

        viewModelScope.launch {
            // 1. Crear usuario con Firebase Authentication
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { authTask ->
                    if (authTask.isSuccessful) {
                        val userId = authTask.result?.user?.uid ?: return@addOnCompleteListener

                        // 2. Guardar datos adicionales en Firestore (CRUD - Create)
                        val userProfile = UserProfile(email, name, lastName, phone)

                        db.collection("users")
                            .document(userId) // Usamos el UID de Firebase Auth como ID del documento
                            .set(userProfile)
                            .addOnSuccessListener {
                                // Registro y guardado de perfil exitoso
                                _registerState.value = RegisterState.Success
                            }
                            .addOnFailureListener { e ->
                                // Error al guardar en Firestore
                                _registerState.value = RegisterState.Error("Registro exitoso, pero fallo al guardar perfil: ${e.message}")
                            }
                    } else {
                        // Error de autenticación (ej: Email ya en uso, contraseña débil)
                        _registerState.value = RegisterState.Error(authTask.exception?.message ?: "Error desconocido")
                    }
                }
        }
    }
}

sealed class RegisterState {
    object Initial : RegisterState()
    object Loading : RegisterState()
    object Success : RegisterState()
    data class Error(val message: String) : RegisterState()
}