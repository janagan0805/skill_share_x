package com.example.skillsharex.ui.course

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.FileCopy
import androidx.compose.material.icons.outlined.LibraryBooks
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.skillsharex.R
import com.example.skillsharex.network.AuthApiClient
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnrolledCourseScreen(
    navController: NavController
) {
    // DEMO DATA
    val courseTitle = "UI/UX Design Masterclass"
    val mentorName = "Sarah Mitchell"
    val mentorRole = "Senior UI/UX Designer"
    val progress = 35
    val nextSession = "User Research Methods"
    val nextSessionTime = "Jan 25, 2026 · 2:00 PM - 3:30 PM"
    val showComingSoonOverlay = remember { mutableStateOf(true) }

    val sessions = listOf(
        SessionItem(
            "Introduction to UI/UX Fundamentals",
            "Recorded",
            "Jan 22, 2026 · 2:00 PM - 3:30 PM",
            SessionStatus.COMPLETED
        ),
        SessionItem(
            "Design Thinking Workshop",
            "Recorded",
            "Jan 23, 2026 · 3:00 PM - 4:30 PM",
            SessionStatus.COMPLETED
        ),
        SessionItem(
            "User Research Methods",
            "Live",
            "Jan 25, 2026 · 2:00 PM - 3:30 PM",
            SessionStatus.LIVE
        ),
        SessionItem(
            "Wireframing & Prototyping",
            "Live",
            "Jan 27, 2026 · 3:00 PM - 4:30 PM",
            SessionStatus.LOCKED
        ),
        SessionItem(
            "Usability Testing & Iteration",
            "Recorded",
            "Jan 29, 2026 · 2:00 PM - 3:30 PM",
            SessionStatus.LOCKED
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Learning",
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
                ),
                actions = {
                    IconButton(onClick = { /* Share action */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                }
            )
        }
    ) { padding ->

        Box(modifier = Modifier.fillMaxSize()) {
            // MAIN CONTENT
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // 1. HERO SECTION
                item {
                    val heroShape = RoundedCornerShape(
                        bottomStart = 32.dp,
                        bottomEnd = 32.dp
                    )
                    Box(
                        modifier = Modifier.fillMaxWidth().clip(heroShape)
                    ) {
                        // Course cover image
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data("https://images.unsplash.com/photo-1611224923853-80b023f02d71")
                                .crossfade(true)
                                .build(),
                            contentDescription = courseTitle,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(280.dp)
                        )

                        // Gradient overlay
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(280.dp)
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color.Black.copy(alpha = 0.7f)
                                        ),
                                        startY = 0f,
                                        endY = 280f
                                    )
                                )
                        )

                        // Content over image
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(start = 24.dp, bottom = 32.dp)
                        ) {
                            // Enrollment badge
                            Surface(
                                color = Color(0xFF22C55E),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.padding(bottom = 12.dp)
                            ) {
                                Text(
                                    text = "ENROLLED",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(
                                        horizontal = 10.dp,
                                        vertical = 5.dp
                                    )
                                )
                            }

                            // Course title
                            Text(
                                text = courseTitle,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )

                            // Mentor name
                            Text(
                                text = "by $mentorName",
                                fontSize = 16.sp,
                                color = Color.White.copy(alpha = 0.9f),
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }

                // Progress indicator
                item {
                    Card(
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Your Progress",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF111827)
                                )
                                Text(
                                    text = "$progress%",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF544DCA)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            LinearProgressIndicator(
                                progress = progress / 100f,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp),
                                color = Color(0xFF544DCA),
                                trackColor = Color(0xFFF3F4F6)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "${sessions.count { it.status == SessionStatus.COMPLETED }} of ${sessions.size} sessions completed",
                                fontSize = 13.sp,
                                color = Color(0xFF6B7280)
                            )
                        }
                    }
                }

                // 2. PRIMARY ACTION CARD
                item {
                    Card(
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F3FF)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Next session",
                                    tint = Color(0xFF544DCA),
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Next Session",
                                        fontSize = 12.sp,
                                        color = Color(0xFF6B7280),
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = nextSession,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF111827),
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = nextSessionTime,
                                        fontSize = 13.sp,
                                        color = Color(0xFF6B7280),
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = { /* Join session */ },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF544DCA)
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VideoCall,
                                    contentDescription = "Join",
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Join Live Session",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                // 3. COURSE SESSIONS SECTION
                item {
                    Text(
                        text = "Course Sessions",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827),
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }

                items(sessions) { session ->
                    SessionCard(session = session)
                }

                // 4. MENTOR SECTION
                item {
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
                                text = "Your Mentor",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF111827),
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Mentor avatar
                                Box(
                                    modifier = Modifier.size(60.dp)
                                ) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data("https://images.unsplash.com/photo-1494790108755-2616b612b786")
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = mentorName,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(60.dp)
                                            .clip(CircleShape)
                                    )

                                    // Online indicator
                                    Box(
                                        modifier = Modifier
                                            .size(12.dp)
                                            .background(Color.Green, shape = CircleShape)
                                            .align(Alignment.BottomEnd)
                                    )
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = mentorName,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF111827)
                                    )
                                    Text(
                                        text = mentorRole,
                                        fontSize = 14.sp,
                                        color = Color(0xFF6B7280)
                                    )
                                }

                                TextButton(
                                    onClick = { /* View profile */ }
                                ) {
                                    Text(
                                        text = "Ask",
                                        color = Color(0xFF544DCA),
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }

                // 5. RESOURCES SECTION
                item {
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
                                text = "Resources",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF111827),
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            ResourceItem(
                                icon = Icons.Outlined.LibraryBooks,
                                title = "Course Notes (PDF)",
                                description = "Complete course materials"
                            )
                            ResourceItem(
                                icon = Icons.Default.DesignServices,
                                title = "Figma Design Files",
                                description = "Practice with real design files"
                            )
                            ResourceItem(
                                icon = Icons.Outlined.FileCopy,
                                title = "UX Templates",
                                description = "Ready-to-use templates"
                            )
                            ResourceItem(
                                icon = Icons.Outlined.Download,
                                title = "Extra Reading Materials",
                                description = "Additional resources & articles"
                            )
                        }
                    }
                }

                // 6. REVIEW SECTION (LOCKED)
                item {
                    Card(
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF8FAFC)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Locked",
                                tint = Color(0xFF94A3B8),
                                modifier = Modifier.size(32.dp)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Complete 50% to leave a review",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF64748B)
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Share your experience and help others",
                                fontSize = 13.sp,
                                color = Color(0xFF94A3B8),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(80.dp)) }
            }

            // COMING SOON OVERLAY
            AnimatedVisibility(
                visible = showComingSoonOverlay.value,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.7f))
                        .clickable { /* Do nothing - overlay is non-interactive */ }
                ) {
                    Card(
                        modifier = Modifier
                            .padding(40.dp)
                            .align(Alignment.Center),
                        shape = RoundedCornerShape(28.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(32.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.HourglassEmpty,
                                contentDescription = "Coming Soon",
                                tint = Color(0xFF544DCA),
                                modifier = Modifier.size(64.dp)
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Text(
                                text = "Coming Soon",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF111827)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "This learning experience is currently under development.",
                                fontSize = 16.sp,
                                color = Color(0xFF4B5563),
                                textAlign = TextAlign.Center,
                                lineHeight = 24.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "We're building something great for you!",
                                fontSize = 14.sp,
                                color = Color(0xFF6B7280),
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(32.dp))

                            Button(
                                onClick = { showComingSoonOverlay.value = false },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF544DCA)
                                )
                            ) {
                                Text(
                                    text = "Preview Anyway",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            TextButton(
                                onClick = { navController.popBackStack() },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Go Back",
                                    color = Color(0xFF6B7280)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// DATA CLASSES
data class SessionItem(
    val title: String,
    val type: String,
    val time: String,
    val status: SessionStatus
)


// COMPONENTS
@Composable
fun SessionCard(session: SessionItem) {
    Card(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = when (session.status) {
                SessionStatus.COMPLETED -> Color(0xFFF8FAFC)
                SessionStatus.LIVE -> Color(0xFFF5F3FF)
                SessionStatus.LOCKED -> Color(0xFFF8FAFC)
                SessionStatus.UPCOMING -> Color(0xFFF8FAFC)
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    // Status badge
                    Surface(
                        color = when (session.status) {
                            SessionStatus.COMPLETED -> Color(0xFFD1FAE5)
                            SessionStatus.LIVE -> Color(0xFFFEE2E2)
                            SessionStatus.LOCKED -> Color(0xFFF3F4F6)
                            SessionStatus.UPCOMING -> Color(0xFFF3F4F6)
                        },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Text(
                            text = when (session.status) {
                                SessionStatus.COMPLETED -> "COMPLETED"
                                SessionStatus.LIVE -> "LIVE"
                                SessionStatus.LOCKED -> "LOCKED"
                                SessionStatus.UPCOMING -> "UPCOMING"
                            },
                            color = when (session.status) {
                                SessionStatus.COMPLETED -> Color(0xFF065F46)
                                SessionStatus.LIVE -> Color(0xFF991B1B)
                                SessionStatus.LOCKED -> Color(0xFF6B7280)
                                SessionStatus.UPCOMING -> Color(0xFF6B7280)
                            },
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    // Session title
                    Text(
                        text = session.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF111827),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    // Session info
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (session.type == "Live") Icons.Default.VideoCall else Icons.Default.VideoLibrary,
                            contentDescription = session.type,
                            tint = Color(0xFF6B7280),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${session.type} · ${session.time}",
                            fontSize = 13.sp,
                            color = Color(0xFF6B7280)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Action button
                when (session.status) {
                    SessionStatus.COMPLETED -> {
                        OutlinedButton(
                            onClick = { /* Watch recording */ },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFF6B7280)
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Watch",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Watch")
                        }
                    }
                    SessionStatus.LIVE -> {
                        Button(
                            onClick = { /* Join session */ },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF544DCA)
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.VideoCall,
                                contentDescription = "Join",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Join")
                        }
                    }
                    SessionStatus.LOCKED -> {
                        IconButton(
                            onClick = { /* Locked - no action */ },
                            enabled = false
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Locked",
                                tint = Color(0xFF94A3B8)
                            )
                        }
                    }
                    SessionStatus.UPCOMING -> {
                        IconButton(
                            onClick = { /* Upcoming - no action */ },
                            enabled = false
                        ) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Upcoming",
                                tint = Color(0xFF94A3B8)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ResourceItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = Color(0xFF544DCA),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF111827)
            )
            Text(
                text = description,
                fontSize = 13.sp,
                color = Color(0xFF6B7280)
            )
        }
        IconButton(
            onClick = { /* Download/view */ },
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Download,
                contentDescription = "Download",
                tint = Color(0xFF6B7280)
            )
        }
    }
}