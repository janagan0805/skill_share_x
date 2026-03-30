package com.example.skillsharex.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.skillsharex.model.Course
import com.example.skillsharex.network.AuthApiClient
import com.example.skillsharex.utils.SessionManager
import com.example.skillsharex.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.skillsharex.navigation.Routes


@Composable
fun ProfileScreen(
    navController: NavController,
    rootNavController: NavController,
    viewModel: ProfileViewModel = viewModel()
) {

    val context = LocalContext.current
    val session = SessionManager(context)
    val scope = rememberCoroutineScope()


    val tabs = listOf("Profile", "Sessions", "Reviews")
    var selectedTab by remember { mutableIntStateOf(0) }

    var myCourses by remember { mutableStateOf<List<Course>>(emptyList()) }

    LaunchedEffect(Unit) {
        val userId = session.getUserId() ?: return@LaunchedEffect
        try {
            val response = AuthApiClient.api.getUserCourses(userId)
            if (response.isSuccessful && response.body()?.status == true) {
                myCourses = response.body()!!.data
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /* 🔑 INIT VIEWMODEL + FETCH PROFILE */
    LaunchedEffect(Unit) {
        viewModel.initSession(context)
        viewModel.fetchProfile()
    }
//    val refreshTrigger =
//        navController.currentBackStackEntry
//            ?.savedStateHandle
//            ?.getLiveData<Boolean>("profile_updated")
//            ?.observeAsState()
//
//    LaunchedEffect(refreshTrigger?.value) {
//        if (refreshTrigger?.value == true) {
//            viewModel.fetchProfile()
//            navController.currentBackStackEntry
//                ?.savedStateHandle
//                ?.remove<Boolean>("profile_updated")
//        }
//    }

    val savedStateHandle =
        navController.currentBackStackEntry?.savedStateHandle

    LaunchedEffect(savedStateHandle) {
        savedStateHandle
            ?.getStateFlow("profile_updated", false)
            ?.collect { updated ->
                if (updated) {
                    viewModel.fetchProfile()
                    // 🔑 Reset flag so it doesn’t loop
                    savedStateHandle["profile_updated"] = false
                }
            }
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Spacer(Modifier.height(20.dp))

        ProfileHeader(
            viewModel = viewModel,
            userName = session.getUserName() ?: "User",
            onEditClick = {
                navController.navigate(Routes.EDIT_PROFILE)
            }
        )

        SkillSection()
        StatsRow()

        Spacer(Modifier.height(20.dp))

        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, text ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text,
                            fontWeight =
                                if (selectedTab == index)
                                    FontWeight.Bold
                                else FontWeight.Normal
                        )
                    }
                )
            }
        }

        when (selectedTab) {
            0 -> ProfileTabContent(
                courses = myCourses,
                onEditProfile = {
                    navController.navigate(Routes.EDIT_PROFILE)
                },
                onUserCourse = {
                    navController.navigate(Routes.MY_COURSES)
                },
                onOpenSettings = {
                    navController.navigate(Routes.SETTINGS)
                },
                onLogout = {
                    scope.launch {
                        try {
                            val userId = session.getUserId()
                            if (userId != null) {
                                AuthApiClient.api.logout(userId)
                            }
                        } catch (_: Exception) {
                        } finally {
                            session.logout()

                            rootNavController.navigate(Routes.AUTH_GRAPH) {
                                popUpTo(Routes.MAIN_GRAPH) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    }
                },
                onSubscription = {
                    scope.launch {
                        navController.navigate("subscription")
                    }
                }
            )

            1 -> SessionsTabContent()
            2 -> ReviewsTabContent()
        }
    }
}

/* ---------------- PROFILE HEADER ---------------- */

@Composable
fun ProfileHeader(
    viewModel: ProfileViewModel,
    userName: String,
    onEditClick: () -> Unit
) {
    val imagePath = viewModel.profileImagePath.value
    val reloadKey = viewModel.imageReloadKey.value

    val imageUrl = remember(imagePath, reloadKey) {
        imagePath?.let {
            AuthApiClient.IMAGE_BASE_URL + it
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            if (!imageUrl.isNullOrEmpty()) {

                val context = LocalContext.current
                val imagePath = viewModel.profileImagePath.value
                val reloadKey = viewModel.imageReloadKey.value

                val imageRequest = remember(imagePath, reloadKey) {
                    ImageRequest.Builder(context)
                        .data(
                            imagePath?.let {
                                AuthApiClient.IMAGE_BASE_URL + it
                            }
                        )
                        // 🔑 THIS IS THE KEY FIX
                        .memoryCacheKey("$imagePath-$reloadKey")
                        .diskCacheKey("$imagePath-$reloadKey")
                        .crossfade(true)
                        .build()
                }

                AsyncImage(
                    model = imageRequest,
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentScale = ContentScale.Crop
                )


            } else {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .padding(20.dp)
                )
            }

            Icon(
                Icons.Default.Edit,
                contentDescription = "Edit",
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1022FF))
                    .clickable { onEditClick() }
                    .padding(6.dp)
            )
        }

        Spacer(Modifier.height(10.dp))
        Text(userName, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text(viewModel.role.value, fontSize = 14.sp, color = Color.DarkGray)
    }
}



/* ---------------- PROFILE TAB ---------------- */
@Composable
fun ProfileTabContent(
    onEditProfile: () -> Unit,
    onUserCourse: () -> Unit,
    onOpenSettings: () -> Unit,
    onLogout: () -> Unit,
    onSubscription: () -> Unit,
    courses: List<Course>
) {
    Column(Modifier.padding(16.dp)) {

        ProfileOption("Edit Profile", onEditProfile)
        ProfileOption("My Course", onUserCourse) // 👈 NEW
        ProfileOption("Help & Support")
        ProfileOption("Settings", onOpenSettings)

        if (courses.isNotEmpty()) {
            Spacer(Modifier.height(16.dp))
            Text("Courses I Teach", fontWeight = FontWeight.Bold)

            LazyColumn {
                items(courses) { course ->
                    CourseRow(course)
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("Logout")
        }

        Button(
            onClick = onSubscription,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)) // Gold color
        ) {
            Icon(Icons.Default.Star, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Subscribe Now", color = Color.Black)
        }
    }
}


/* ---------------- OTHER UI (UNCHANGED) ---------------- */

@Composable
fun CourseRow(course: Course) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(course.title, fontWeight = FontWeight.Bold)
            Text(course.description, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun SkillSection() {
    val skills = listOf("UI/UX", "Java", "Figma", "Photoshop")
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        skills.forEach {
            AssistChip(
                onClick = {},
                label = { Text(it, fontSize = 11.sp) },
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}

@Composable
fun StatsRow() {
    Spacer(Modifier.height(14.dp))
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatItem("⭐ 4.8", "Ratings")
        StatItem("2.5k+", "Learners")
        StatItem("12", "Sessions")
    }
}

@Composable
fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontWeight = FontWeight.Bold)
        Text(label, fontSize = 12.sp, color = Color.DarkGray)
    }
}

@Composable
fun SessionsTabContent() {
    val sessions = listOf(
        "Android Live Session",
        "Figma Workshop",
        "Java Q&A"
    )

    LazyColumn(Modifier.padding(16.dp)) {
        items(sessions) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(it, fontWeight = FontWeight.Bold)
                    Text("Completed Session", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun ReviewsTabContent() {
    val reviews = listOf(
        "Great mentor!",
        "Very helpful",
        "Clear explanations"
    )

    LazyColumn(Modifier.padding(16.dp)) {
        items(reviews) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(it, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun ProfileOption(title: String, onClick: () -> Unit = {}) {
    Text(
        title,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp)
    )
    Divider()
}
