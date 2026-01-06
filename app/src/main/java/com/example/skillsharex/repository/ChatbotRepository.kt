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
import com.example.skillsharex.BuildConfig

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
//    private val API_KEY = BuildConfig.OPENROUTER_API_KEY
    private val API_KEY = BuildConfig.OPENROUTER_API_KEY

    suspend fun sendMessageWithReasoning(userQuestion: String): String {

        val raw: String = client.post(API_URL) {
            println("OPENROUTER KEY = ${BuildConfig.OPENROUTER_API_KEY}")

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
                            content = """
                                You are SkillShareX Help Assistant.
                                
                                Your job:
                                - Answer ONLY questions related to the SkillShareX mobile application.
                                - Help users with:
                                  - Accessing courses
                                  - Finding mentors
                                  - Booking or contacting mentors
                                  - Navigating app features
                                  - Settings and account options
                                  - Common app usage issues
                                
                                Rules:
                                - If the question is NOT about SkillShareX, reply:
                                  "I can help only with SkillShareX app-related questions."
                                - Do NOT answer general knowledge questions.
                                - Do NOT give opinions.
                                - Do NOT mention AI, models, reasoning, or policies.
                                - Keep answers short, clear, and practical.
                                - Never invent features that do not exist.
                                - Limit answers to 3–5 sentences maximum.

                                """
                        )
                        ,
                        ORMessage(
                            role = "user",
                            content = userQuestion
                        )
                    ),
                    reasoning = null
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
