package com.example.skillsharex.ui.sessions

import androidx.compose.foundation.layout.*
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionDetailScreen(
    navController: NavController,
    sessionId: String
) {

    val sessionViewModel: SessionViewModel = viewModel()

    // 🔥 LOAD SESSION DETAIL FROM BACKEND
    LaunchedEffect(sessionId) {
        sessionViewModel.loadSessionDetail(sessionId.toInt())
    }

    // ✅ CORRECT STATE ACCESS
    val session = sessionViewModel.selectedSession.value

    // Loading state
    if (session == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val statusColor = when (session.status) {
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
                        text = session.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        text = "Mentor: ${session.mentor.name}",
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        text = "${session.date} • ${session.start_time} - ${session.end_time}",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )

                    Spacer(Modifier.height(10.dp))

                    Text(
                        text = session.status,
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
                        text = session.description
                            ?: "Session details will be explained by the mentor."
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            /* -------- ACTION BUTTON -------- */

            Button(
                onClick = {
                    navController.navigate("live_session/${session.id}")
                },
                enabled = session.status != "COMPLETED",
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = statusColor)
            ) {
                Icon(Icons.Default.VideoCall, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(
                    when (session.status) {
                        "LIVE" -> "Join Now"
                        "UPCOMING" -> "View Session"
                        else -> "Completed"
                    }
                )
            }
        }
    }
}
