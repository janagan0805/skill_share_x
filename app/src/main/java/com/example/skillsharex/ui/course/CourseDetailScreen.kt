package com.example.skillsharex.ui.course

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.skillsharex.network.AuthApiClient
import com.example.skillsharex.viewmodel.CourseDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailScreen(
    navController: NavController,
    courseId: String,
    viewModel: CourseDetailViewModel = viewModel()
) {

    LaunchedEffect(courseId) {
        viewModel.loadCourseDetail(courseId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Course Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
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

            viewModel.courseDetail != null -> {
                val course = viewModel.courseDetail!!

                LazyColumn(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                ) {

                    item {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = course.cover_image?.let {
                                    AuthApiClient.IMAGE_BASE_URL + it
                                }
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp),
                            contentScale = ContentScale.Crop
                        )
                    }

                    item {
                        Column(modifier = Modifier.padding(16.dp)) {

                            Text(
                                text = course.course_name,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Mentor: ${course.mentor.name}",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFFFC107)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    "${course.rating} (${course.rating_count})",
                                    fontSize = 14.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = course.description,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
