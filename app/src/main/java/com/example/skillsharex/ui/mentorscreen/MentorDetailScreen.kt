package com.example.skillsharex.ui.mentor

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.skillsharex.network.AuthApiClient
import com.example.skillsharex.viewmodel.MentorDetailViewModel

/* ---------------- THEME ---------------- */

private val Lavender = Color(0xFFE8E6FF)
private val HeaderPurple = Color(0xFF544DCA)
private val Gradient = Brush.horizontalGradient(
    listOf(Color(0xFF6C47FF), Color(0xFF4BC9FF))
)

/* ---------------- MENTOR DETAIL SCREEN ---------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MentorDetailScreen(
    navController: NavController,
    mentorId: Int,
    viewModel: MentorDetailViewModel = viewModel()
) {

    LaunchedEffect(mentorId) {
        viewModel.loadMentorDetail(mentorId)
    }

    Scaffold(
        containerColor = Lavender,
        topBar = {
            TopAppBar(
                title = { Text("Mentor Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = HeaderPurple,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
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

                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp)
                ) {

                    /* -------- PROFILE -------- */
                    item {
                        Spacer(Modifier.height(20.dp))

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = mentor.image?.let {
                                        AuthApiClient.IMAGE_BASE_URL + it
                                    }
                                ),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape)
                            )

                            Spacer(Modifier.height(10.dp))

                            Text(mentor.name, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                            Text(mentor.skill, color = Color.Gray)
                            Text("⭐ ${mentor.rating} (${mentor.ratingCount})", color = Color(0xFFFFC107))

                            Spacer(Modifier.height(16.dp))
                        }
                    }

                    /* -------- ABOUT -------- */
                    item {
                        SectionTitle("About Mentor")
                        Text(mentor.bio, color = Color.DarkGray)
                        Spacer(Modifier.height(14.dp))
                    }

                    /* -------- SKILLS -------- */
                    item {
                        SectionTitle("Skills & Expertise")

                        LazyRow {
                            items(mentor.expertiseList) { skill ->
                                Box(
                                    modifier = Modifier
                                        .padding(end = 10.dp)
                                        .background(Gradient, RoundedCornerShape(20.dp))
                                        .padding(horizontal = 14.dp, vertical = 8.dp)
                                ) {
                                    Text(skill, color = Color.White)
                                }
                            }
                        }

                        Spacer(Modifier.height(20.dp))
                    }
                }
            }
        }
    }
}

/* ---------------- COMPONENTS ---------------- */

@Composable
fun SectionTitle(title: String) {
    Text(
        title,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        modifier = Modifier.padding(vertical = 6.dp)
    )
}
