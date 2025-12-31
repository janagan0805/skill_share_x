package com.example.skillsharex.ui.session

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


// THEME COLORS (same style)
private val LavenderBg = Color(0xFFE8E6FF)
private val HeaderPurple = Color(0xFF544DCA)
private val CardWhite = Color(0xFFFFFFFF)

data class SessionItem(
    val id: String,
    val title: String,
    val mentor: String,
    val status: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionListScreen(
    navController: NavController
) {

    val sessions = listOf(
        SessionItem("1", "Android Development", "Jana", "LIVE"),
        SessionItem("2", "UI/UX Basics", "Karthick", "UPCOMING"),
        SessionItem("3", "Photoshop Mastery", "Dhanush", "UPCOMING")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sessions", color = Color.White) },
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
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(LavenderBg)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(sessions) { session ->
                SessionCard(session) {
                    navController.navigate(
                        "session_overview/${session.id}"
                    )
                }
            }
        }
    }
}

@Composable
fun SessionCard(
    session: SessionItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Default.PlayCircle,
                contentDescription = null,
                tint = HeaderPurple,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = session.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Mentor: ${session.mentor}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Text(
                text = session.status,
                fontSize = 12.sp,
                color = if (session.status == "LIVE") Color.Red else HeaderPurple,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
