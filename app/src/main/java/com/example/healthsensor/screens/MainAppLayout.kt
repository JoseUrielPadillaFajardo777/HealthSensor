// screens/MainAppLayout.kt (Ruta Routes.MainAppContainer)

package com.example.healthsensor.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.healthsensor.Routes
import com.example.healthsensor.components.AppBottomNavigationBar

@Composable
fun MainAppLayout(mainNavController: NavController) {
    // Este controlador maneja la navegación DENTRO de las pestañas (Home, Notifications, Contacts)
    val bottomNavController = rememberNavController()

    Scaffold(
        // Pestaña inferior: insertamos el componente creado arriba
        bottomBar = { AppBottomNavigationBar(navController = bottomNavController) }
    ) { paddingValues ->

        // Host de Navegación Anidado para el contenido de las pestañas
        NavHost(
            navController = bottomNavController,
            // Inicia en la pestaña Home
            startDestination = Routes.Home.route,
            Modifier.padding(paddingValues)
        ) {
            // --- Definición de Rutas Internas de la Bottom Bar ---

            // 1. Pestaña HOME
            composable(Routes.Home.route) {
                // Le pasamos el controlador principal para poder navegar a HealthSummary/Configuration, etc.
                HomeScreen(mainNavController = mainNavController)
            }

            // 2. Pestaña NOTIFICATIONS
            composable(Routes.Notifications.route) {
                NotificationsScreen(mainNavController = mainNavController)
            }

            // 3. Pestaña CONTACTS
            composable(Routes.Contacts.route) {
                ContactsScreen(mainNavController = mainNavController)
            }
        }
    }
}

// --- PLACEHOLDERS para la navegación interna (deben estar en screens/) ---
@Composable fun HomeScreen(mainNavController: NavController) { Text("Pantalla Home (Pág 5)") }
@Composable fun NotificationsScreen(mainNavController: NavController) { Text("Pantalla de Notificaciones (Pág 10)") }
@Composable fun ContactsScreen(mainNavController: NavController) { Text("Pantalla de Contactos (Pág 15)") }