package com.example.skillsharex.ui.mentor

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.skillsharex.model.MentorData
import com.example.skillsharex.network.AuthApiClient
import com.example.skillsharex.viewmodel.MentorListViewModel

/* ---------- THEME ---------- */
private val Lavender = Color(0xFFE8E6FF)
private val HeaderPurple = Color(0xFF544DCA)
private val CardBg = Color.White
private val BorderBlue = Color(0xFF6C47FF)

private val GradientBtn = Brush.horizontalGradient(
    listOf(Color(0xFF6C47FF), Color(0xFF4BC9FF))
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MentorListScreen(
    navController: NavController,
    viewModel: MentorListViewModel = viewModel()
) {

    var searchText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadMentors()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Lavender)
    ) {

        Column(modifier = Modifier.fillMaxSize()) {

            /* ---------- HEADER ---------- */
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(HeaderPurple)
                    .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
                    .padding(top = 50.dp, bottom = 24.dp, start = 18.dp, end = 18.dp)
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier
                            .size(28.dp)
                            .clickable { navController.popBackStack() }
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "Top Mentors",
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            /* ---------- SEARCH BAR ---------- */
            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text("Search mentors…") },
                modifier = Modifier
                    .padding(horizontal = 18.dp)
                    .fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(18.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = BorderBlue,
                    unfocusedIndicatorColor = BorderBlue
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            /* ---------- FILTERED LIST ---------- */
            val filteredList = viewModel.mentors.filter {
                it.name.contains(searchText, ignoreCase = true) ||
                        it.skill.contains(searchText, ignoreCase = true)
            }

            /* ---------- MENTOR LIST ---------- */
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                items(filteredList) { mentor ->
                    MentorCard(
                        mentor = mentor,
                        onClick = {
                            navController.navigate("mentorDetail/${mentor.id}")
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }
    }
}

/* ---------- MENTOR CARD ---------- */

@Composable
fun MentorCard(
    mentor: MentorData,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(CardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = rememberAsyncImagePainter(
                    model = mentor.imageUrl?.let {
                        AuthApiClient.IMAGE_BASE_URL + it
                    }
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(65.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    mentor.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    mentor.skill,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Text(
                    "⭐ ${mentor.rating}",
                    color = BorderBlue,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Box(
                modifier = Modifier
                    .background(GradientBtn, RoundedCornerShape(14.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Text("View", color = Color.White)
            }
        }
    }
}
