package com.example.lumiai.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lumiai.data.database.ChatMessage
import com.example.lumiai.utils.NetworkObserver
import com.example.lumiai.viewmodel.ChatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavController,
    viewModel: ChatViewModel
) {
    val messages by viewModel.messages.collectAsState()
    var input by remember { mutableStateOf("") }

    val context = LocalContext.current
    val isConnected by NetworkObserver.observe(context).collectAsState(initial = true)

    // Live observer: als verbinding wegvalt, ga automatisch naar StartScreen
    LaunchedEffect(isConnected) {
        if (!isConnected) {
            navController.navigate("start") {
                popUpTo("chat") { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Lumi AI") }) },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = input,
                    onValueChange = { input = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Type your message...") }
                )
                Button(
                    onClick = {
                        if (isConnected && input.isNotBlank()) {
                            viewModel.sendMessage(input)
                            input = ""
                        } else {
                            navController.navigate("start") {
                                popUpTo("chat") { inclusive = true }
                            }
                        }
                    },
                    enabled = input.isNotBlank()
                ) {
                    Text("Send")
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(messages) { msg ->
                ChatBubble(message = msg)
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    val bubbleColor = if (message.isUser) Color(0xFFBEB5B4) else Color(0xFFFBD977)
    val alignment = if (message.isUser) Arrangement.End else Arrangement.Start

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = alignment
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = bubbleColor,
            modifier = Modifier
                .fillMaxWidth(0.75f)  // max 75% breedte
                .padding(2.dp)
        ) {
            Text(
                text = message.content,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
