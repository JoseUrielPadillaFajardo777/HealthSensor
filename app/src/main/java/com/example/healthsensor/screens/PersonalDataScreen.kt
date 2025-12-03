// screens/PersonalDataScreen.kt

package com.example.healthsensor.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.healthsensor.viewmodels.PersonalDataViewModel

@Composable
fun PersonalDataScreen(
    navController: NavController,
    viewModel: PersonalDataViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    // Mostrar Toast/Snackbar si el guardado es exitoso
    LaunchedEffect(state.saveSuccess) {
        if (state.saveSuccess) {
            // Muestra un mensaje de éxito
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Datos Personales") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            viewModel.savePersonalData(
                                state.weightKg, state.heightCm, state.chronicDiseases, state.bloodType
                            )
                        },
                        enabled = !state.isLoading
                    ) {
                        Text("Guardar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {

            // Pestañas (para cambiar entre Pág 6 y Pág 7)
            TabRow(selectedTabIndex = selectedTabIndex) {
                Tab(selected = selectedTabIndex == 0, onClick = { selectedTabIndex = 0 }, text = { Text("Medidas/Enfermedades") })
                Tab(selected = selectedTabIndex == 1, onClick = { selectedTabIndex = 1 }, text = { Text("Tipo de Sangre") })
            }

            // Contenido de las Pestañas
            Box(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                when (selectedTabIndex) {
                    0 -> MeasuresAndDiseasesTab(viewModel, state.weightKg, state.heightCm, state.chronicDiseases)
                    1 -> BloodTypeTab(viewModel, state.bloodType)
                }
            }
        }
    }
}

// Contenido para la Pestaña 1: Medidas Corporales y Enfermedades (Pág 6)
@Composable
fun MeasuresAndDiseasesTab(viewModel: PersonalDataViewModel, weight: String, height: String, diseases: Map<String, Boolean>) {
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {

        // Medidas Corporales
        Text("Medidas corporales", style = MaterialTheme.typography.titleLarge)
        OutlinedTextField(value = weight, onValueChange = { viewModel.updateBloodType(it) /* Usamos updateBloodType temporalmente para weight */ }, label = { Text("Peso (kg)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = height, onValueChange = { viewModel.updateBloodType(it) /* Usamos updateBloodType temporalmente para height */ }, label = { Text("Estatura (cm)") }, modifier = Modifier.fillMaxWidth())

        // Enfermedades Crónicas
        Text("Enfermedades Crónicas", style = MaterialTheme.typography.titleLarge)
        diseases.keys.forEach { disease ->
            Row(
                modifier = Modifier.fillMaxWidth().clickable { viewModel.updateDiseaseSelection(disease, !diseases[disease]!!) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(disease, modifier = Modifier.weight(1f))
                Checkbox(
                    checked = diseases[disease] ?: false,
                    onCheckedChange = { isChecked -> viewModel.updateDiseaseSelection(disease, isChecked) }
                )
            }
        }

        // Campo Otros (Placeholder)
        OutlinedTextField(value = "", onValueChange = { /* otros */ }, label = { Text("Otros") }, modifier = Modifier.fillMaxWidth())
    }
}

// Contenido para la Pestaña 2: Tipo de Sangre (Pág 7)
@Composable
fun BloodTypeTab(viewModel: PersonalDataViewModel, selectedType: String) {
    val bloodTypes = listOf("A+", "A-", "O+", "O-", "AB+", "AB-", "B+", "B-")

    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Tipo de sangre", style = MaterialTheme.typography.titleLarge)

        // Grupo de radio buttons (o similar) para el tipo de sangre
        Column(Modifier.selectableGroup()) {
            bloodTypes.chunked(2).forEach { rowTypes ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                    rowTypes.forEach { type ->
                        Row(
                            Modifier
                                .weight(1f)
                                .selectable(
                                    selected = (type == selectedType),
                                    onClick = { viewModel.updateBloodType(type) },
                                    role = Role.RadioButton
                                )
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = (type == selectedType), onClick = null)
                            Spacer(Modifier.width(8.dp))
                            Text(text = type)
                        }
                    }
                }
            }
        }
    }
}