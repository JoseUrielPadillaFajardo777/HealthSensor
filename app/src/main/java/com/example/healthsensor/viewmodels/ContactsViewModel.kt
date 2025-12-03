// viewmodels/ContactsViewModel.kt

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

// Modelo de datos del Contacto (similar a José Lopez Torres, Pág 15)
data class EmergencyContact(
    val id: String = "", // Usado como ID de documento en Firestore
    val name: String = "",
    val relationship: String = "",
    val phone: String = ""
)

class ContactsViewModel(
    private val db: FirebaseFirestore = Firebase.firestore,
    private val auth: FirebaseAuth = Firebase.auth
) : ViewModel() {

    private val _contacts = MutableStateFlow<List<EmergencyContact>>(emptyList())
    val contacts: StateFlow<List<EmergencyContact>> = _contacts

    init {
        fetchContacts() // Lee los contactos al iniciar el ViewModel
    }

    // --- 1. READ (Lectura/Consulta de Contactos) ---
    private fun fetchContacts() {
        val userId = auth.currentUser?.uid ?: return

        db.collection("users").document(userId)
            .collection("emergency_contacts")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    println("Error al escuchar contactos: $e")
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val contactList = snapshot.documents.mapNotNull { document ->
                        // Mapea el documento a nuestro modelo de datos, incluyendo el ID
                        document.toObject(EmergencyContact::class.java)?.copy(id = document.id)
                    }
                    _contacts.value = contactList
                }
            }
    }

    // --- 2. CREATE (Creación/Registro de un Contacto) ---
    fun addContact(name: String, relationship: String, phone: String) = viewModelScope.launch {
        val userId = auth.currentUser?.uid ?: return@launch
        val newContact = EmergencyContact(name = name, relationship = relationship, phone = phone)

        db.collection("users").document(userId)
            .collection("emergency_contacts")
            .add(newContact) // Firestore asigna un ID automáticamente
            .addOnSuccessListener {
                println("Contacto agregado exitosamente.")
            }
            .addOnFailureListener { e ->
                println("Fallo al agregar contacto: $e")
            }
    }

    // --- 3. DELETE (Eliminación de un Contacto) ---
    fun deleteContact(contactId: String) = viewModelScope.launch {
        val userId = auth.currentUser?.uid ?: return@launch

        db.collection("users").document(userId)
            .collection("emergency_contacts").document(contactId)
            .delete()
            .addOnSuccessListener {
                println("Contacto eliminado exitosamente.")
            }
            .addOnFailureListener { e ->
                println("Fallo al eliminar contacto: $e")
            }
    }

    // Nota: Para el UPDATE, se usaría un método set(contact, SetOptions.merge()) en lugar de add(contact).
}