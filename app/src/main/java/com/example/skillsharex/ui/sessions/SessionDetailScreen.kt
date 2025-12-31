package com.example.skillsharex.ui.sessions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionDetailScreen(
    navController: NavController,
    sessionId: String
) {

    // 🔹 TEMP DATA (replace with API later)
    val sessionTitle: String
    val mentorName: String
    val status: String
    val time: String

    when (sessionId) {
        "1" -> {
            sessionTitle = "Android Development"
            mentorName = "Karthick"
            status = "LIVE"
            time = "Now • 45 mins"
        }
        "2" -> {
            sessionTitle = "UI/UX Design"
            mentorName = "Saranraj"
            status = "UPCOMING"
            time = "Today 6:00 PM"
        }
        "3" -> {
            sessionTitle = "Java Basics"
            mentorName = "Pranav"
            status = "UPCOMING"
            time = "Tomorrow 10:00 AM"
        }
        else -> {
            sessionTitle = "Photoshop Tips"
            mentorName = "Gowtham"
            status = "COMPLETED"
            time = "Completed"
        }
    }

    val statusColor = when (status) {
        "LIVE" -> Color(0xFF2ECC71)
        "UPCOMING" -> Color(0xFF425CFF)
        else -> Color.Gray
    }

    Scaffold(
        containerColor = Color(0xFFE8E6FF),
        topBar = {
            TopAppBar(
                title = { Text("Session Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF544DCA),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            /* -------- SESSION INFO -------- */

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        text = sessionTitle,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        text = "Mentor: $mentorName",
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        text = time,
                        fontSize = 13.sp,
                        color = Color.Gray
                    )

                    Spacer(Modifier.height(10.dp))

                    Text(
                        text = status,
                        color = statusColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            /* -------- DESCRIPTION -------- */

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("About this session", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "In this session, the mentor will explain core concepts, share practical tips, and answer your questions live."
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            /* -------- ACTION BUTTON -------- */

            Button(
                onClick = { navController.navigate("chat/${sessionId}") },
                enabled = status != "COMPLETED",
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = statusColor)
            ) {
                Icon(Icons.Default.VideoCall, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(
                    when (status) {
                        "LIVE" -> "Join Now"
                        "UPCOMING" -> "Set Reminder"
                        else -> "Completed"
                    }
                )
            }
        }
    }
}
