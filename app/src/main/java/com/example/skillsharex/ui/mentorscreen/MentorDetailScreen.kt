package com.example.skillsharex.ui.mentor

import android.widget.Toast
import androidx.compose.foundation.*
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.skillsharex.data.sampleMentorDetails

/* ---------------- THEME ---------------- */

private val Lavender = Color(0xFFE8E6FF)
private val HeaderPurple = Color(0xFF544DCA)
private val Gradient = Brush.horizontalGradient(
    listOf(Color(0xFF6C47FF), Color(0xFF4BC9FF))
)

/* ---------------- MENTOR DETAIL SCREEN ---------------- */

@Composable
fun MentorDetailScreen(
    navController: NavController
) {

    val mentorId = navController
        .currentBackStackEntry
        ?.arguments
        ?.getString("mentorId")

    val mentor = sampleMentorDetails.firstOrNull { it.mentorId == mentorId }
    val context = LocalContext.current

    if (mentor == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Mentor Not Found", color = Color.Red)
        }
        return
    }

    Scaffold(
        containerColor = Lavender,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(HeaderPurple)
                    .padding(top = 46.dp, bottom = 20.dp, start = 16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {

                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(Modifier.width(10.dp))

                    Text(
                        mentor.name,
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {

            /* -------- PROFILE -------- */
            item {
                Spacer(Modifier.height(22.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(mentor.profileImageRes),
                        contentDescription = null,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                    )

                    Spacer(Modifier.height(10.dp))

                    Text(mentor.name, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                    Text(mentor.skill, color = Color.Gray)
                    Text("⭐ ${mentor.rating}", color = Color(0xFFFFC107))

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

            /* -------- REVIEWS -------- */
            item { SectionTitle("Learner Reviews") }

            items(mentor.reviews) { review ->
                ReviewCard(
                    review.reviewerName,
                    review.rating,
                    review.comment
                )
                Spacer(Modifier.height(10.dp))
            }

            /* -------- ACTION BUTTONS -------- */
            item {

                Spacer(Modifier.height(20.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    SolidButton(
                        text = "Request Mentorship",
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        Toast.makeText(context, "Request Sent!", Toast.LENGTH_SHORT).show()
                    }

                    Spacer(Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        SolidButton("Chat", Modifier.weight(1f)) {
                            navController.navigate("chat/$mentorId")
                        }

                        Spacer(Modifier.width(12.dp))

                        OutlineButton("Book Call", Modifier.weight(1f)) {
                            // Future call screen
                        }
                    }

                    Spacer(Modifier.height(26.dp))
                }
                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = {
                        navController.navigate("requests")
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.9f),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = "View Mentorship Requests",
                        fontWeight = FontWeight.Bold
                    )
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

@Composable
fun ReviewCard(name: String, rating: Double, comment: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(14.dp))
            .padding(12.dp)
    ) {
        Text("$name • ⭐ $rating", fontWeight = FontWeight.Bold)
        Text(comment, color = Color.DarkGray)
    }
}

@Composable
fun SolidButton(text: String, modifier: Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .background(Gradient, RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = Color.White, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun OutlineButton(text: String, modifier: Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .border(1.dp, Gradient, RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, fontWeight = FontWeight.Bold)
    }
}
