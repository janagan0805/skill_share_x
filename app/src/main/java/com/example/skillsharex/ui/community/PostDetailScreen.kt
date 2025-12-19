package com.example.skillsharex.ui.community

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

private val LavenderBg = Color(0xFFE8E6FF)
private val HeaderPurple = Color(0xFF544DCA)
private val PrimaryBlue = Color(0xFF1022FF)

@Composable
fun PostDetailScreen(
    navController: NavController,
    postTitle: String
) {

    val comments = remember {
        listOf(
            "This explanation really helped 👍",
            "Can you share some resources?",
            "Nice question, I had the same doubt!"
        )
    }

    Scaffold(containerColor = LavenderBg) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            /* ---------- HEADER ---------- */
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(HeaderPurple)
                    .clip(RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp))
                    .padding(16.dp, 50.dp, 16.dp, 20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier
                            .size(28.dp)
                            .clickable { navController.popBackStack() }
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = "Post Detail",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            /* ---------- POST CONTENT ---------- */
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Person,
                            null,
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE0E0FF))
                                .padding(8.dp),
                            tint = PrimaryBlue
                        )
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text("Arun", fontWeight = FontWeight.Bold)
                            Text("Android Learner", fontSize = 12.sp)
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text = postTitle,
                        fontSize = 15.sp
                    )

                    Spacer(Modifier.height(14.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                        Icon(Icons.Default.FavoriteBorder, null, tint = PrimaryBlue)
                        Icon(Icons.Default.ChatBubbleOutline, null, tint = PrimaryBlue)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            /* ---------- COMMENTS ---------- */
            Text(
                text = "Comments",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )

            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                items(comments) { comment ->
                    Text(
                        text = "• $comment",
                        modifier = Modifier.padding(vertical = 6.dp),
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}
