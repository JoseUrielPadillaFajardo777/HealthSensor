package com.example.healthsensor.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.healthsensoravance.R
import com.example.healthsensoravance.Routes
import com.example.healthsensoravance.viewmodels.RegisterState
import com.example.healthsensoravance.viewmodels.RegisterViewModel

@Composable
fun RegisterScreen(
    navController: NavController,
    registerViewModel: RegisterViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val registerState by registerViewModel.registerState.collectAsState()

    // Manejar el cambio de estado
    LaunchedEffect(key1 = registerState) {
        when (registerState) {
            is RegisterState.Success -> {
                // Navega a Home o vuelve a Login
                navController.navigate(Routes.Home.route) {
                    popUpTo(Routes.Register.route) { inclusive = true }
                }
            }
            is RegisterState.Error -> {
                println("REGISTRO ERROR: ${(registerState as RegisterState.Error).message}")
                // Aquí deberías mostrar un Snackbar o un AlertDialog con el error
            }
            else -> Unit
        }
    }

    // Contenedor principal para permitir el scroll
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 32.dp)
                .wrapContentSize(Alignment.Center) // Centra el contenido si no hay scroll
        ) {

            // Icono y Título
            Image(
                painter = painterResource(id = R.drawable.app_icon),
                contentDescription = "Register",
                modifier = Modifier.size(80.dp).align(Alignment.CenterHorizontally).padding(bottom = 16.dp)
            )
            Text(text = "Register", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 32.dp))

            // Campos de Texto (E-mail, Name, Last Name, Phone Number, New Password)
            // Se recomienda usar un Scrollable Column para manejar el teclado
            Column(modifier = Modifier.weight(1f).fillMaxWidth()) {
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("E-mail") }, modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), singleLine = true)
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), singleLine = true)
                OutlinedTextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Last Name") }, modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), singleLine = true)
                OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone Number") }, modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), singleLine = true)
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("New Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                    singleLine = true
                )
            }


            // Botón Register
            Button(
                onClick = {
                    registerViewModel.signUpAndSave(email, password, name, lastName, phone)
                },
                modifier = Modifier.fillMaxWidth().height(50.dp).padding(bottom = 8.dp),
                enabled = registerState !is RegisterState.Loading
            ) {
                if (registerState is RegisterState.Loading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                } else {
                    Text("REGISTER")
                }
            }

            // Botón Cancel
            TextButton(onClick = { navController.popBackStack() }, modifier = Modifier.fillMaxWidth()) {
                Text("Cancel")
            }
        }
    }
}