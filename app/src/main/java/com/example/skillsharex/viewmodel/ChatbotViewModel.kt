package com.example.skillsharex.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.skillsharex.model.chatbot.ChatMessage

class ChatbotViewModel : ViewModel() {

    var messages by mutableStateOf(listOf<ChatMessage>())
        private set

    var inputText by mutableStateOf("")
        private set

    fun onInputChange(text: String) {
        inputText = text
    }

    fun sendMessage() {
        if (inputText.isBlank()) return

        val userMessage = ChatMessage(inputText, true)
        messages = messages + userMessage
        inputText = ""

        // TODO: Replace this with real API response
        simulateAIResponse()
    }

    private fun simulateAIResponse() {
        messages = messages + ChatMessage(
            "AI response will appear here once API is connected.",
            false
        )
    }
}
