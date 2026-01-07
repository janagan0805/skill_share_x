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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.skillsharex.viewmodel.SessionViewModel
import com.example.skillsharex.data.model.Session   // ✅ CORRECT IMPORT

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionScreen(
    navController: NavController
) {

    val sessionViewModel: SessionViewModel = viewModel()

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Live", "Upcoming", "My Sessions")

    // 🔥 Load sessions from backend
    LaunchedEffect(Unit) {
        sessionViewModel.loadSessions()
    }

    // ✅ Correct state read
    val allSessions = sessionViewModel.sessions.value

    val filteredSessions = when (selectedTab) {
        0 -> allSessions.filter { it.status == "LIVE" }
        1 -> allSessions.filter { it.status == "UPCOMING" }
        else -> allSessions
    }

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

            /* -------- SESSION LIST -------- */

            LazyColumn(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredSessions) { session ->

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
        "LIVE" -> Color(0xFF2ECC71)
        "UPCOMING" -> Color(0xFF425CFF)
        else -> Color.Gray
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
                    text = session.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = session.status,
                    color = statusColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Mentor: ${session.mentor.name}",
                fontSize = 13.sp
            )

            Text(
                text = "${session.date} • ${session.start_time}",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onClick,
                enabled = session.status != "COMPLETED",
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = statusColor)
            ) {
                Icon(Icons.Default.VideoCall, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text(
                    when (session.status) {
                        "LIVE" -> "Join Now"
                        "UPCOMING" -> "View Details"
                        else -> "Completed"
                    }
                )
            }
        }
    }
}
