package com.example.skillsharex.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.skillsharex.model.CourseData
import com.example.skillsharex.viewmodel.UserCourseViewModel
import androidx.compose.material3.FloatingActionButton
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.skillsharex.R
import com.example.skillsharex.network.AuthApiClient


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserCourseScreen(
    navController: NavController,
    viewModel: UserCourseViewModel = viewModel()
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.initSession(context)
        viewModel.loadMyCourses()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Courses") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("create_course")
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Course")
            }
        }
    ) { padding ->

        when {
            viewModel.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            viewModel.myCourses.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No courses created yet")
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(16.dp)
                ) {

                    item {
                        CourseHorizontalSection(
                            title = "Active Courses",
                            courses = viewModel.activeCourses,
                            onCourseClick = { course ->
                                navController.navigate("edit_course/${course.id}")
                            }
                        )
                    }

                    item {
                        Spacer(Modifier.height(20.dp))
                    }

                    item {
                        CourseHorizontalSection(
                            title = "Inactive Courses",
                            courses = viewModel.inactiveCourses,
                            onCourseClick = { course ->
                                navController.navigate("edit_course/${course.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun CourseHorizontalSection(
    title: String,
    courses: List<CourseData>,
    onCourseClick: (CourseData) -> Unit
) {
    if (courses.isEmpty()) return

    Text(
        text = title,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 18.dp, bottom = 10.dp)
    )

    LazyRow {
        items(courses) { course ->
            CourseCard(
                course = course,
                onClick = { onCourseClick(course) }
            )
            Spacer(Modifier.width(12.dp))
        }
    }
}

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
            }
        }
    }
}

