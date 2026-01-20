package com.example.skillsharex.ui.course

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
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
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.skillsharex.R
import com.example.skillsharex.navigation.Routes
import com.example.skillsharex.network.AuthApiClient
import com.example.skillsharex.viewmodel.CourseDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailScreen(
    navController: NavController,
    viewModel: CourseDetailViewModel = viewModel()
) {
    val courseId = navController.currentBackStackEntry?.arguments?.getInt("courseId") ?: return
    val tabTitles = listOf("Overview", "Sessions", "Reviews")
    var selectedTab by remember { mutableIntStateOf(0) }

    val isSessionsEnabled = false
    val isReviewsEnabled = false

    val isActive = viewModel.courseDetail?.status == "active"

    LaunchedEffect(courseId) {
        viewModel.loadCourseDetail(courseId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Course Details",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF111827)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            // BOTTOM CTA
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Button(
                    enabled = isActive,
                    onClick = {
                        navController.navigate("${Routes.ENROLLED_COURSE}/$courseId")
                              },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF544DCA)
                    )
                ) {
                    Text(
                        text = "Enroll Now",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
            }
        }
    ) { paddingValues ->
        when {
            viewModel.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            viewModel.errorMessage != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(viewModel.errorMessage ?: "Error")
                }
            }

            else -> {
                val course = viewModel.courseDetail ?: return@Scaffold

                LazyColumn(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                ) {
                    // HERO COURSE IMAGE
                    item {

                        val heroShape = RoundedCornerShape(
                            bottomStart = 24.dp,
                            bottomEnd = 24.dp
                        )
                        Box(
                            modifier = Modifier.fillMaxWidth().clip(heroShape)
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
                                    .height(240.dp)
                                    .clip(
                                        RoundedCornerShape(
                                            bottomStart = 32.dp,
                                            bottomEnd = 32.dp
                                        )
                                    )
                            )

                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Black.copy(alpha = 0.75f),
                                                Color.Transparent
                                            ),
                                            startY = Float.POSITIVE_INFINITY,
                                            endY = 0f
                                        )
                                    )
                            )

                            // ACTIVE BADGE
//                            Surface(
//                                color = Color(0xFF22C55E),
//                                shape = RoundedCornerShape(12.dp),
//                                modifier = Modifier
//                                    .padding(start = 16.dp, top = 16.dp)
//                                    .align(Alignment.BottomStart)
//                            ) {
//                                Text(
//                                    text = "ACTIVE",
//                                    color = Color.White,
//                                    fontSize = 11.sp,
//                                    fontWeight = FontWeight.Bold,
//                                    modifier = Modifier.padding(
//                                        horizontal = 10.dp,
//                                        vertical = 5.dp
//                                    )
//                                )
//                            }

                            // COURSE TITLE & RATING OVERLAY // ACTIVE BADGE
                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(start = 24.dp, bottom = 24.dp)
                            ) {

                                // Status badge
                                Surface(
                                    color = if (isActive) Color(0xFF22C55E) else Color(0xFF9CA3AF),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.padding(
                                        horizontal = 0.dp,
                                        vertical = 10.dp
                                    )
                                ) {
                                    Text(
                                        text = if (isActive) "ACTIVE" else "INACTIVE",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(
                                            horizontal = 10.dp,
                                            vertical = 5.dp
                                        )
                                    )
                                }

                                Text(
                                    text = course.course_name,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                // RATING ROW
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.star_2),
                                        contentDescription = "Rating",
                                        tint = Color(0xFFFACC15),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${course.rating} · ${course.rating_count} reviews",
                                        fontSize = 14.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }

                    // MENTOR INFO CARD
                    item {
                        Card(
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 20.dp)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // MENTOR AVATAR WITH ONLINE INDICATOR
                                Box(
                                    modifier = Modifier.size(56.dp)
                                ) {
                                    SubcomposeAsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(course.mentor?.image?.let { AuthApiClient.IMAGE_BASE_URL + it })
                                            .crossfade(true)
                                            .build(),
                                        loading = {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(24.dp)
                                            )
                                        },
                                        contentDescription = course.mentor?.name,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(56.dp)
                                            .clip(CircleShape)
                                    )

                                    // ONLINE INDICATOR
                                    Box(
                                        modifier = Modifier
                                            .size(14.dp)
                                            .background(
                                                Color.Green,
                                                shape = CircleShape
                                            )
                                            .align(Alignment.BottomEnd)
                                            .border(
                                                width = 2.dp,
                                                color = Color.White,
                                                shape = RoundedCornerShape(50)
                                            )
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                // MENTOR INFO
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = course.mentor?.name ?: "Sarah Mitchell",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF111827)
                                    )
                                    Text(
                                        text = "${course.mentor?.skill} Developer",
                                        fontSize = 14.sp,
                                        color = Color(0xFF6B7280)
                                    )
                                }

                                // VIEW PROFILE BUTTON
                                TextButton(
                                    onClick = {
                                        navController.navigate("mentorDetail/${course.mentor.id}") }
                                ) {
                                    Text(
                                        text = "View Profile",
                                        color = Color(0xFF544DCA),
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }

                    // TAB BAR
                    item {
                        TabRow(
                            selectedTabIndex = selectedTab,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth(),
                            indicator = { tabPositions ->
                                if (selectedTab == 0) {
                                    TabRowDefaults.Indicator(
                                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                                        height = 3.dp,
                                        color = Color(0xFF544DCA)
                                    )
                                }
                            },
                            divider = {}
                        ) {
                            tabTitles.forEachIndexed { index, title ->

                                val enabled = when (index) {
                                    1 -> isSessionsEnabled
                                    2 -> isReviewsEnabled
                                    else -> true
                                }

                                Tab(
                                    selected = selectedTab == index,
                                    enabled = enabled,
                                    onClick = {
                                        if (enabled) selectedTab = index
                                    },
                                    text = {
                                        Text(
                                            text = title,
                                            color = when {
                                                !enabled -> Color(0xFF9CA3AF) // disabled gray
                                                selectedTab == index -> Color(0xFF544DCA)
                                                else -> Color(0xFF6B7280)
                                            },
                                            fontWeight = if (selectedTab == index && enabled)
                                                FontWeight.SemiBold
                                            else
                                                FontWeight.Normal
                                        )
                                    }
                                )
                            }
                        }
                    }

                    // TAB CONTENT
                    when (selectedTab) {
                        0 -> { // OVERVIEW TAB
                            // About this course
                            item {
                                Column(
                                    modifier = Modifier.padding(24.dp)
                                ) {
                                    Text(
                                        text = "About this course",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF111827),
                                        modifier = Modifier.padding(bottom = 12.dp)
                                    )
                                    Text(
                                        text = "Master the art of creating user-centered designs with this comprehensive UI/UX course. Learn design thinking, user research, wireframing, prototyping, and usability testing to create exceptional digital experiences.",
                                        fontSize = 14.sp,
                                        color = Color(0xFF4B5563),
                                        lineHeight = 22.sp
                                    )
                                }
                            }

                            // What you'll learn
                            item {
                                Column(
                                    modifier = Modifier.padding(horizontal = 24.dp)
                                ) {
                                    Text(
                                        text = "What you'll learn",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF111827),
                                        modifier = Modifier.padding(bottom = 16.dp)
                                    )

                                    val learnPoints = listOf(
                                        "Understand core principles of user-centered design",
                                        "Conduct effective user research and create personas",
                                        "Create wireframes and interactive prototypes",
                                        "Apply design thinking methodologies",
                                        "Perform usability testing and iterate on designs",
                                        "Build comprehensive design systems"
                                    )

                                    learnPoints.forEach { point ->
                                        Row(
                                            verticalAlignment = Alignment.Top,
                                            modifier = Modifier.padding(bottom = 12.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Outlined.CheckCircle,
                                                contentDescription = "Check",
                                                tint = Color(0xFF22C55E),
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Text(
                                                text = point,
                                                fontSize = 14.sp,
                                                color = Color(0xFF4B5563),
                                                modifier = Modifier.weight(1f)
                                            )
                                        }
                                    }
                                }
                            }

                            // Skills covered
                            item {
                                Column(
                                    modifier = Modifier.padding(24.dp)
                                ) {
                                    Text(
                                        text = "Skills covered",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF111827),
                                        modifier = Modifier.padding(bottom = 12.dp)
                                    )

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        SkillChip("Figma")
                                        SkillChip("User Research")
                                        SkillChip("Prototyping")
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        SkillChip("Design Systems")
                                        SkillChip("Usability Testing")
                                    }
                                }
                            }

                            // Course meta card
                            item {
                                Card(
                                    modifier = Modifier
                                        .padding(horizontal = 24.dp, vertical = 8.dp)
                                        .fillMaxWidth(),
                                    shape = RoundedCornerShape(20.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(20.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "Level",
                                                fontSize = 14.sp,
                                                color = Color(0xFF6B7280)
                                            )
                                            Text(
                                                text = "Intermediate",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = Color(0xFF111827)
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(12.dp))
                                        Divider(color = Color(0xFFF3F4F6))

                                        Spacer(modifier = Modifier.height(12.dp))

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "Duration",
                                                fontSize = 14.sp,
                                                color = Color(0xFF6B7280)
                                            )
                                            Text(
                                                text = "8 weeks",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = Color(0xFF111827)
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(12.dp))
                                        Divider(color = Color(0xFFF3F4F6))

                                        Spacer(modifier = Modifier.height(12.dp))

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "Language",
                                                fontSize = 14.sp,
                                                color = Color(0xFF6B7280)
                                            )
                                            Text(
                                                text = "English",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = Color(0xFF111827)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        1 -> { // SESSIONS TAB
                            val sessions = listOf(
                                SessionData(
                                    title = "Introduction to UI/UX Fundamentals",
                                    date = "Jan 22, 2026",
                                    time = "2:00 PM - 3:30 PM",
                                    status = SessionStatus.COMPLETED
                                ),
                                SessionData(
                                    title = "Design Thinking Workshop",
                                    date = "Jan 23, 2026",
                                    time = "3:00 PM - 4:30 PM",
                                    status = SessionStatus.LIVE
                                ),
                                SessionData(
                                    title = "User Research Methods",
                                    date = "Jan 25, 2026",
                                    time = "2:00 PM - 3:30 PM",
                                    status = SessionStatus.UPCOMING
                                ),
                                SessionData(
                                    title = "Wireframing & Prototyping",
                                    date = "Jan 27, 2026",
                                    time = "3:00 PM - 4:30 PM",
                                    status = SessionStatus.UPCOMING
                                )
                            )

                            items(sessions) { session ->
                                SessionCard(session = session)
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }

                        2 -> { // REVIEWS TAB
                            val reviews = listOf(
                                ReviewData(
                                    reviewerName = "Alex Thompson",
                                    date = "Jan 18, 2026",
                                    rating = 5,
                                    reviewText = "Excellent course! Sarah is an amazing mentor who breaks down complex concepts into easy-to-understand lessons. Highly recommend!"
                                ),
                                ReviewData(
                                    reviewerName = "Maria Santos",
                                    date = "Jan 15, 2026",
                                    rating = 4,
                                    reviewText = "This course has transformed my design skills. The practical exercises and real-world examples are invaluable."
                                ),
                                ReviewData(
                                    reviewerName = "David Kim",
                                    date = "Jan 12, 2026",
                                    rating = 4,
                                    reviewText = "Great content and well-structured. Would love to see more advanced topics covered in future sessions."
                                )
                            )

                            items(reviews) { review ->
                                ReviewCard(review = review)
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun SkillChip(skill: String) {
    Surface(
        color = Color(0xFFF5F3FF),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = skill,
            color = Color(0xFF544DCA),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}

data class SessionData(
    val title: String,
    val date: String,
    val time: String,
    val status: SessionStatus
)

enum class SessionStatus {
    COMPLETED, LIVE, UPCOMING, LOCKED
}

@Composable
fun SessionCard(session: SessionData) {
    Card(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = session.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827),
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = "${session.date} · ${session.time}",
                fontSize = 14.sp,
                color = Color(0xFF6B7280),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // STATUS BADGE
                Surface(
                    color = when (session.status) {
                        SessionStatus.COMPLETED -> Color(0xFF6B7280)
                        SessionStatus.LIVE -> Color(0xFFEF4444)
                        SessionStatus.UPCOMING -> Color(0xFF3B82F6)
                        SessionStatus.LOCKED -> Color(0xFFF3F4F6)
                    },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = when (session.status) {
                            SessionStatus.COMPLETED -> "Completed"
                            SessionStatus.LIVE -> "Live"
                            SessionStatus.UPCOMING -> "Upcoming"
                            SessionStatus.LOCKED -> "Locked"
                        },
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // BUTTON
                Button(
                    onClick = { /* Button action */ },
                    modifier = Modifier.height(36.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = when (session.status) {
                            SessionStatus.COMPLETED -> Color(0xFFF3F4F6)
                            SessionStatus.LIVE -> Color(0xFF544DCA)
                            SessionStatus.UPCOMING -> Color(0xFF6B7280)
                            SessionStatus.LOCKED -> Color(0xFFF3F4F6)
                        }
                    )
                ) {
                    Text(
                        text = when (session.status) {
                            SessionStatus.COMPLETED -> "View Recording"
                            SessionStatus.LIVE -> "Join Now"
                            SessionStatus.UPCOMING -> "Set Reminder"
                            SessionStatus.LOCKED -> "Lock"
                        },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = when (session.status) {
                            SessionStatus.COMPLETED -> Color(0xFF6B7280)
                            SessionStatus.LIVE -> Color.White
                            SessionStatus.UPCOMING -> Color.White
                            SessionStatus.LOCKED -> Color(0xFF6B7280)
                        }
                    )
                }
            }
        }
    }
}

data class ReviewData(
    val reviewerName: String,
    val date: String,
    val rating: Int,
    val reviewText: String
)

@Composable
fun ReviewCard(review: ReviewData) {
    Card(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // REVIEWER HEADER
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // AVATAR
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0xFFE5E7EB), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = review.reviewerName.take(2).uppercase(),
                        color = Color(0xFF544DCA),
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // REVIEWER INFO
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = review.reviewerName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF111827)
                        )
                        Text(
                            text = review.date,
                            fontSize = 12.sp,
                            color = Color(0xFF6B7280)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // STAR RATING
                    Row {
                        repeat(5) { index ->
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "Star",
                                tint = if (index < review.rating) Color(0xFFFACC15) else Color(0xFFE5E7EB),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // REVIEW TEXT
            Text(
                text = review.reviewText,
                fontSize = 14.sp,
                color = Color(0xFF4B5563),
                lineHeight = 22.sp
            )
        }
    }
}
