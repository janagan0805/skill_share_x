package com.example.skillsharex.model

// D:/skillsharex/app/src/main/java/com/example/skillsharex/model/Subscription.kt
data class SubscriptionPlan(
    val id: String,
    val name: String,
    val price: String,
    val features: List<String>,
    val duration: String // e.g., "Monthly", "Yearly"
)

data class SubscriptionResponse(
    val status: String,
    val message: String,
    val is_subscribed: Boolean
)