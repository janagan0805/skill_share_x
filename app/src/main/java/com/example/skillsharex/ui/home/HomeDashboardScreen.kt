package com.example.skillsharex.ui.home

import android.R.color.white
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.SmartToy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
import androidx.navigation.NavGraph.Companion.findStartDestination
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.skillsharex.R
import com.example.skillsharex.model.CourseData
import com.example.skillsharex.model.MentorData
import com.example.skillsharex.navigation.Routes
import com.example.skillsharex.network.AuthApiClient
import com.example.skillsharex.utils.SessionManager
import com.example.skillsharex.viewmodel.home.DashboardViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeDashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel
) {
    val context = LocalContext.current
    val session = SessionManager(context)
    val userName = session.getUserName()

    var showSubscriptionBanner by remember { mutableStateOf(false) }

    // ✅ Load dashboard data
    LaunchedEffect(Unit) {
        viewModel.loadDashboardData()
    }

    // Check subscription status on launch
    LaunchedEffect(Unit) {
        // Mock check: if user is not subscribed, show banner after 2 seconds
        delay(2000)
        showSubscriptionBanner = true
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Your existing Dashboard Content...

        if (showSubscriptionBanner) {
            SubscriptionBanner(
                onClose = { showSubscriptionBanner = false },
                onSubscribe = {
                    showSubscriptionBanner = false
                    navController.navigate("subscription")
                }
            )
        }
    }

    val swipeState = rememberSwipeRefreshState(
        isRefreshing = viewModel.isLoading
    )

    // Get active courses from ViewModel
    val activeCourses by remember { derivedStateOf {
        viewModel.courses.filter { it.status == "active" }
    } }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Routes.CHATBOT) },
                containerColor = Color(0xFF0285ff),
                shape = RoundedCornerShape(50)
            ) {
                Icon( painter = painterResource(id = R.drawable.ai_chat), null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(26.dp))
            }
        }
    ) { padding ->

        SwipeRefresh(
            state = swipeState,
            onRefresh = { viewModel.loadDashboardData(force = true) },
            modifier = Modifier.padding(padding)
        ) {

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {

                // 1. HEADER SECTION
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(0.dp)
                    ) {

                        HomeDashboardHeader()
                        Column(
                            modifier = Modifier.padding(
                                horizontal = 16.dp,
                                vertical = 0.dp
                            )
                        ) {

                            Spacer(modifier = Modifier.height(16.dp))

                            // 2. CATEGORY CHIPS ROW
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(listOf("UI/UX", "Android", "Java", "Design", "React", "Node", "Python")) { category ->
                                    Surface(
                                        onClick = {},
                                        shape = RoundedCornerShape(15.dp),
                                        color = Color.White,
                                        border = androidx.compose.foundation.BorderStroke(
                                            width = 1.dp,
                                            color = Color.Black.copy(alpha = 0.15f)
                                        )
                                    ) {
                                        Text(
                                            text = category,
                                            color = Color.Black.copy(alpha = 0.6f),
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 14.sp,
                                            modifier = Modifier.padding(
                                                horizontal = 16.dp,
                                                vertical = 8.dp
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // 3. ACTIVE SESSION CARD
                item {
//                    ActiveSessionCard(navController, viewModel)
                    // Example usage with dynamic data

                    val activeSession = viewModel.courses.firstOrNull { it.status == "active" }

                    ActiveSessionCard(
                        mentorName = activeSession?.mentor_name ?: "No Mentor",
                        courseName = activeSession?.course_name ?: "No Course",
                        onViewAllSessions = {
                            // Navigate to Sessions screen
                            navController.navigate(Routes.SESSIONS)
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // 4. COURSES AVAILABLE NOW

                item {
                    Spacer(modifier = Modifier.height(12.dp))
                }

                item {
                    CoursesAvailableNowSection(
                        courses = activeCourses,
                        onCourseClick = { course ->
                            navController.navigate("courseDetail/${course.id}")
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }

                // 6. TOP MENTORS FOR YOU

                item {
                    Spacer(modifier = Modifier.height(12.dp))
                }

                item {
                    TopMentorsSection(
                        mentors = viewModel.mentors,
                        onMentorClick = { mentor ->
                            navController.navigate("mentorDetail/${mentor.id}")
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}

/* ------------------ HEADER SECTION ---------------*/

@Composable
fun HomeDashboardHeader() {
    val context = LocalContext.current
    val session = SessionManager(context)
    val userName = session.getUserName()

    // Purple gradient colors
    val gradientColors = listOf(
        Color(0xFF5B4EDB), // Left color
        Color(0xFF7C64D9)  // Right color
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = gradientColors
                ),
                shape = RoundedCornerShape(
                    bottomStart = 25.dp,
                    bottomEnd = 25.dp,
                    topStart = 0.dp,
                    topEnd = 0.dp
                )
            ),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        // Padding for the content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 20.dp,
                    top = 60.dp,
                    end = 20.dp,
                    bottom = 10.dp
                ),
            verticalArrangement = Arrangement.Top
        ) {
            // Primary Text: Welcome message
            Text(
                text = "Welcome, ${userName ?: "User"} 👋",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            // Secondary Text: Subtitle
            Text(
                text = "Let's continue learning",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

/* ---------------- ACTIVE SESSION CARD ---------------- */

@Composable
fun ActiveSessionCard(
    mentorName: String,
    courseName: String,
    onViewAllSessions: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // 1. TITLE
            Text(
                text = "Your Active Session",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF111827)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 2. SUBTITLE
            Text(
                text = "$mentorName • $courseName",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF6B7280),
                maxLines = 2,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 3. PRIMARY ACTION BUTTON
            Button(
                onClick = onViewAllSessions,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF544DCA)
                )
            ) {
                Text(
                    text = "View All Sessions",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }
    }
}

/* ---------------- COURSES AVAILABLE NOW SECTION ---------------- */
@Composable
fun CoursesAvailableNowSection(
    courses: List<CourseData>,
    onCourseClick: (CourseData) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // SECTION TITLE
        Text(
            text = "Courses Available Now",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF111827),
            modifier = Modifier.padding(
                start = 16.dp,
                bottom = 12.dp
            )
        )

        // COURSE LIST
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(courses) { course ->
                CourseCardHorizontal(
                    course = course,
                    onClick = { onCourseClick(course) }
                )
            }
        }
    }
}

@Composable
fun CourseCardHorizontal(
    course: CourseData,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(250.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // COURSE IMAGE SECTION
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(course.cover_image?.let { AuthApiClient.IMAGE_BASE_URL + it })
                        .crossfade(true)
                        .build(),
                    contentDescription = course.course_name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )

                // STATUS BADGE (only if active)
                if (course.status == "active") {
                    Surface(
                        color = Color(0xFF22C55E),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .padding(top = 12.dp, end = 12.dp)
                            .align(Alignment.TopEnd)
                    ) {
                        Text(
                            text = "ACTIVE",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(
                                horizontal = 8.dp,
                                vertical = 4.dp
                            )
                        )
                    }
                }
            }

            // CARD CONTENT
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // COURSE TITLE
                Text(
                    text = course.course_name ?: "Untitled Course",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF111827),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // MENTOR NAME WITH ONLINE INDICATOR
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // MENTOR NAME
                    Text(
                        text = course.mentor_name ?: "Unknown Mentor",
                        fontSize = 14.sp,
                        color = Color(0xFF6B7280),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    // ONLINE INDICATOR DOT

                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                if (course.mentor_online_status == "online")
                                    Color.Green else Color.Gray,
                                shape = RoundedCornerShape(50)
                            )
                    )
                }
            }
        }
    }
}

/* ---------------- TOP MENTORS SECTION ---------------- */
@Composable
fun TopMentorsSection(
    mentors: List<MentorData>,
    onMentorClick: (MentorData) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // SECTION TITLE
        Text(
            text = "Top Mentors For You",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF111827),
            modifier = Modifier.padding(
                start = 16.dp,
                bottom = 12.dp
            )
        )

        // MENTOR LIST
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(mentors) { mentor ->
                MentorCardHorizontal(
                    mentor = mentor,
                    onClick = { onMentorClick(mentor) }
                )
            }
        }
    }
}

@Composable
fun MentorCardHorizontal(
    mentor: MentorData,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // MENTOR IMAGE SECTION
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(mentor.imageUrl?.let { AuthApiClient.IMAGE_BASE_URL + it })
                        .crossfade(true)
                        .build(),
                    contentDescription = mentor.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .padding(13.dp)
                        .clip(RoundedCornerShape(16.dp))
                )

                // ONLINE STATUS INDICATOR
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = (-20).dp, y = (-20).dp) // 🔑 moves dot inward
                        .size(12.dp)
                        .background(
                            if (mentor.status.equals("online", ignoreCase = true))
                                Color(0xFF22C55E) else Color.Gray,
                            shape = RoundedCornerShape(50)
                        )
                        .border(
                            width = 2.dp,
                            color = Color.White,
                            shape = RoundedCornerShape(50)
                        )
                )
            }

            // CARD CONTENT
            Column(
                modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 16.dp)
            ) {
                // MENTOR SKILL / CATEGORY
                Text(
                    text = mentor.skill.firstOrNull() ?: "Mentor",
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                // MENTOR NAME
                Text(
                    text = mentor.name ?: "Unknown Mentor",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF111827),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // RATING ROW
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.star_2),
                        contentDescription = "Rating",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "%.1f".format(mentor.rating ?: 0.0),
                        fontSize = 14.sp,
                        color = Color(0xFF111827),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun SubscriptionBanner(onClose: () -> Unit, onSubscribe: () -> Unit) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f)),
        contentAlignment = Alignment.BottomCenter
    ) {

        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Column(Modifier.padding(16.dp)) {

                Text("Upgrade to Pro 🚀", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                Text("Unlock premium features & mentorship")

                Spacer(Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    TextButton(onClick = onClose) {
                        Text("Later")
                    }

                    Button(onClick = onSubscribe) {
                        Text("Go Pro")
                    }
                }
            }
        }
    }
}