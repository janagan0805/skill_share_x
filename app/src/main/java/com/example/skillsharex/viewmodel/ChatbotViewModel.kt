package com.example.skillsharex.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillsharex.model.chatbot.ChatMessage
import com.example.skillsharex.repository.ChatbotRepository
import kotlinx.coroutines.launch

class ChatbotViewModel : ViewModel() {

    private val repository = ChatbotRepository()

    var messages by mutableStateOf(listOf<ChatMessage>())
        private set

    var inputText by mutableStateOf("")
        private set

    var isTyping by mutableStateOf(false)
        private set

    fun onInputChange(text: String) {
        inputText = text
    }

    fun sendMessage() {
        if (inputText.isBlank()) return

        val question = inputText
        messages = messages + ChatMessage(question, true)
        inputText = ""

        viewModelScope.launch {
            isTyping = true

            val aiReply =
                repository.sendMessageWithReasoning(question)

            isTyping = false
            messages = messages + ChatMessage(aiReply, false)
        }
    }


    private fun getAIResponse(userText: String) {
        viewModelScope.launch {
            isTyping = true

            val aiReply = repository.sendMessageWithReasoning(userText)

            isTyping = false
            messages = messages + ChatMessage(aiReply, false)
        }
    }
}
