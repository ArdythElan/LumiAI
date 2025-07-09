package com.example.lumiai.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import  com.example.lumiai.utils.NetworkObserver

@Composable
fun StartScreen(navController: NavController) {
    val context = LocalContext.current
    val isConnected by NetworkObserver.observe(context).collectAsState(initial = false)

    LaunchedEffect(isConnected) {
        if (isConnected) {
            navController.navigate("chat") {
                popUpTo("start") { inclusive = true }
            }
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Welkom bij Lumi AI", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            if (!isConnected) {
                Text(
                    "Geen internetverbinding gevonden.\nControleer je verbinding en probeer opnieuw.",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(8.dp)
                )
            } else {
                CircularProgressIndicator()
            }
        }
    }
}
