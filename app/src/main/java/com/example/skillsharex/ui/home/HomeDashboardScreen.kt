package com.example.skillsharex.ui.home

import android.util.Log
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
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.skillsharex.R
import com.example.skillsharex.model.CourseData
import com.example.skillsharex.model.MentorData
import com.example.skillsharex.navigation.safeNavigate
import com.example.skillsharex.network.AuthApiClient
import com.example.skillsharex.utils.SessionManager
import com.example.skillsharex.viewmodel.DashboardViewModel

/* ---------------- HOME DASHBOARD ---------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeDashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel = viewModel()
) {

    val context = LocalContext.current
    val session = SessionManager(context)
    val userName = session.getUserName()

    val categories = listOf("UI/UX", "Android", "Java", "Photoshop", "Design", "Career")
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.loadDashboardData()
    }

    Scaffold(
        containerColor = Color(0xFFE8E6FF),
        bottomBar = {
            BottomBar(
                navController = navController,
                onProfileClick = { navController.navigate("profile") },
                onOpenCourses = { navController.navigate("session_list") },
                onOpenMentors = { navController.navigate("mentors") },
                onOpenCommunity = { navController.safeNavigate("community")
                }
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
                        text = "Welcome ${userName ?: "User"} 👋",
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
                        Icon(Icons.Default.Search, null, tint = Color(0xFF425CFF))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Search skills or mentors", color = Color.Gray)
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = null,
                            tint = Color(0xFF425CFF),
                            modifier = Modifier.clickable {
                                navController.navigate("notifications")
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            /* -------- CATEGORY CHIPS -------- */

            LazyRow(modifier = Modifier.padding(start = 12.dp)) {
                items(categories) { item ->
                    AssistChip(
                        onClick = { },
                        label = { Text(item, fontWeight = FontWeight.SemiBold) },
                        modifier = Modifier.padding(end = 10.dp),
                        shape = RoundedCornerShape(30.dp)
                    )
                }
            }

            /* -------- ACTIVE SESSION -------- */

            ActiveSessionCard(navController)

            /* -------- SECTIONS -------- */

            DashboardSection(
                title = "Courses Available Now",
                items = viewModel.courses,
                itemContent = { course ->
                    CourseCard(course = course) {
                        navController.navigate("courseDetail/${course.id}")
                    }
                }
            )

            DashboardSection(
                title = "Top Mentors For You",
                items = viewModel.mentors,
                itemContent = { mentor ->
                    MentorCard(mentor = mentor) {
                        navController.navigate("mentorDetail/${mentor.id}")
                    }
                }
            )

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

/* ---------------- ACTIVE SESSION CARD ---------------- */

@Composable
fun ActiveSessionCard(
    navController: NavController
) {

    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Your Active Session", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            Text("Mentor: Karthick")
            Text("Skill: Android Development")
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    navController.navigate("session_list")
                }
            ) {
                Text("View All Sessions")
            }

        }
    }
}


/* ---------------- DASHBOARD SECTION ---------------- */

@Composable
fun <T> DashboardSection(
    title: String,
    items: List<T>,
    itemContent: @Composable (T) -> Unit
) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 18.dp, bottom = 10.dp)
        )

        if (items.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Nothing to show at the moment.",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        } else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items) { item ->
                    itemContent(item)
                }
            }
        }
    }
}

/* ---------------- MENTOR CARD ---------------- */

@Composable
fun MentorCard(
    mentor: MentorData,
    onClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .clickable { onClick(mentor.id) },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(mentor.imageUrl?.let { AuthApiClient.IMAGE_BASE_URL + it })
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(id = R.drawable.profile),
                error = painterResource(id = R.drawable.profile),
                contentDescription = mentor.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
            )

            Column(modifier = Modifier.padding(10.dp)) {

                Text(
                    text = mentor.skill ?: "No Skill",
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "Mentor: ${mentor.name ?: "Unknown"}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                if (mentor.status.equals("online", ignoreCase = true)) Color.Green else Color.Gray,
                                shape = RoundedCornerShape(50)
                            )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (mentor.status.equals("online", ignoreCase = true)) "Online" else "Offline",
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text("⭐ ${mentor.rating}", fontSize = 12.sp)
                }
            }
        }
    }
}

/* ---------------- COURSE CARD ---------------- */

@Composable
fun CourseCard(
    course: CourseData,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(course.cover_image?.let { AuthApiClient.IMAGE_BASE_URL + it })
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(id = R.drawable.android),
                error = painterResource(id = R.drawable.android),
                contentDescription = course.course_name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
            )

            Column(modifier = Modifier.padding(10.dp)) {

                Text(
                    text = course.course_name ?: "Untitled Course",
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "Mentor: ${course.mentor_name ?: "Unknown"}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                if (course.mentor_online_status.equals("online", ignoreCase = true)) Color.Green else Color.Gray,
                                shape = RoundedCornerShape(50)
                            )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (course.mentor_online_status.equals("online", ignoreCase = true)) "Online" else "Offline",
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

/* ---------------- BOTTOM BAR ---------------- */

@Composable
fun BottomBar(
    navController: NavController,
    onProfileClick: () -> Unit,
    onOpenCourses: () -> Unit,
    onOpenMentors: () -> Unit,
    onOpenCommunity: () -> Unit
) {
    NavigationBar(containerColor = Color.White) {

        NavigationBarItem(
            selected = navController.currentDestination?.route == "home",
            onClick = { navController.navigate("home") },
            icon = { Icon(Icons.Default.Home, null, tint = Color(0xFF425CFF)) },
            label = { Text("Home") }
        )

        NavigationBarItem(
            selected = navController.currentDestination?.route == "community",
            onClick = onOpenCommunity,
            icon = { Icon(Icons.Default.People, null) },
            label = { Text("Community") }
        )

        NavigationBarItem(
            selected = navController.currentDestination?.route == "session_list",
            onClick = onOpenCourses,
            icon = { Icon(Icons.Outlined.Book, null) },
            label = { Text("Sessions") }
        )

        NavigationBarItem(
            selected = navController.currentDestination?.route == "mentors",
            onClick = onOpenMentors,
            icon = { Icon(Icons.Default.Person, null) },
            label = { Text("Mentors") }
        )

        NavigationBarItem(
            selected = navController.currentDestination?.route == "profile",
            onClick = onProfileClick,
            icon = { Icon(Icons.Default.AccountCircle, null) },
            label = { Text("Profile") }
        )
    }
}
