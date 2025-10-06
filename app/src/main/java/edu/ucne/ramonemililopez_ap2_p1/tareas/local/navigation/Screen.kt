package edu.ucne.ramonemililopez_ap2_p1.tareas.local.navigation

sealed class Screen(val route: String) {
    data object ListEntrada : Screen("list_entrada_screen")
    data object EditEntrada : Screen("edit_entrada_screen/{entradaId}") {
        const val ARG = "entradaId"
        fun createRoute(entradaId: Int) = "edit_entrada_screen/$entradaId"
    }

}