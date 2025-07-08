package com.example.lumiai.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lumiai.viewmodel.ChatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(viewModel: ChatViewModel) {
    val messages by viewModel.messages.collectAsState()
    var input by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Lumi AI") }) },
        bottomBar = {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)) {
                TextField(
                    value = input,
                    onValueChange = { input = it },
                    modifier = Modifier.weight(1f)
                )
                Button(onClick = {
                    viewModel.sendMessage(input)
                    input = ""
                }) {
                    Text("Send")
                }
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(messages) { msg ->
                Text(text = if (msg.isUser) "You: ${msg.content}" else "Lumi: ${msg.content}")
            }
        }
    }
}
