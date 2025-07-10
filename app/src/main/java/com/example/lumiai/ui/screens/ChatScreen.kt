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
import coil.compose.rememberAsyncImagePainter
import com.example.lumiai.data.database.ChatMessage
import com.example.lumiai.utils.NetworkObserver
import com.example.lumiai.viewmodel.ChatViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Info



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
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Rechtsboven: settings & about icons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = { navController.navigate("settings") }) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                    IconButton(onClick = { navController.navigate("about") }) {
                        Icon(Icons.Filled.Info, contentDescription = "About")
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Profielfoto (gecached via Coil)
                Image(
                    painter = rememberAsyncImagePainter("https://raw.githubusercontent.com/ArdythElan/LumiAI/refs/heads/main/assets/LumiAI%20Profile%20picture.png"),
                    contentDescription = "Profile picture",
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Naam
                Text(
                    text = "Lumi AI",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        },
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
