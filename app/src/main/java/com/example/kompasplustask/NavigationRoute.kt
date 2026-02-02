package com.example.kompasplustask

sealed class Screen(val route: String) {
    object MainScreen : Screen("main_screen")
    object DetailAnswerScreen : Screen("detail_answer_screen")
}