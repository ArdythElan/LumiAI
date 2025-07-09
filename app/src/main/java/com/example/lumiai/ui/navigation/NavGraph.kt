package com.example.lumiai.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import com.example.lumiai.viewmodel.ChatViewModel
import com.example.lumiai.ui.screens.*

@Composable
fun LumiNavGraph(navController: NavHostController, viewModel: ChatViewModel) {
    NavHost(navController, startDestination = "start") {
        composable("start") { StartScreen(navController) }
        composable("chat") { ChatScreen(navController, viewModel) }
        composable("settings") { SettingsScreen() }
        composable("about") { AboutScreen() }
    }
}
