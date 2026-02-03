package com.example.kompasplustask

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

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
                onNavigateToDetail = {
                    navController.navigate(Screen.DetailAnswerScreen.route)
                }
            )
        }

        composable(route = Screen.DetailAnswerScreen.route) {
            DetailAnswerScreen()
        }
    }
}