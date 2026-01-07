package com.example.skillsharex.ui.session

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.skillsharex.viewmodel.SessionViewModel

// THEME COLORS
private val LavenderBg = Color(0xFFE8E6FF)
private val HeaderPurple = Color(0xFF544DCA)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionOverviewScreen(
    navController: NavController,
    sessionId: String
) {

    val context = LocalContext.current

    // ✅ SINGLE ViewModel instance
    val sessionViewModel: SessionViewModel = viewModel()

    LaunchedEffect(sessionId) {
        sessionViewModel.loadSessionDetail(sessionId.toInt())
    }

    val session = sessionViewModel.selectedSession.value

    // ✅ LOADING STATE
    if (session == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    // ✅ SAFE mentor access
    val mentor = session.mentor
    val mentorPhone = mentor?.phone ?: ""

    val status = session.status ?: "UNKNOWN"

    val statusColor = when (status) {
        "LIVE" -> Color.Red
        "UPCOMING" -> Color(0xFF425CFF)
        else -> Color.Gray
    }

    Scaffold(
        containerColor = LavenderBg,
        topBar = {
            TopAppBar(
                title = { Text("Session Overview", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = HeaderPurple
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            /* -------- SESSION HEADER -------- */

            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = HeaderPurple)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        text = session.title ?: "",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Mentor: ${mentor?.name ?: "Not assigned"}",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "● $status",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = statusColor
                    )
                }
            }

            /* -------- DESCRIPTION -------- */

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        text = "Session Description",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = session.description
                            ?: "This session will be handled by the mentor.",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = "Date: ${session.date ?: "N/A"}")
                    Text(
                        text = "Time: ${(session.start_time ?: "--")} - ${(session.end_time ?: "--")}"
                    )
                }
            }

            /* -------- JOIN SESSION -------- */

            Button(
                onClick = {
                    navController.navigate("live_session/${session.id}")
                },
                enabled = status != "COMPLETED",
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    when (status) {
                        "LIVE" -> "Join Session"
                        "UPCOMING" -> "View Session"
                        else -> "Completed"
                    }
                )
            }

            /* -------- CHAT / CALL -------- */

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                OutlinedButton(
                    onClick = {
                        if (mentorPhone.isNotEmpty()) {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://wa.me/$mentorPhone")
                            )
                            context.startActivity(intent)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Chat, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Chat")
                }

                OutlinedButton(
                    onClick = {
                        if (mentorPhone.isNotEmpty()) {
                            val intent = Intent(
                                Intent.ACTION_DIAL,
                                Uri.parse("tel:$mentorPhone")
                            )
                            context.startActivity(intent)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Call, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Call")
                }
            }
        }
    }
}
