package com.example.skillsharex.ui.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// Theme colors
private val LavenderBg = Color(0xFFE8E6FF)
private val HeaderPurple = Color(0xFF544DCA)

// Notification Data Model
data class AppNotification(
    val id: String,
    val title: String,
    val message: String,
    val time: String,
    val type: String = "general"
)

// Sample Notifications
val sampleNotifications = listOf(
    AppNotification("1", "Mentorship Request Approved",
        "Your mentorship request from Saranraj has been accepted. Start learning now!",
        "2 min ago", "success"
    ),
    AppNotification("2", "New Message", "Karthick sent you a new message.",
        "10 min ago", "chat"
    ),
    AppNotification("3", "New Course Added",
        "‘Figma Masterclass’ has been added recently.", "1 hour ago", "course"
    ),
    AppNotification("4", "Mentorship Request Declined",
        "Your mentorship request was declined by Gowtham.", "Yesterday", "error"
    )
)

@Composable
fun NotificationsScreen(navController: NavController) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LavenderBg)
    ) {

        Column {

            // -------- HEADER --------
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(HeaderPurple)
                    .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
                    .padding(top = 50.dp, bottom = 20.dp, start = 20.dp, end = 20.dp)
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier
                            .size(28.dp)
                            .clickable { navController.popBackStack() }
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "Notifications",
                        fontSize = 24.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // -------- NOTIFICATION LIST --------
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(sampleNotifications) { notification ->
                    NotificationItem(notification)
                }
            }
        }
    }
}

// -------- SINGLE NOTIFICATION CARD --------
@Composable
fun NotificationItem(notification: AppNotification) {

    val bgColor = when (notification.type) {
        "success" -> Color(0xFFDFF6DD)
        "error" -> Color(0xFFFFE5E5)
        "chat" -> Color(0xFFE5F0FF)
        "course" -> Color(0xFFFFF3CD)
        else -> Color.White
    }

    val textColor = Color(0xFF1A1A1A)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {},
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(bgColor)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = notification.title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = textColor
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = notification.message,
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = notification.time,
                fontSize = 12.sp,
                color = Color(0xFF777777)
            )
        }
    }
}
