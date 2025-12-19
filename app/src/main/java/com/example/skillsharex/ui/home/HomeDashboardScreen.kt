package com.example.skillsharex.ui.home

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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skillsharex.R

/* ---------------- COURSE MODEL ---------------- */

data class Course(
    val id: String,
    val title: String,
    val author: String,
    val image: Int
)

/* ---------------- SAMPLE COURSES ---------------- */

private val sampleCourses = listOf(
    Course("1", "UI/UX Design", "Saranraj", R.drawable.ic_ui_ux_design),
    Course("2", "Android Development", "Karthick", R.drawable.android),
    Course("3", "Web Development", "Sriram", R.drawable.ic_web),
    Course("4", "Java Programming", "Pranav", R.drawable.ic_java),
    Course("5", "Graphic Design", "Gowtham", R.drawable.ic_graphics),
    Course("6", "Python Basics", "Dilip", R.drawable.ic_python),
    Course("7", "Photoshop", "Dhanush", R.drawable.ic_photoshop),
    Course("8", "Figma Masterclass", "Dhanacheziyan", R.drawable.ic_figma),
    Course("9", "Communication Skills", "Suriya", R.drawable.ic_communication)
)

private val topMentorCourses = sampleCourses.take(3)
private val popularCourses = sampleCourses.drop(3).take(3)
private val recommendedCourses = sampleCourses.drop(6).take(3)

/* ---------------- HOME DASHBOARD ---------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeDashboardScreen(
    onOpenProfile: () -> Unit,
    onOpenOverview: (String) -> Unit,
    onOpenCourses: () -> Unit,
    onOpenMentors: () -> Unit,
    onOpenCommunity: () -> Unit,
    onOpenNotifications: () -> Unit
) {

    val scrollState = rememberScrollState()
    val categories = listOf("UI/UX", "Website Design", "Java", "Photoshop", "Music", "Time Management")

    Scaffold(
        containerColor = Color(0xFFE8E6FF),
        bottomBar = {
            BottomBar(
                onProfileClick = onOpenProfile,
                onOpenCourses = onOpenCourses,
                onOpenMentors = onOpenMentors,
                onOpenCommunity = onOpenCommunity
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {

            /* -------- HEADER -------- */

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF544DCA)),
                shape = RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    Text(
                        text = "Welcome Jana",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, RoundedCornerShape(50.dp))
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF425CFF))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Search Here", color = Color.Gray)
                        Spacer(modifier = Modifier.weight(1f))

                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = null,
                            tint = Color(0xFF425CFF),
                            modifier = Modifier.clickable { onOpenNotifications() }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            /* -------- CATEGORY CHIPS -------- */

            LazyRow(modifier = Modifier.padding(start = 12.dp)) {
                items(categories) { item ->
                    AssistChip(
                        onClick = {},
                        label = { Text(item, fontWeight = FontWeight.SemiBold) },
                        modifier = Modifier.padding(end = 10.dp),
                        shape = RoundedCornerShape(30.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            CourseSection("Top Mentor Courses", topMentorCourses, onOpenOverview)
            CourseSection("Popular Courses", popularCourses, onOpenOverview)
            CourseSection("Recommended For You", recommendedCourses, onOpenOverview)

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

/* ---------------- COURSE SECTION ---------------- */

@Composable
fun CourseSection(
    title: String,
    courses: List<Course>,
    onCourseClick: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {

        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 18.dp, bottom = 10.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(courses) { course ->
                CourseItemCard(
                    course = course,
                    onClick = { onCourseClick(course.id) }
                )
            }
        }
    }
}

/* ---------------- COURSE CARD ---------------- */

@Composable
fun CourseItemCard(
    course: Course,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(170.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {

            Image(
                painter = painterResource(id = course.image),
                contentDescription = course.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
            )

            Text(
                text = course.title,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
            )

            Text(
                text = "By ${course.author}",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
            )
        }
    }
}

/* ---------------- BOTTOM NAV BAR ---------------- */

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    onProfileClick: () -> Unit,
    onOpenCourses: () -> Unit,
    onOpenMentors: () -> Unit,
    onOpenCommunity: () -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
    ) {

        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = { Icon(Icons.Default.Home, contentDescription = "Home", tint = Color(0xFF425CFF)) },
            label = { Text("Home") }
        )

        NavigationBarItem(
            selected = false,
            onClick = onOpenCommunity,
            icon = { Icon(Icons.Default.People, contentDescription = "Community") },
            label = { Text("Community") }
        )

        NavigationBarItem(
            selected = false,
            onClick = onOpenCourses,
            icon = { Icon(Icons.Outlined.Book, contentDescription = "Courses") },
            label = { Text("Courses") }
        )

        NavigationBarItem(
            selected = false,
            onClick = onOpenMentors,
            icon = { Icon(Icons.Default.Person, contentDescription = "Mentors") },
            label = { Text("Mentors") }
        )

        NavigationBarItem(
            selected = false,
            onClick = onProfileClick,
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") }
        )
    }
}
