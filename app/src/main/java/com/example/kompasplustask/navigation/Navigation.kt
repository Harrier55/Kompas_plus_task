package com.example.kompasplustask.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.kompasplustask.presentation.detail_screen.DetailAnswerScreen
import com.example.kompasplustask.presentation.main_screen.MainScreen

@Composable
fun NavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    androidx.navigation.compose.NavHost(
        navController = navController,
        startDestination = Screen.MainScreen.route,
        modifier = modifier
    ) {
        composable(route = Screen.MainScreen.route) {
            MainScreen(
                modifier = Modifier,
                onNavigateToDetail = { faqCode ->
                //    navController.navigate(Screen.DetailAnswerScreen.route)
                    navController.navigate(Screen.DetailAnswerScreen.createRoute(faqCode))
                }
            )
        }

        composable(
            route = Screen.DetailAnswerScreen.route,
            arguments = listOf(
                navArgument("faqCode") {
                    type = NavType.StringType
                    nullable = false // Аргумент обязательный
                }
            )
        ) { navBackStackEntry ->
            // Извлекаем аргумент из навигации
            val faqCode = navBackStackEntry.arguments?.getString("faqCode") ?: ""

            // Передаем faqCode в DetailAnswerScreen
            DetailAnswerScreen(
                faqCode = faqCode,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}