// screens/NotificationsScreen.kt

package com.example.healthsensor.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.healthsensor.viewmodels.NotificationsViewModel

@Composable
fun NotificationsScreen(
    mainNavController: NavController,
    notificationsViewModel: NotificationsViewModel = viewModel()
) {
    val state by notificationsViewModel.uiState.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notificaciones") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            // --- Botón de Llamada a Emergencias (Pág 10) ---
            Button(
                onClick = {
                    notificationsViewModel.callEmergency()
                    initiateCall(context, state.emergencyNumber)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Llamar a Emergencias", fontWeight = FontWeight.Bold)
            }

            Divider()

            // --- Lista de Notificaciones ---
            if (state.notifications.isNotEmpty() && state.notifications.first() != "No hay notificaciones") {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.notifications) { notification ->
                        NotificationItem(notification = notification)
                    }
                }
            } else {
                Text(
                    text = "No hay notificaciones", // Texto de Pág 10
                    modifier = Modifier.padding(32.dp).fillMaxWidth()
                )
            }
        }
    }
}

// Función para iniciar la llamada telefónica (Intención de Android)
fun initiateCall(context: Context, number: String) {
    try {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "No se puede iniciar la llamada. Verifique el permiso.", Toast.LENGTH_LONG).show()
    }
}

@Composable
fun NotificationItem(notification: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(Icons.Filled.Warning, contentDescription = "Alerta", tint = MaterialTheme.colorScheme.warning)
        Text(notification)
    }
    Divider()
}