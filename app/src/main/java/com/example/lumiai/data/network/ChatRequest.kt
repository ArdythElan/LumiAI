package com.example.lumiai.data.network

data class ChatRequest(
    val model: String = "meta-llama/llama-4-maverick:free", //deepseek/deepseek-r1:free
    val messages: List<Message>
)

data class Message(
    val role: String, // "user" of "assistant"
    val content: String
)
