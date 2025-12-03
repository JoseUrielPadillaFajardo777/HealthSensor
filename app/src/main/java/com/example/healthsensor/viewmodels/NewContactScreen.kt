// screens/NewContactScreen.kt

package com.example.healthsensor.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.healthsensor.viewmodels.ContactsViewModel

@Composable
fun NewContactScreen(
    navController: NavController,
    contactsViewModel: ContactsViewModel = viewModel()
) {
    var name by remember { mutableStateOf("") }
    var relationship by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo Contacto") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Text("Cancelar") // Simula el botón Cancelar de la Pág 8
                    }
                },
                actions = {
                    TextButton(onClick = {
                        // Validación básica
                        if (name.isNotBlank() && phone.isNotBlank()) {
                            contactsViewModel.addContact(name, relationship, phone)
                            navController.popBackStack() // Regresar a la lista de contactos
                        }
                    }) {
                        Text("Guardar") // Simula el botón Guardar de la Pág 8
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).padding(16.dp).fillMaxWidth()) {
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp))
            OutlinedTextField(value = relationship, onValueChange = { relationship = it }, label = { Text("Relación (Familiar, amigo, etc.)") }, modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp))
            OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Número de Teléfono") }, modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp))
        }
    }
}