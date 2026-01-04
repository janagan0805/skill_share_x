package com.example.skillsharex.repository

import ChatRequest
import ChatResponse
import ORMessage
import ReasoningConfig
import com.example.skillsharex.model.chatbot.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class ChatbotRepository {

    private val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    explicitNulls = false
                }
            )
        }
    }

    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    private val API_URL = "https://openrouter.ai/api/v1/chat/completions"
    private val MODEL = "nvidia/nemotron-3-nano-30b-a3b:free"
    private val API_KEY = "sk-or-v1-dummyapi"

    suspend fun sendMessageWithReasoning(userQuestion: String): String {

        val raw: String = client.post(API_URL) {
            contentType(ContentType.Application.Json)
            header("Authorization", "Bearer $API_KEY")
            header("HTTP-Referer", "https://skillsharex.app")
            header("X-Title", "SkillShareX")

            setBody(
                ChatRequest(
                    model = MODEL,
                    messages = listOf(
                        ORMessage(
                            role = "system",
                            content = "You are a concise assistant. Answer directly. Never reveal chain-of-thought."
                        ),
                        ORMessage(
                            role = "user",
                            content = userQuestion
                        )
                    ),
                    reasoning = ReasoningConfig(enabled = true)
                )
            )
        }.body()

        val response = json.decodeFromString<ChatResponse>(raw)

        return response.choices
            ?.firstOrNull()
            ?.message
            ?.content
            ?: response.error?.message
            ?: "AI did not return a response."
    }

}
