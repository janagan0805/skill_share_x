package com.example.skillsharex.ui.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Book
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.skillsharex.R
import com.example.skillsharex.navigation.safeNavigate
import com.example.skillsharex.network.AuthApiClient
import com.example.skillsharex.utils.SessionManager



/* ---------------- MENTOR MODEL ---------------- */

data class Mentor(
    val id: String,
    val name: String,
    val skill: String,
    val image: Int,
    val isOnline: Boolean,
    val rating: Double
)

/* ---------------- SAMPLE DATA ---------------- */

//private val mentors = listOf(
//    Mentor("1", "Saranraj", "UI/UX Design", R.drawable.ic_ui_ux_design, true, 4.8),
//    Mentor("2", "Karthick", "Android Development", R.drawable.android, true, 4.6),
//    Mentor("3", "Pranav", "Java Programming", R.drawable.ic_java, false, 4.5),
//    Mentor("4", "Gowtham", "Graphic Design", R.drawable.ic_graphics, true, 4.7),
//    Mentor("5", "Dhanush", "Photoshop", R.drawable.ic_photoshop, false, 4.4)
//)
//
//private val onlineMentors = mentors.filter { it.isOnline }

/* ---------------- HOME DASHBOARD ---------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeDashboardScreen(
    navController: NavController
) {

    // 🔹 STEP 5 STATE: Online mentors from backend
    var onlineMentors by remember { mutableStateOf<List<Mentor>>(emptyList()) }
    var topMentors by remember { mutableStateOf<List<Mentor>>(emptyList()) }

    val context = LocalContext.current
    val session = SessionManager(context)
    val userName = session.getUserName()

    val categories = listOf("UI/UX", "Android", "Java", "Photoshop", "Design", "Career")
    val scrollState = rememberScrollState()

    // 🔹 STEP 5 API CALL: Fetch online mentors
    LaunchedEffect(Unit) {
        try {
            val response = AuthApiClient.api.getOnlineMentors()

            Log.d("ONLINE_MENTORS", "HTTP CODE = ${response.code()}")
            Log.d("ONLINE_MENTORS", "RAW BODY = ${response.body()}")

            if (response.isSuccessful) {
                val list = response.body()?.data ?: emptyList()
                Log.d("ONLINE_MENTORS", "COUNT = ${list.size}")

                onlineMentors = list.map { mentor ->
                    Mentor(
                        id = mentor.id.toString(),
                        name = mentor.full_name,
                        skill = "Mentor",
                        image = R.drawable.android,
                        isOnline = true,
                        rating = 4.5
                    )
                }

                topMentors = list.map { mentor ->
                    Mentor(
                        id = mentor.id.toString(),
                        name = mentor.full_name,
                        skill = "Mentor",
                        image = R.drawable.android,
                        isOnline = true,
                        rating = 4.6
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("ONLINE_MENTORS", "ERROR", e)
        }
    }



    Scaffold(
        containerColor = Color(0xFFE8E6FF),
        bottomBar = {
            BottomBar(
                onProfileClick = { navController.navigate("profile") },
                onOpenCourses = { navController.navigate("session_list") },
                onOpenMentors = { navController.navigate("mentors") },
                onOpenCommunity = { navController.safeNavigate("community")
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {

            /* -------- HEADER -------- */

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF544DCA)),
                shape = RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    Text(
                        text = "Welcome $userName 👋",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, RoundedCornerShape(50.dp))
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Icon(Icons.Default.Search, null, tint = Color(0xFF425CFF))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Search skills or mentors", color = Color.Gray)
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = null,
                            tint = Color(0xFF425CFF),
                            modifier = Modifier.clickable {
                                navController.navigate("notifications")
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            /* -------- CATEGORY CHIPS -------- */

            LazyRow(modifier = Modifier.padding(start = 12.dp)) {
                items(categories) { item ->
                    AssistChip(
                        onClick = { },
                        label = { Text(item, fontWeight = FontWeight.SemiBold) },
                        modifier = Modifier.padding(end = 10.dp),
                        shape = RoundedCornerShape(30.dp)
                    )
                }
            }

            /* -------- ACTIVE SESSION -------- */

            ActiveSessionCard(navController)

            /* -------- MENTOR SECTIONS -------- */

            MentorSection(
                title = "Mentors Available Now",
                mentors = onlineMentors,
                onMentorClick = { sessionId ->
                    navController.navigate("sessionDetail/$sessionId")
                }
            )



            MentorSection(
                title = "Top Mentors For You",
                mentors = topMentors,
                onMentorClick = { mentorId ->
                    navController.navigate("mentorDetail/$mentorId")
                }
            )

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

/* ---------------- ACTIVE SESSION CARD ---------------- */

@Composable
fun ActiveSessionCard(
    navController: NavController
) {

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
                    navController.navigate("session_list")
                }
            ) {
                Text("View All Sessions")
            }

        }
        }
    }


/* ---------------- MENTOR SECTION ---------------- */

@Composable
fun MentorSection(
    title: String,
    mentors: List<Mentor>,
    onMentorClick: (String) -> Unit
) {
    Column {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 18.dp, bottom = 10.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(mentors) { mentor ->
                MentorCard(mentor, onMentorClick)
            }
        }
    }
}

/* ---------------- MENTOR CARD ---------------- */

@Composable
fun MentorCard(
    mentor: Mentor,
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

            Image(
                painter = painterResource(mentor.image),
                contentDescription = mentor.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
            )

            Column(modifier = Modifier.padding(10.dp)) {

                Text(
                    mentor.skill,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    "Mentor: ${mentor.name}",
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
                                if (mentor.isOnline) Color.Green else Color.Gray,
                                shape = RoundedCornerShape(50)
                            )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        if (mentor.isOnline) "Online" else "Offline",
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text("⭐ ${mentor.rating}", fontSize = 12.sp)
                }
            }
        }
    }
}

/* ---------------- BOTTOM BAR ---------------- */

@Composable
fun BottomBar(
    onProfileClick: () -> Unit,
    onOpenCourses: () -> Unit,
    onOpenMentors: () -> Unit,
    onOpenCommunity: () -> Unit
) {
    NavigationBar(containerColor = Color.White) {

        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = { Icon(Icons.Default.Home, null, tint = Color(0xFF425CFF)) },
            label = { Text("Home") }
        )

        NavigationBarItem(
            selected = false,
            onClick = onOpenCommunity,
            icon = { Icon(Icons.Default.People, null) },
            label = { Text("Community") }
        )

        NavigationBarItem(
            selected = false,
            onClick = onOpenCourses,
            icon = { Icon(Icons.Outlined.Book, null) },
            label = { Text("Sessions") }
        )

        NavigationBarItem(
            selected = false,
            onClick = onOpenMentors,
            icon = { Icon(Icons.Default.Person, null) },
            label = { Text("Mentors") }
        )

        NavigationBarItem(
            selected = false,
            onClick = onProfileClick,
            icon = { Icon(Icons.Default.Person, null) },
            label = { Text("Profile") }
        )
    }
}
