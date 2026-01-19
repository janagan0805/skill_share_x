package com.example.skillsharex.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.SmartToy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
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
import com.example.skillsharex.utils.RefreshBus
import com.example.skillsharex.utils.RefreshEvent
import com.example.skillsharex.utils.SessionManager
import com.example.skillsharex.viewmodel.home.DashboardViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

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

    // ✅ Load ONCE
    LaunchedEffect(Unit) {
        viewModel.loadDashboardData()
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(
            Lifecycle.State.STARTED
        ) {
            RefreshBus.events.collect { event ->
                when (event) {
                    RefreshEvent.ProfileUpdated -> {
                        viewModel.loadDashboardData(force = true)
                    }
                    else -> Unit
                }
            }
        }
    }



    val swipeState = rememberSwipeRefreshState(
        isRefreshing = viewModel.isLoading
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Routes.CHATBOT) },
                containerColor = Color(0xFFFACC15),
                shape = RoundedCornerShape(50)
            ) {
                Icon(Icons.Outlined.SmartToy, null)
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

                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF544DCA)),
                        shape = RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(20.dp)) {
                            Text(
                                text = "Welcome ${userName ?: "User"} 👋",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                item {
                    Spacer(Modifier.height(16.dp))
                }

                item {
                    LazyRow(
                        modifier = Modifier.padding(start = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(categories) {
                            AssistChip(
                                onClick = {},
                                label = { Text(it) }
                            )
                        }
                    }
                }

                item {
                    ActiveSessionCard(navController)
                }

                item {
                    DashboardSection(
                        title = "Courses Available Now",
                        items = viewModel.activeCourses
                    ) { course ->
                        CourseCard(course) {
                            navController.navigate("courseDetail/${course.id}")
                        }
                    }
                }

                item {
                    DashboardSection(
                        title = "Top Mentors For You",
                        items = viewModel.mentors
                    ) { mentor ->
                        MentorCard(mentor) {
                            navController.navigate("mentorDetail/${mentor.id}")
                        }
                    }
                }

                item {
                    Spacer(Modifier.height(80.dp))
                }
            }
        }
    }
}

/* ---------------- ACTIVE SESSION CARD ---------------- */

@Composable
fun ActiveSessionCard(navController: NavController) {
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
                    navController.navigate(Routes.SESSIONS) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
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
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No active courses right now.",
                    color = Color.Gray
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
                    .data(AuthApiClient.IMAGE_BASE_URL + mentor.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = mentor.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
            )

            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = "${mentor.name ?: "Unknown"}",
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "Skills : ${mentor.skill ?: "No Skill"}",
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
                                if (mentor.status.equals("online", true))
                                    Color.Green else Color.Gray,
                                shape = RoundedCornerShape(50)
                            )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (mentor.status.equals("online", true)) "Online" else "Offline",
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
                                if (course.mentor_online_status.equals("online", true))
                                    Color.Green else Color.Gray,
                                shape = RoundedCornerShape(50)
                            )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (course.mentor_online_status.equals("online", true))
                            "Mentor is Online" else "Mentor is Offline",
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
