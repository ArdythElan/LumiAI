package com.example.lumiai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lumiai.data.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ChatViewModel(private val repository: ChatRepository) : ViewModel() {
    val messages = repository.getAllMessages().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    private val _navigateToStart = MutableStateFlow(false)
    val navigateToStart = _navigateToStart.asStateFlow()


    fun sendMessage(text: String) {
        viewModelScope.launch {
            repository.sendMessage(text)
        }
    }
}
