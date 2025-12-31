package com.example.skillsharex.ui.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import com.example.skillsharex.model.Course
import com.example.skillsharex.navigation.Screen
import com.example.skillsharex.network.AuthApiClient
import com.example.skillsharex.utils.FileUtils
import com.example.skillsharex.utils.SessionManager
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

@Composable
fun ProfileScreen(navController: NavController) {

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFC7C3F9))
    ) {

        Spacer(Modifier.height(20.dp))

        ProfileHeader()
        SkillSection()
        StatsRow()

        Spacer(Modifier.height(20.dp))

        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.Transparent
        ) {
            tabs.forEachIndexed { index, text ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text,
                            fontWeight = if (selectedTab == index)
                                FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }

        when (selectedTab) {
            0 -> ProfileTabContent(
                courses = myCourses,
                onOpenSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onLogout = {
                    scope.launch {
                        try {
                            val userId = session.getUserId()

                            if (userId != null) {
                                // 🔥 STEP 6: Tell backend user is offline
                                AuthApiClient.api.logout(userId)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        } finally {
                            // 🔹 Always clear session & navigate
                            session.logout()
                            navController.navigate("login") {
                                popUpTo(0) { inclusive = true }
                            }
                        }
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
fun ProfileHeader() {

    val context = LocalContext.current
    val session = SessionManager(context)
    val scope = rememberCoroutineScope()

    val userName = session.getUserName() ?: "User"

    var profileImageUri by remember {
        mutableStateOf(
            session.getProfileImageUrl()?.let {
                "http://172.25.105.154/skillsharex_backend/$it"
            }
        )
    }

    val imagePicker =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri ?: return@rememberLauncherForActivityResult

            scope.launch {
                val file = FileUtils.getFileFromUri(context, uri)

                val requestBody =
                    file.asRequestBody("image/*".toMediaType())

                val part = MultipartBody.Part.createFormData(
                    "image",
                    file.name,
                    requestBody
                )

                val userId = session.getUserId()?.toString() ?: return@launch

                val response = AuthApiClient.api.uploadProfileImage(
                    image = part,
                    userId = userId
                )


                if (response.isSuccessful) {
                    response.body()?.data?.image_url?.let { url ->
                        session.saveProfileImageUrl(url)
                        profileImageUri =
                            "http://172.25.105.154/skillsharex_backend/$url"
                    }
                }
            }
        }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box {
            if (profileImageUri != null) {
                AsyncImage(
                    model = profileImageUri,
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
                    null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .padding(20.dp)
                )
            }

            Icon(
                Icons.Default.Edit,
                null,
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1022FF))
                    .clickable { imagePicker.launch("image/*") }
                    .padding(6.dp)
            )
        }

        Spacer(Modifier.height(10.dp))

        Text(userName, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text("Mentor • SkillShareX", fontSize = 14.sp, color = Color.DarkGray)
    }
}

/* ---------------- PROFILE TAB ---------------- */

@Composable
fun ProfileTabContent(
    onOpenSettings: () -> Unit,
    onLogout: () -> Unit,
    courses: List<Course>
) {
    Column(Modifier.padding(16.dp)) {

        ProfileOption("Edit Profile")
        ProfileOption("Help & Support")
        ProfileOption("Settings", onOpenSettings)

        /* 🔥 COURSES SECTION (THIS WAS MISSING) */
        if (courses.isNotEmpty()) {

            Spacer(Modifier.height(16.dp))

            Text(
                "Courses I Teach",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(Modifier.height(8.dp))

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
    }
}
@Composable
fun CourseRow(course: Course) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = "http://10.88.233.111/skillsharex_backend/${course.image_path}",
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(12.dp))

            Column {
                Text(
                    text = course.title,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = course.description,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 2
                )
            }
        }
    }
}



/* ---------------- SKILLS ---------------- */

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

/* ---------------- STATS ---------------- */

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

/* ---------------- SESSIONS ---------------- */

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

/* ---------------- REVIEWS ---------------- */

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
