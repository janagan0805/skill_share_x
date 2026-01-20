package com.example.skillsharex.ui.sessions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
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

// Theme colors
private val LavenderBg = Color(0xFFE8E6FF)
private val HeaderPurple = Color(0xFF544DCA)

/* -------------------- Helper functions -------------------- */

fun openWhatsApp(context: Context, phoneNumber: String?) {
    if (phoneNumber.isNullOrBlank()) {
        Toast.makeText(context, "Phone number not available", Toast.LENGTH_SHORT).show()
        return
    }

    val cleanNumber = phoneNumber.replace("+", "").replace(" ", "")
    val intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("https://wa.me/$cleanNumber")
    )

    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "WhatsApp not installed", Toast.LENGTH_SHORT).show()
    }
}

fun openDialer(context: Context, phoneNumber: String?) {
    if (phoneNumber.isNullOrBlank()) {
        Toast.makeText(context, "Phone number not available", Toast.LENGTH_SHORT).show()
        return
    }

    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:$phoneNumber")
    }
    context.startActivity(intent)
}

/* -------------------- Screen -------------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionOverviewScreen(
    navController: NavController,
    sessionId: Int,
    sessionViewModel: SessionViewModel = viewModel()
) {
    val context = LocalContext.current

    val session by sessionViewModel.selectedSession.collectAsState()
    val isLoading by sessionViewModel.isLoading.collectAsState()

    // Load session detail once
    LaunchedEffect(sessionId) {
        sessionViewModel.loadSessionDetail(sessionId)
    }

    if (isLoading || session == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val mentor = session!!.mentor

    // Normalize backend status (your backend sends empty string)
    val status = session!!.status?.ifBlank { "scheduled" } ?: "scheduled"

    val statusColor = when (status.lowercase()) {
        "scheduled" -> Color(0xFF425CFF)
        "completed" -> Color(0xFF2E7D32)
        "cancelled" -> Color.Gray
        else -> Color.Red
    }

    Scaffold(
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

            /* ---------- SESSION HEADER ---------- */

            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = HeaderPurple)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        text = session!!.title,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Mentor: ${mentor?.name ?: "Not available"}",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "● ${status.uppercase()}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = statusColor
                    )
                }
            }

            /* ---------- DETAILS ---------- */

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        text = "Session Details",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = session!!.description ?: "Session conducted by mentor.",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = "Date: ${session!!.date}")
                    Text(text = "Time: ${session!!.start_time} - ${session!!.end_time}")
                }
            }

            /* ---------- JOIN BUTTON ---------- */

            Button(
                onClick = {
                    navController.navigate("live_session/${session!!.id}")
                },
                enabled = status != "completed",
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    if (status == "scheduled") "Join Session" else "Completed"
                )
            }

            /* ---------- CHAT / CALL ---------- */

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                OutlinedButton(
                    onClick = { openWhatsApp(context, mentor?.phone) },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Chat, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Chat")
                }

                OutlinedButton(
                    onClick = { openDialer(context, mentor?.phone) },
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
