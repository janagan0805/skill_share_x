package com.example.skillsharex.ui.mentorscreen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.skillsharex.R
import com.example.skillsharex.model.CourseData
import com.example.skillsharex.network.AuthApiClient
import com.example.skillsharex.ui.home.MentorCardHorizontal
import com.example.skillsharex.viewmodel.MentorDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MentorDetailScreen(
    navController: NavController,
    mentorId: Int,
    viewModel: MentorDetailViewModel = viewModel()
) {

    LaunchedEffect(mentorId) {
        viewModel.loadMentorDetail(mentorId)
        viewModel.loadMyCourses(mentorId)
    }

    val context = LocalContext.current

    fun openWhatsAppChat(context: Context, phoneNumber: String) {
        val url = "https://wa.me/$phoneNumber"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)

        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "WhatsApp not installed", Toast.LENGTH_SHORT).show()
        }
    }

    fun openDialer(context: Context, phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        context.startActivity(intent)
    }

    Scaffold(
        containerColor = Color(0xFFF8F7FF),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Mentor Profile",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF544DCA),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            MentorActionBar(
                onChatClick = {
                    openWhatsAppChat(context, "91${viewModel.mentor?.phone}")
                },
                onCallClick = {
                    viewModel.mentor?.phone?.let { openDialer(context, it) }
                }
            )
        }
    ) { innerPadding ->

        when {
            viewModel.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            viewModel.errorMessage != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(viewModel.errorMessage ?: "Error", color = Color.Red)
                }
            }

            viewModel.mentor != null -> {
                val mentor = viewModel.mentor!!

                // Filter courses
                val activeCourses = viewModel.mentorCourses.filter { it.status == "active" }
                val inactiveCourses = viewModel.mentorCourses.filter { it.status != "active" }

                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp)
                ) {

                    /* -------- PROFILE SECTION -------- */
                    item {
                        Spacer(Modifier.height(32.dp))

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            // Avatar with online indicator
                            Box(
                                modifier = Modifier.size(130.dp)
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    tonalElevation = 6.dp,
                                    shadowElevation = 8.dp,
                                    color = Color.Transparent
                                ) {
                                    SubcomposeAsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(mentor.image?.let { AuthApiClient.IMAGE_BASE_URL + it })
                                            .crossfade(true)
                                            .build(),
                                        loading = {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(24.dp)
                                            )
                                        },
                                        contentDescription = mentor.name,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(130.dp)
                                            .clip(CircleShape)
                                            .border(
                                                width = 5.dp,
                                                color = Color.White,
                                                shape = CircleShape
                                            )
                                    )
                                }


                                // ONLINE STATUS INDICATOR
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .offset(x = (-12).dp, y = (-12).dp) // 🔑 moves dot inward
                                        .size(18.dp)
                                        .background(
                                            if (mentor.status.equals("online", ignoreCase = true))
                                                Color(0xFF22C55E) else Color.Gray,
                                            shape = CircleShape
                                        )
                                        .border(
                                            width = 3.dp,
                                            color = Color.White,
                                            shape = CircleShape
                                        )
                                )
                            }

                            Spacer(Modifier.height(20.dp))

                            // Mentor name
                            Text(
                                text = mentor.name,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF111827)
                            )

                            Spacer(Modifier.height(4.dp))

                            // Mentor role
                            Text(
                                text = "${mentor.skill} Developer",
                                fontSize = 16.sp,
                                color = Color(0xFF6B7280)
                            )

                            Spacer(Modifier.height(16.dp))

                            // Rating badge
                            Card(
                                shape = RoundedCornerShape(24.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.star_2),
                                        contentDescription = "Rating",
                                        tint = Color.Unspecified,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        text = "%.1f".format(mentor.rating),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF111827)
                                    )
                                    Text(
                                        text = " (${mentor.ratingCount} reviews)",
                                        fontSize = 16.sp,
                                        color = Color(0xFF6B7280)
                                    )
                                }
                            }

                            Spacer(Modifier.height(32.dp))
                        }
                    }

                    /* -------- ABOUT MENTOR SECTION -------- */
                    item {
                        Text(
                            text = "About Mentor",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF111827),
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        Text(
                            text = mentor.bio,
                            fontSize = 15.sp,
                            color = Color(0xFF4B5563),
                            lineHeight = 24.sp
                        )

                        Spacer(Modifier.height(32.dp))
                    }

                    /* -------- SKILLS & EXPERTISE SECTION -------- */
                    item {
                        Text(
                            text = "Skills & Expertise",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF111827),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(mentor.expertiseList) { skill ->
                                Surface(
                                    color = Color(0xFF544DCA),
                                    shape = RoundedCornerShape(20.dp)
                                ) {
                                    Text(
                                        text = skill,
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(
                                            horizontal = 16.dp,
                                            vertical = 10.dp
                                        )
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(32.dp))
                    }

                    /* -------- COURSES BY THIS MENTOR -------- */
                    item {
                        Text(
                            text = "Courses by This Mentor",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF111827),
                            modifier = Modifier.padding(bottom = 24.dp)
                        )
                    }

                    //* -------- ACTIVE COURSES (HORIZONTAL) -------- */
                    if (activeCourses.isNotEmpty()) {
                        item {
                            Text(
                                text = "Active Courses",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF111827),
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                        }

                        item {
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                contentPadding = PaddingValues(end = 24.dp)
                            ) {
                                items(activeCourses) { course ->
                                    Box(
                                        modifier = Modifier.width(300.dp)
                                    ) {
                                        CourseCard(
                                            course = course,
                                            isActive = true,
                                            onClick = {navController.navigate("courseDetail/${course.id}") }
                                        )
                                    }
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(32.dp))
                        }
                    }


                    /* -------- INACTIVE COURSES (HORIZONTAL) -------- */
                    if (inactiveCourses.isNotEmpty()) {
                        item {
                            Text(
                                text = "Inactive Courses",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF111827),
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                        }

                        item {
                            LazyRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(inactiveCourses) { course ->
                                        CourseCard(
                                            course = course,
                                            isActive = false,
                                            onClick = { navController.navigate("courseDetail/${course.id}") }
                                        )
                                }
                            }


                        }

                        item {
                            Spacer(modifier = Modifier.height(32.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CourseCard(
    course: CourseData,
    isActive: Boolean,
    onClick: () -> Unit = {}
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
            // Course image
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

                // Status badge
                Surface(
                    color = if (isActive) Color(0xFF22C55E) else Color(0xFF9CA3AF),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(top = 12.dp, end = 12.dp)
                        .align(Alignment.TopEnd)
                ) {
                    Text(
                        text = if (isActive) "ACTIVE" else "INACTIVE",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 5.dp
                        )
                    )
                }

                // Course code overlay
                if (isActive) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(start = 16.dp, top = 16.dp)
                    ) {
                        Text(
                            text = "W₄ E₁ B₃\nD₂ E₁ S₁ I₁ G₂ N₁",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 14.sp
                        )
                    }
                }
            }

            // Course content
            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                // Course title
                Text(
                    text = course.course_name ?: "Untitled Course",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isActive) Color(0xFF111827) else Color(0xFF6B7280),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Rating row
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
                        text = "%.1f".format(course.rating ?: 0.0),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isActive) Color(0xFF111827) else Color(0xFF9CA3B8)
                    )
                    Text(
                        text = " (${course.rating_count ?: 0})",
                        fontSize = 14.sp,
                        color = if (isActive) Color(0xFF6B7280) else Color(0xFFCBD5E1)
                    )
                }
            }
        }
    }
}

@Composable
fun MentorActionBar(
    onChatClick: () -> Unit,
    onCallClick: () -> Unit
) {
    Surface(
        tonalElevation = 6.dp,
        shadowElevation = 8.dp,
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Button(
                onClick = onChatClick,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF544DCA)
                )
            ) {
                Icon(
                    Icons.Default.Chat,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "Chat",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            OutlinedButton(
                onClick = onCallClick,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.5.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF544DCA)
                )
            ) {
                Icon(
                    Icons.Default.Call,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "Call",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
