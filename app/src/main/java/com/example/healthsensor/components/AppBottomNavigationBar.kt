// components/AppBottomNavigationBar.kt

package com.example.healthsensor.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.healthsensor.Routes
import androidx.compose.runtime.getValue

// Define los ítems para las 3 pestañas (Pág 5 del PDF)
data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun AppBottomNavigationBar(navController: NavController) {
    // Definición de los 3 ítems según tu diseño (Pág 5 del PDF)
    val items = listOf(
        // Home
        BottomNavItem("Home", Icons.Filled.Home, Routes.Home.route),
        // Notifications
        BottomNavItem("Notifications", Icons.Filled.Notifications, Routes.Notifications.route),
        // Contacts
        BottomNavItem("Contacts", Icons.Filled.People, Routes.Contacts.route)
    )

    // Observa la ruta actual para resaltar el icono correcto
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = selected,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            // popUpTo se asegura de que, al cambiar de pestaña,
                            // no se acumulen pantallas en el NavHost interno
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}