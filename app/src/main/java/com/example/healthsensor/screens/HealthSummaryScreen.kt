// screens/HealthSummaryScreen.kt (Simplificado)

package com.example.healthsensor.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun HealthSummaryScreen(
    navController: NavController
    // Eliminamos el viewModel() si no queremos la lógica de datos por ahora
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Healty Summary") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            // --- Gráfico de Presión Arterial (Pág 9) ---
            Text("Blood Pressure (systolic)", style = MaterialTheme.typography.titleLarge)
            Card(modifier = Modifier.fillMaxWidth().height(250.dp)) {
                ChartPlaceholder(title = "Presión Arterial")
            }

            // --- Gráfico de Ritmo Cardíaco (Pág 9) ---
            Text("Heart Rate (bpm)", style = MaterialTheme.typography.titleLarge)
            Card(modifier = Modifier.fillMaxWidth().height(250.dp)) {
                ChartPlaceholder(title = "Ritmo Cardíaco")
            }

            // Agrega otros gráficos de la Pág 4 aquí si es necesario
        }
    }
}

// Componente Placeholder para el gráfico
@Composable
fun ChartPlaceholder(title: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Text("Placeholder: $title (Gráfico Desactivado)", style = MaterialTheme.typography.bodySmall)
    }
}