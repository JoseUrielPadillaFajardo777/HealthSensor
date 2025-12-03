// MainAppNavigation.kt

package com.example.healthsensor

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavController
import com.example.healthsensor.screens.* // Importa todas las pantallas de tu carpeta 'screens'

@Composable
fun MainAppNavigation() {
    // El controlador principal para toda la aplicación
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.BrandedSplash.route
    ) {

        // --- 1. Flujo de Splash Screen ---
        composable(Routes.BrandedSplash.route) {
            // La Splash Screen navegará a Login automáticamente
            BrandedSplashScreen(navController = navController)
        }

        // --- 2. Flujo de Autenticación (Seguridad) ---
        composable(Routes.Login.route) {
            LoginScreen(navController = navController)
        }

        composable(Routes.Register.route) {
            RegisterScreen(navController = navController)
        }

        // --- 3. Contenedor Principal (Entrada a la App con Bottom Bar) ---
        composable(Routes.MainAppContainer.route) {
            // Este Layout contiene la Bottom Navigation Bar y el NavHost interno
            MainAppLayout(mainNavController = navController)
        }

        // --- 4. Rutas Secundarias (Acceso desde cualquier parte, fuera de la Bottom Bar) ---

        // Gráficos y Resumen (Pág 9)
        composable(Routes.HealthSummary.route) {
            HealthSummaryScreen(navController = navController)
        }

        // Configuración de la App
        composable(Routes.Configuration.route) {
            ConfigurationScreen(navController = navController)
        }

        composable(Routes.PersonalData.route) {
            PersonalDataScreen(navController = navController)
        }
        // Medidas Corporales (Pág 6)
        composable(Routes.PersonalData.route) {
            PersonalDataScreen(navController = navController)
        }

        // Agregar Nuevo Contacto (Pág 8)
        composable(Routes.NewContact.route) {
            NewContactScreen(navController = navController)
        }

        // Las rutas restantes (LanguageChange, Rating, HowTo)
        composable(Routes.LanguageChange.route) {
            PlaceholderScreen(name = "Cambio de Idioma")
        }
        composable(Routes.Rating.route) {
            PlaceholderScreen(name = "Calificación")
        }
        composable(Routes.HowTo.route) {
            PlaceholderScreen(name = "Tutorial / Cómo Usar")
        }
    }
}


// --- PLACEHOLDERS (DEBEN ESTAR EN screens/ O YA DEFINIDOS PREVIAMENTE) ---

// Para el ejemplo, definimos las funciones que se requieren si aún no las has creado:
@Composable fun PlaceholderScreen(name: String) {
    Text(text = "Pantalla de $name: Implementación Pendiente")
}

// Las pantallas de Login/Registro/Splash que ya te ayudé a crear
@Composable fun BrandedSplashScreen(navController: NavController) {
    PlaceholderScreen(name = "Splash")
}
@Composable fun LoginScreen(navController: NavController) {
    PlaceholderScreen(name = "Login")
}
@Composable fun RegisterScreen(navController: NavController) {
    PlaceholderScreen(name = "Registro")
}

// El contenedor principal (Contiene la Bottom Bar)
@Composable fun MainAppLayout(mainNavController: NavController) {
    Text("Contenedor Principal con Bottom Bar y Navegación Interna")
}

// Pantallas Secundarias
@Composable fun HealthSummaryScreen(navController: NavController) {
    PlaceholderScreen(name = "Resumen de Salud")
}
@Composable fun PersonalDataScreen(navController: NavController) {
    PlaceholderScreen(name = "Medidas Corporales")
}
@Composable fun NewContactScreen(navController: NavController) {
    PlaceholderScreen(name = "Nuevo Contacto")
}
@Composable fun ConfigurationScreen(navController: NavController) {
    PlaceholderScreen(name = "Configuración")
}