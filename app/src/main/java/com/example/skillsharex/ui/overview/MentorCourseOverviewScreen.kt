package com.example.skillsharex.ui.overview

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skillsharex.R
import com.example.skillsharex.data.allCourses

private val Lavender = Color(0xFFE8E6FF)
private val HeaderPurple = Color(0xFF544DCA)

data class CourseModule(
    val title: String,
    val lessons: List<String>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MentorCourseOverviewScreen(
    courseId: String,
    onBack: () -> Unit
) {
    val course = allCourses.firstOrNull { it.id == courseId }

    if (course == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Course not found", color = Color.Red)
        }
        return
    }

    val scrollState = rememberScrollState()

    val modules = listOf(
        CourseModule("Introduction & Basics", listOf("What is ${course.title}?", "Tools Required", "Getting Started")),
        CourseModule("Core Concepts", listOf("Deep Dive Lessons", "Hands-on Practice", "Expert Guidance")),
        CourseModule("Real World Projects", listOf("Mini Project", "Final Project", "Portfolio Building"))
    )

    Scaffold(
        containerColor = Lavender,
        topBar = { CourseHeader(course.title, onBack) }
    ) { innerPadding ->

        // --------- FIXED STRUCTURE: BOX ---------
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            // ---------- SCROLL CONTENT ----------
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .padding(bottom = 90.dp) // space for button
            ) {

                // ---------- BANNER ----------
                Card(
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(Color.White)
                ) {
                    Column {
                        Image(
                            painter = painterResource(id = course.banner),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp),
                            contentScale = ContentScale.Crop
                        )

                        Column(Modifier.padding(16.dp)) {

                            Text(
                                text = course.title,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(Modifier.height(6.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                repeat(4) {
                                    Icon(Icons.Default.Star, null, tint = Color(0xFFFFC107))
                                }
                                Icon(Icons.Default.StarBorder, null, tint = Color(0xFFFFC107))

                                Spacer(Modifier.width(8.dp))

                                Text(
                                    "${course.rating} • ${course.learners} learners",
                                    fontSize = 13.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }

                // ---------- MENTOR CARD ----------
                MentorInfoCard(course.mentorName, course.mentorRole, course.learners)

                Spacer(Modifier.height(20.dp))

                // ---------- ABOUT ----------
                Text(
                    "About This Course",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Text(
                    course.about,
                    color = Color.DarkGray,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                )

                // ---------- TAGS ----------
                LazyRow(Modifier.padding(horizontal = 16.dp)) {
                    items(course.tags) { tag ->
                        AssistChip(
                            onClick = {},
                            label = { Text(tag) },
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                // ---------- MODULES ----------
                Text(
                    "Course Content",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(Modifier.height(10.dp))

                modules.forEach { ModuleItem(it) }
            }

            // ---------- FIXED BOTTOM BUTTON (NO ERROR NOW) ----------
            Button(
                onClick = {},
                modifier = Modifier
                    .align(Alignment.BottomCenter) // now valid because inside Box
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF1022FF))
            ) {
                Text("Start Learning", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ---------- HEADER ----------
@Composable
fun CourseHeader(title: String, onBack: () -> Unit) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(HeaderPurple)
            .clip(RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp))
            .padding(top = 48.dp, bottom = 20.dp, start = 16.dp, end = 16.dp)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {

            Icon(
                Icons.Default.ArrowBack,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onBack() }
            )

            Spacer(Modifier.width(10.dp))

            Text(
                text = title,
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ---------- MENTOR CARD ----------
@Composable
fun MentorInfoCard(name: String, role: String, learners: String) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Row(
            Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(Color.LightGray, RoundedCornerShape(50))
            )

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(role, fontSize = 13.sp, color = Color.Gray)
                Text("$learners learners", fontSize = 13.sp, color = Color.Gray)
            }

            Button(
                onClick = {},
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF1022FF))
            ) {
                Text("Follow", color = Color.White)
            }
        }
    }
}

// ---------- MODULE ITEM ----------
@Composable
fun ModuleItem(module: CourseModule) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Column(
            Modifier
                .clickable { expanded = !expanded }
                .padding(16.dp)
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    module.title,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    if (expanded) "Hide" else "Show",
                    fontSize = 12.sp,
                    color = Color(0xFF1022FF)
                )
            }

            if (expanded) {
                Spacer(Modifier.height(8.dp))
                module.lessons.forEach {
                    Text("• $it", fontSize = 14.sp, modifier = Modifier.padding(vertical = 2.dp))
                }
            }
        }
    }
}
