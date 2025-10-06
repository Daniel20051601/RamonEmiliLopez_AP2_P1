package edu.ucne.ramonemililopez_ap2_p1.tareas.local.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import edu.ucne.ramonemililopez_ap2_p1.tareas.local.presentation.edit.EditEntradaScreen
import edu.ucne.ramonemililopez_ap2_p1.tareas.local.presentation.list.ListEntradaScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.ListEntrada.route,
        modifier = modifier
    ) {
        composable(Screen.ListEntrada.route) {
            ListEntradaScreen(navController = navController)
        }
        composable(
            route = Screen.EditEntrada.route,
            arguments = listOf(
                navArgument(Screen.EditEntrada.ARG) { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val entradaId = backStackEntry.arguments?.getInt(Screen.EditEntrada.ARG)
            EditEntradaScreen(
                navController = navController,
                entradaId = entradaId
            )
        }
    }
}
