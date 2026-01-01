package com.example.skillsharex.ui.course

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.skillsharex.model.CourseDetail
import com.example.skillsharex.network.AuthApiClient

@OptIn(ExperimentalMaterial3Api::class) // <-- Add this line
@Composable
fun CourseDetailScreen(
    navController: NavController,
    courseId: Int
) {
    var courseDetail by remember { mutableStateOf<CourseDetail?>(null) }

    LaunchedEffect(courseId) {
        // Fetch course details from the API
        try {
            val response = AuthApiClient.api.getCourseDetail(courseId)
            if (response.isSuccessful) {
                courseDetail = response.body()?.data
            }
        } catch (e: Exception) {
            // Handle error, e.g., show a toast or a snackbar
        }
    }

    Scaffold(
        topBar = {
            TopAppBar( // This is the component that requires the OptIn
                title = { Text(courseDetail?.title ?: "Course Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        courseDetail?.let { course ->
            LazyColumn(modifier = Modifier.padding(paddingValues)) {
                // Course Image
                item {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = course.coverImage?.let { img -> AuthApiClient.IMAGE_BASE_URL + img }
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                // Course Info
                item {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(course.title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Mentor: ${course.mentorName}", fontSize = 16.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = "Rating", tint = Color(0xFFFFC107))
                            Text("%.1f".format(course.rating), fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(16.dp))
                            Text("${course.totalHours} hours", fontSize = 16.sp)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(course.description, fontSize = 16.sp)
                    }
                }

                // Lessons Section
                course.lessons?.let { lessons ->
                    item {
                        Text(
                            text = "Lessons",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
                        )
                    }
                    items(lessons) { lesson ->
                        Card(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(lesson, fontSize = 16.sp)
                            }
                        }
                    }
                }
            }

        }
    }
}
