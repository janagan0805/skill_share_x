package com.example.skillsharex.ui.sessions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

/* ---------------- DATA MODEL ---------------- */

data class Session(
    val id: String,
    val mentor: String,
    val skill: String,
    val time: String,
    val status: SessionStatus
)

enum class SessionStatus {
    LIVE, UPCOMING, COMPLETED
}

/* ---------------- SAMPLE DATA ---------------- */

private val sessions = listOf(
    Session("1", "Karthick", "Android Development", "Now • 45 mins", SessionStatus.LIVE),
    Session("2", "Saranraj", "UI/UX Design", "Today 6:00 PM", SessionStatus.UPCOMING),
    Session("3", "Pranav", "Java Basics", "Tomorrow 10:00 AM", SessionStatus.UPCOMING),
    Session("4", "Gowtham", "Photoshop Tips", "Completed", SessionStatus.COMPLETED)
)

/* ---------------- SESSION SCREEN ---------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionScreen(
    navController: NavController
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Live", "Upcoming", "My Sessions")

    Scaffold(
        containerColor = Color(0xFFE8E6FF),
        topBar = {
            TopAppBar(
                title = { Text("Sessions") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = false }
                            }
                        }
                    ) {
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
                .fillMaxSize()
        ) {

            /* -------- TABS -------- */

            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                title,
                                fontWeight = if (selectedTab == index)
                                    FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            /* -------- FILTER SESSIONS -------- */

            val filteredSessions = when (selectedTab) {
                0 -> sessions.filter { it.status == SessionStatus.LIVE }
                1 -> sessions.filter { it.status == SessionStatus.UPCOMING }
                else -> sessions
            }

            /* -------- SESSION LIST -------- */

            LazyColumn(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredSessions) { session ->

                    // ✅ CLICKING THE CARD GOES TO SESSION DETAIL
                    SessionCard(
                        session = session,
                        onClick = {
                            navController.navigate("sessionDetail/${session.id}")

                        }
                    )
                }
            }
        }
    }
}

/* ---------------- SESSION CARD ---------------- */

@Composable
fun SessionCard(
    session: Session,
    onClick: () -> Unit
) {

    val statusColor = when (session.status) {
        SessionStatus.LIVE -> Color(0xFF2ECC71)
        SessionStatus.UPCOMING -> Color(0xFF425CFF)
        SessionStatus.COMPLETED -> Color.Gray
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    session.skill,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    session.status.name,
                    color = statusColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text("Mentor: ${session.mentor}", fontSize = 13.sp)
            Text(session.time, fontSize = 12.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { /* later: chat / call */ },
                enabled = session.status != SessionStatus.COMPLETED,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = statusColor)
            ) {
                Icon(Icons.Default.VideoCall, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text(
                    when (session.status) {
                        SessionStatus.LIVE -> "Join Now"
                        SessionStatus.UPCOMING -> "Set Reminder"
                        SessionStatus.COMPLETED -> "Completed"
                    }
                )
            }
        }
    }
}
