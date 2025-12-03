// Routes.kt
package com.example.healthsensor

sealed class Routes(val route: String) {
    // Rutas de Inicio y Seguridad
    object BrandedSplash : Routes("branded_splash")
    object Login : Routes("login_screen")
    object Register : Routes("register_screen")

    // Contenedor principal (Bottom Nav Bar)
    object MainAppContainer : Routes("main_app_container")

    // Destinos internos de la Bottom Bar
    object Home : Routes("home_screen")
    object Notifications : Routes("notifications_screen")
    object Contacts : Routes("contacts_screen")

    // Rutas Secundarias (Total: 13 Rutas)
    object HealthSummary : Routes("health_summary_screen")
    object Configuration : Routes("configuration_screen")
    object LanguageChange : Routes("language_change_screen") // Se mantiene por rúbrica
    object Rating : Routes("rating_screen")
    object HowTo : Routes("howto_screen")
    object NewContact : Routes("new_contact_screen")
    object PersonalData : Routes("personal_data_screen")
}