package com.example.skillsharex.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
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
import com.example.skillsharex.Screen

@Composable
fun ProfileScreen(
    navController: NavController,
    onOpenSettings: () -> Unit,
    onLogout: () -> Unit
) {
    val tabs = listOf("Profile", "Courses", "Reviews")
    var selectedTab by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFC7C3F9))
    ) {
        Spacer(Modifier.height(20.dp))

        ProfileHeader(navController)
        SkillSection()
        StatsRow()

        Spacer(Modifier.height(20.dp))

        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.Transparent,
            contentColor = Color(0xFF1022FF)
        ) {
            tabs.forEachIndexed { index, text ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text,
                            color = if (selectedTab == index) Color(0xFF1022FF) else Color.Black,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }

        when (selectedTab) {
            0 -> ProfileTabContent(onOpenSettings, onLogout)
            1 -> CoursesTabContent()
            2 -> ReviewsTabContent(navController)
        }
    }
}

/* ---------------- HEADER ---------------- */

@Composable
fun ProfileHeader(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            Icon(
                Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )

            Icon(
                Icons.Default.Edit,
                contentDescription = "Edit Profile",
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1022FF))
                    .padding(5.dp)
                    .clickable {
                        navController.navigate(Screen.EditProfile.route)
                    }
            )
        }

        Spacer(Modifier.height(10.dp))

        Text("Jana", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text("Mentor • UI/UX & Development", fontSize = 14.sp, color = Color.DarkGray)
        Spacer(Modifier.height(6.dp))
        Text(
            "Helping learners to grow in Design and Tech 🚀",
            fontSize = 13.sp,
            color = Color.Black
        )
    }
}

/* ---------------- SKILLS ---------------- */

@Composable
fun SkillSection() {
    val skills = listOf("UI/UX", "Java", "Figma", "Photoshop", "AI Engineer")

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        skills.forEach {
            AssistChip(
                onClick = {},
                label = { Text(it, fontSize = 11.sp) },
                modifier = Modifier.padding(horizontal = 4.dp),
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}

/* ---------------- STATS ---------------- */

@Composable
fun StatsRow() {
    Spacer(Modifier.height(14.dp))
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatItem("⭐ 4.8", "Ratings")
        StatItem("2.5k+", "Students")
        StatItem("12", "Courses")
        StatItem("3.2k", "Followers")
    }
}

@Composable
fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontWeight = FontWeight.Bold)
        Text(label, fontSize = 12.sp, color = Color.DarkGray)
    }
}

/* ---------------- PROFILE TAB ---------------- */

@Composable
fun ProfileTabContent(
    onOpenSettings: () -> Unit,
    onLogout: () -> Unit
) {
    Column(Modifier.padding(16.dp)) {

        ProfileOption("Edit Profile")
        ProfileOption("Share Profile")
        ProfileOption("Help & Support")
        ProfileOption("Settings") { onOpenSettings() }

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = onLogout,
            colors = ButtonDefaults.buttonColors(Color.Red),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout", color = Color.White)
        }
    }
}

@Composable
fun ProfileOption(title: String, onClick: () -> Unit = {}) {
    Text(
        title,
        fontSize = 15.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable { onClick() },
        color = Color.Black
    )
    Divider(color = Color.Gray.copy(alpha = 0.3f))
}

/* ---------------- COURSES TAB ---------------- */

@Composable
fun CoursesTabContent() {
    val courses = listOf("UI UX Mastery", "Java Development", "Photoshop Basics")

    LazyColumn(Modifier.padding(16.dp)) {
        items(courses) {
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                Row(
                    Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.LightGray)
                    )
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text(it, fontWeight = FontWeight.Bold)
                        Text("1k+ Enrolled", fontSize = 12.sp, color = Color.DarkGray)
                    }
                }
            }
        }
    }
}

/* ---------------- REVIEWS TAB ---------------- */

@Composable
fun ReviewsTabContent(navController: NavController) {
    val reviews = listOf(
        "Amazing teaching experience!",
        "Very helpful mentor!",
        "Clear and practical examples!"
    )

    LazyColumn(Modifier.padding(16.dp)) {
        items(reviews) {
            Card(
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Person, null, modifier = Modifier.size(30.dp))
                        Spacer(Modifier.width(10.dp))

                        repeat(4) {
                            Icon(Icons.Default.Star, null, tint = Color(0xFFFFC107), modifier = Modifier.size(14.dp))
                        }
                        Icon(Icons.Default.Star, null, tint = Color.LightGray, modifier = Modifier.size(14.dp))
                    }

                    Spacer(Modifier.height(8.dp))
                    Text(it, fontSize = 13.sp)
                }
            }
        }
    }
}
