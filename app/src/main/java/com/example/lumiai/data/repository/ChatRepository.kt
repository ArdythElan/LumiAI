package com.example.lumiai.data.repository

import com.example.lumiai.data.database.ChatDao
import com.example.lumiai.data.database.ChatMessage
import com.example.lumiai.data.network.AiApiService
import com.example.lumiai.data.network.ChatRequest
import com.example.lumiai.data.network.Message

class ChatRepository(
    private val dao: ChatDao,
    private val api: AiApiService
) {

    fun getAllMessages() = dao.getAllMessages()

    suspend fun sendMessage(userMessage: String): String {
        // Sla het bericht van de gebruiker lokaal op
        dao.insertMessage(
            ChatMessage(
                content = userMessage,
                isUser = true,
                timestamp = System.currentTimeMillis()
            )
        )

        // Verstuur naar OpenRouter
        val response = api.getChatCompletion(
            ChatRequest(
                messages = listOf(
                    Message(role = "user", content = userMessage)
                )
            )
        )

        // Haal het antwoord op
        val botReply = response.choices.firstOrNull()?.message?.content ?: "Sorry, geen antwoord."

        // Sla het antwoord lokaal op
        dao.insertMessage(
            ChatMessage(
                content = botReply,
                isUser = false,
                timestamp = System.currentTimeMillis()
            )
        )

        return botReply
    }
}
