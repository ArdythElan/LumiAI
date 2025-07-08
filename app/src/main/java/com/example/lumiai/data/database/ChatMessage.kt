package com.example.lumiai.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val content: String,
    val isUser: Boolean,
    val timestamp: Long
)
