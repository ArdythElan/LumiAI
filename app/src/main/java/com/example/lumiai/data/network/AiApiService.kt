package com.example.lumiai.data.network

import retrofit2.http.Body
import retrofit2.http.POST

interface AiApiService {
    @POST("v1/chat/completions")
    suspend fun getChatCompletion(@Body request: ChatRequest): ChatResponse
}
