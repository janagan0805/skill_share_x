package com.example.skillsharex.ui.chatbot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.skillsharex.viewmodel.ChatbotViewModel
import com.example.skillsharex.model.chatbot.ChatMessage
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.safeDrawing

private val AppPrimary = Color(0xFF2563EB)   // Primary logo blue
private val ChatBg = Color(0xFFF8FAFF)       // Very light blue background
private val UserBubble = Color(0xFF2563EB)   // User bubble = primary
private val BotBubble = Color.White
private val TextDark = Color(0xFF111827)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatbotScreen(
    navController: NavController,
    viewModel: ChatbotViewModel = viewModel()
) {
    val messages = viewModel.messages
    val inputText = viewModel.inputText

    Scaffold(
        containerColor = ChatBg,
        topBar = {
            TopAppBar(
                title = { Text("SkillShareX AI", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppPrimary
                )
            )
        }
    ) { padding ->


    Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            /* ---------- MESSAGES ---------- */
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            reverseLayout = true
        ) {

            // 1️⃣ Show typing indicator FIRST (because reverseLayout = true)
            if (viewModel.isTyping) {
                item {
                    AppChatBubble(
                        ChatMessage(
                            text = "Typing…",
                            isUser = false
                        )
                    )
                }
            }

            // 2️⃣ Show actual messages
            items(messages.reversed()) { message ->
                AppChatBubble(message)
            }
        }




        /* ---------- INPUT BAR ---------- */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ChatBg)
                .imePadding()   // ✅ keyboard pushes ONLY this
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        )
        {

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color.White, RoundedCornerShape(24.dp))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    TextField(
                        value = inputText,
                        onValueChange = viewModel::onInputChange,
                        placeholder = { Text("Ask SkillShareX AI…") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = viewModel::sendMessage,
                    modifier = Modifier
                        .size(48.dp)
                        .background(AppPrimary, RoundedCornerShape(50))
                ) {
                    Icon(
                        Icons.Filled.Send,
                        contentDescription = "Send",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun AppChatBubble(message: ChatMessage) {

    val isUser = message.isUser

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser)
            Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 300.dp)
                .background(
                    color = if (isUser) UserBubble else BotBubble,
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomEnd = if (isUser) 4.dp else 16.dp,
                        bottomStart = if (isUser) 16.dp else 4.dp
                    )
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = message.text,
                color = if (isUser) Color.White else TextDark
            )
        }
    }
}
