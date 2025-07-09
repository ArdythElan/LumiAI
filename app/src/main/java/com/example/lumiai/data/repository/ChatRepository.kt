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
        // 1) Sla het bericht van de gebruiker lokaal op
        dao.insertMessage(
            ChatMessage(
                content = userMessage,
                isUser = true,
                timestamp = System.currentTimeMillis()
            )
        )

        // 2) Bouw de lijst met messages voor context (optioneel)
        // Hier sturen we nu alleen het laatste bericht mee, later kun je alle sturen als context
        val requestMessages = listOf(
            Message(role = "user", content = userMessage)
        )

        // 3) Verstuur naar OpenRouter (Retrofit)
        val response = api.getChatCompletion(
            ChatRequest(messages = requestMessages)
        )

        // 4) Haal het antwoord op (eerste choice)
        val botReply = response.choices.firstOrNull()?.message?.content ?: "Sorry, geen antwoord."

        // 5) Converteer & sla op als ChatMessage (isUser = false)
        val chatMessage = ChatMessage(
            content = botReply,
            isUser = false,
            timestamp = System.currentTimeMillis()
        )
        dao.insertMessage(chatMessage)

        return botReply
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
