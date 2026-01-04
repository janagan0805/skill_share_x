import android.annotation.SuppressLint
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@SuppressLint("UnsafeOptInUsageError")
@kotlinx.serialization.Serializable
data class ChatRequest(
    val model: String,
    val messages: List<ORMessage>,
    val reasoning: ReasoningConfig? = null
)

@SuppressLint("UnsafeOptInUsageError")
@kotlinx.serialization.Serializable
data class ReasoningConfig(
    val enabled: Boolean
)

@SuppressLint("UnsafeOptInUsageError")
@kotlinx.serialization.Serializable
data class ORMessage(
    val role: String,
    val content: String,
    val reasoning_details: JsonElement? = null
)

@SuppressLint("UnsafeOptInUsageError")
@kotlinx.serialization.Serializable
data class ChatResponse(
    val choices: List<ChatChoice>? = null,
    val error: ORError? = null
)

@SuppressLint("UnsafeOptInUsageError")
@kotlinx.serialization.Serializable
data class ChatChoice(
    val message: ORMessage
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class ORError(
    val message: String? = null,
    val code: Int? = null,
    val metadata: JsonElement? = null
)
