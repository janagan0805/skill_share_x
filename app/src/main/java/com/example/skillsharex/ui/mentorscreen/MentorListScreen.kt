package com.example.skillsharex.ui.mentorscreen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.skillsharex.model.MentorData
import com.example.skillsharex.network.AuthApiClient
import com.example.skillsharex.utils.RefreshBus
import com.example.skillsharex.utils.RefreshEvent
import com.example.skillsharex.viewmodel.MentorListViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

/* ---------- THEME ---------- */
private val Lavender = Color(0xFFE8E6FF)
private val HeaderPurple = Color(0xFF544DCA)
private val CardBg = Color.White
private val BorderBlue = Color(0xFF6C47FF)

private val GradientBtn = Brush.horizontalGradient(
    listOf(Color(0xFF6C47FF), Color(0xFF4BC9FF))
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MentorListScreen(
    navController: NavController,
    viewModel: MentorListViewModel = viewModel()
) {
    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = viewModel.isLoading
    )

    val listState = rememberLazyListState()

    var searchText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadMentorsList()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Column(modifier = Modifier.fillMaxSize()) {

            /* ---------- HEADER ---------- */
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(HeaderPurple)
                    .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
                    .padding(top = 20.dp, bottom = 20.dp, start = 25.dp, end = 20.dp)
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Text(
                        text = "Mentors List",
                        color = Color.White,
                        fontSize = 22.sp,
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
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            /* ---------- FILTERED LIST ---------- */
            val filteredList = viewModel.mentors.filter {
                it.name.contains(searchText, ignoreCase = true) ||
                        it.skill.any { skill ->
                            skill.contains(searchText, ignoreCase = true)
                        }
            }

            /* ---------- MENTOR LIST ---------- */
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = {
                    viewModel.loadMentorsList(force = true)
                }
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    items(filteredList, key = { it.id }) { mentor ->
                        Box(Modifier.animateItemPlacement()) {
                            MentorCard(
                                mentor = mentor,
                                onClick = {
                                    navController.navigate("mentorDetail/${mentor.id}")
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    item { Spacer(modifier = Modifier.height(100.dp)) }
                }
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

            AsyncImage(
                model = AuthApiClient.IMAGE_BASE_URL + mentor.imageUrl,
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(65.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )


            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    mentor.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                val visibleSkills = mentor.skill.take(1)
                val remainingSkills = mentor.skill.size - visibleSkills.size
                Row(
                    modifier = Modifier.padding(top = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    visibleSkills.forEach { skill ->
                        SkillChip(skill)
                    }

                    if (remainingSkills > 0) {
                        Text(
                            text = "+$remainingSkills",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(start = 6.dp)
                        )
                    }
                }

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
@Composable
fun SkillChip(skill: String) {
    Box(
        modifier = Modifier
            .padding(end = 6.dp)
            .background(
                color = Color(0xFFEDEDED),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = skill,
            fontSize = 12.sp,
            color = Color.DarkGray,
            maxLines = 1
        )
    }
}
