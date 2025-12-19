package com.example.skillsharex.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.skillsharex.R
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.res.painterResource

// ---------- THEME ----------
private val Lavender = Color(0xFFE8E6FF)
private val HeaderPurple = Color(0xFF544DCA)
private val BubbleSent = Color(0xFF6C47FF)
private val BubbleReceived = Color.White
private val TextDark = Color(0xFF1A1A1A)
private val TextLight = Color.White

private val Gradient = Brush.horizontalGradient(
    listOf(Color(0xFF6C47FF), Color(0xFF4BC9FF))
)

// ---------- MESSAGE MODEL ----------
data class ChatMessage(
    val id: String,
    val text: String,
    val isUser: Boolean,
    val time: String
)

@Composable
fun ChatScreen(
    navController: NavController,
    userId: String
) {
    var messages by remember { mutableStateOf(sampleMessages()) }
    var currentText by remember { mutableStateOf("") }

    Scaffold(
        containerColor = Lavender,
        topBar = {
            // ---------- PURPLE HEADER ----------
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(HeaderPurple)
                    .padding(top = 46.dp, bottom = 20.dp, start = 16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {

                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(Modifier.width(10.dp))

                    Text(
                        text = userId,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    ) { inner ->

        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
        ) {

            // ------------------ CHAT MESSAGES ------------------
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp)
            ) {
                items(messages) { msg ->
                    ChatBubble(msg)
                    Spacer(Modifier.height(10.dp))
                }
            }

            // ------------------ INPUT BAR ------------------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .background(Color.White, RoundedCornerShape(30.dp))
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // USER TEXT INPUT
                BasicTextField(
                    value = currentText,
                    onValueChange = { currentText = it },
                    textStyle = TextStyle(color = TextDark, fontSize = 16.sp),
                    modifier = Modifier.weight(1f),
                    decorationBox = { inner ->
                        if (currentText.isEmpty()) {
                            Text("Type a message...", color = Color.Gray)
                        }
                        inner()
                    }
                )

                // SEND BUTTON (Gradient)
                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Gradient)
                        .clickable {
                            if (currentText.isNotBlank()) {
                                messages = messages + ChatMessage(
                                    id = System.currentTimeMillis().toString(),
                                    text = currentText,
                                    isUser = true,
                                    time = currentTime()
                                )
                                currentText = ""
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}


// ------------------ CHAT BUBBLE ------------------
@Composable
fun ChatBubble(message: ChatMessage) {
    val alignment = if (message.isUser) Alignment.End else Alignment.Start
    val bubbleColor = if (message.isUser) BubbleSent else BubbleReceived
    val textColor = if (message.isUser) TextLight else TextDark

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = alignment
    ) {
        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = if (message.isUser) 16.dp else 0.dp,
                        topEnd = if (!message.isUser) 16.dp else 0.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 16.dp
                    )
                )
                .background(bubbleColor)
                .padding(horizontal = 14.dp, vertical = 10.dp)
                .widthIn(max = 260.dp)
        ) {
            Text(message.text, color = textColor, fontSize = 15.sp)
        }

        Spacer(Modifier.height(3.dp))

        Text(
            text = message.time,
            fontSize = 11.sp,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 6.dp)
        )
    }
}


// ------------------ SAMPLE MESSAGES ------------------
fun sampleMessages(): List<ChatMessage> {
    return listOf(
        ChatMessage("1", "Hello! How can I guide you today?", false, "10:20 AM"),
        ChatMessage("2", "Hi Mentor! I need help in UI/UX.", true, "10:21 AM"),
        ChatMessage("3", "Sure! Let's start from basics 😊", false, "10:22 AM")
    )
}


// ------------------ TIME FORMATTER ------------------
private fun currentTime(): String {
    return try {
        val sdf = java.text.SimpleDateFormat("hh:mm a", java.util.Locale.getDefault())
        sdf.format(java.util.Date())
    } catch (e: Exception) {
        ""
    }
}
