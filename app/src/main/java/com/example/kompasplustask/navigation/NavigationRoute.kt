package com.example.kompasplustask.navigation

sealed class Screen(val route: String) {
    object MainScreen : Screen("main_screen")
    object DetailAnswerScreen : Screen("detail_answer_screen")
}