package com.example.skillsharex.ui.session

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Stop
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
import com.example.skillsharex.data.model.Session   // ✅ IMPORTANT
import com.example.skillsharex.data.model.Mentor    // ✅ IMPORTANT

// SAME THEME COLORS
private val LavenderBg = Color(0xFFE8E6FF)
private val HeaderPurple = Color(0xFF544DCA)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveSessionScreen(
    navController: NavController,
    sessionId: String
) {

    val context = LocalContext.current
    val sessionViewModel: SessionViewModel = viewModel()

    LaunchedEffect(sessionId) {
        if (sessionViewModel.selectedSession == null) {
            sessionViewModel.loadSessionDetail(sessionId.toInt())
        }
    }

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

    // ✅ SAFE PHONE ACCESS
    val mentorPhone = session.mentor.phone ?: ""

    Scaffold(
        containerColor = LavenderBg,
        topBar = {
            TopAppBar(
                title = { Text("Live Session", color = Color.White) },
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
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            /* -------- SESSION INFO -------- */

            Column {

                Text(
                    text = "● ${session.status}",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = session.title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Mentor: ${session.mentor.name}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            /* -------- LIVE AREA -------- */

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .background(Color.Black, shape = RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Live Session Running...",
                    color = Color.White
                )
            }

            /* -------- ACTION BUTTONS -------- */

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                // 💬 WhatsApp Chat
                OutlinedButton(
                    onClick = {
                        if (mentorPhone.isNotEmpty()) {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://wa.me/$mentorPhone")
                            )
                            context.startActivity(intent)
                        }
                    }
                ) {
                    Icon(Icons.Default.Chat, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Chat")
                }

                // 📞 Phone Call
                OutlinedButton(
                    onClick = {
                        if (mentorPhone.isNotEmpty()) {
                            val intent = Intent(
                                Intent.ACTION_DIAL,
                                Uri.parse("tel:$mentorPhone")
                            )
                            context.startActivity(intent)
                        }
                    }
                ) {
                    Icon(Icons.Default.Call, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Call")
                }

                // ⛔ End Session
                Button(
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Icon(Icons.Default.Stop, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("End")
                }
            }
        }
    }
}
