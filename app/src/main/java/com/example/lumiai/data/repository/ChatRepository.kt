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
        dao.insertMessage(
            ChatMessage(content = userMessage, isUser = true, timestamp = System.currentTimeMillis())
        )

        return try {
            val response = api.getChatCompletion(
                ChatRequest(messages = listOf(Message(role = "user", content = userMessage)))
            )

            val botReply = response.choices.firstOrNull()?.message?.content ?: "Sorry, geen antwoord."

            dao.insertMessage(
                ChatMessage(content = botReply, isUser = false, timestamp = System.currentTimeMillis())
            )

            botReply
        } catch (e: Exception) {
            // optioneel: sla melding op of log
            dao.insertMessage(
                ChatMessage(content = "Er ging iets mis. Controleer je verbinding.", isUser = false, timestamp = System.currentTimeMillis())
            )
            "Er ging iets mis. Controleer je verbinding."
        }
    }

    // Optioneel: mapping functie als je later meerdere messages wilt mappen
    private fun fromNetwork(message: Message): ChatMessage {
        return ChatMessage(
            content = message.content,
            isUser = message.role == "user",
            timestamp = System.currentTimeMillis()
        )
    }
}
