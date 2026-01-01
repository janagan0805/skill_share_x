package com.example.skillsharex.ui.community

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.skillsharex.navigation.Screen

/* ---------------- THEME COLORS ---------------- */

private val LavenderBg = Color(0xFFE8E6FF)
private val HeaderPurple = Color(0xFF544DCA)
private val PrimaryBlue = Color(0xFF1022FF)

/* ---------------- COMMUNITY SCREEN ---------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityScreen(
    navController: NavController,
    viewModel: CommunityViewModel
) {

    LaunchedEffect(Unit) {
        viewModel.loadPosts()
    }

    Scaffold(
        containerColor = LavenderBg,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("createPost") },
                containerColor = PrimaryBlue
            ) {
                Icon(Icons.Default.Add, contentDescription = "Create Post", tint = Color.White)
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            /* ---------- HEADER ---------- */

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(HeaderPurple)
                    .clip(RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp))
                    .padding(top = 50.dp, bottom = 20.dp, start = 16.dp, end = 16.dp)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier
                            .size(28.dp)
                            .clickable {
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Home.route) { inclusive = true }
                                }
                            }
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "Community",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filter",
                        tint = Color.White,
                        modifier = Modifier
                            .size(26.dp)
                            .clickable {
                                navController.navigate(Screen.SkillFilter.route)
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            /* ---------- COMMUNITY FEED ---------- */

            LazyColumn(
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 100.dp)
            ) {
                items(viewModel.posts) { post ->
                    CommunityPostCard(
                        post = post,
                        onPostClick = {
                            navController.navigate(
                                Screen.PostDetail.route.replace("{title}", post.content)
                            )
                        },
                        onSkillClick = {
                            navController.navigate(Screen.SkillFilter.route)
                        },
                        onLikeClick = {
                            viewModel.toggleLike(post.post_id)
                        }
                    )
                }
            }
        }
    }
}

/* ---------------- POST CARD ---------------- */

@Composable
fun CommunityPostCard(
    post: Post,
    onPostClick: () -> Unit,
    onSkillClick: () -> Unit,
    onLikeClick: () -> Unit
) {

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onPostClick() }
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = PrimaryBlue,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE0E0FF))
                        .padding(10.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text("User", fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.weight(1f))

                AssistChip(
                    onClick = onSkillClick,
                    label = { Text("Skill", fontSize = 11.sp) }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(post.content, fontSize = 14.sp)

            Spacer(modifier = Modifier.height(14.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                Icon(
                    imageVector = if (post.is_liked)
                        Icons.Default.Favorite
                    else Icons.Default.FavoriteBorder,
                    contentDescription = "Like",
                    tint = PrimaryBlue,
                    modifier = Modifier.clickable { onLikeClick() }
                )

                Icon(
                    imageVector = Icons.Default.ChatBubbleOutline,
                    contentDescription = "Comment",
                    tint = PrimaryBlue
                )
            }
        }
    }
}
