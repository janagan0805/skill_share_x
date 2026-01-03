package com.example.skillsharex.ui.community

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.skillsharex.model.community.CommunityPost
import com.example.skillsharex.viewmodel.CommunityViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityScreen(
    navController: NavController,
    viewModel: CommunityViewModel = viewModel()
) {

    LaunchedEffect(Unit) {
        viewModel.loadCommunityFeed()
    }
    LaunchedEffect(navController.currentBackStackEntry) {
        viewModel.loadCommunityFeed()
    }


    Scaffold(
        topBar = {
            CommunityHeader(navController)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("create_post") },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Create Post", tint = Color.White)
            }
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            when {
                viewModel.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                viewModel.errorMessage != null -> {
                    ErrorState {
                        viewModel.loadCommunityFeed()
                    }
                }

                viewModel.feedPosts.isEmpty() -> {
                    EmptyState()
                }

                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(viewModel.feedPosts, key = { it.postId }) { post ->
                            CommunityPostCard(post)
                        }
                    }
                }
            }
        }
    }
}

/* ---------------- HEADER ---------------- */

@Composable
fun CommunityHeader(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(
                    listOf(Color(0xFF544DCA), Color(0xFF7A60D8))
                )
            )
            .padding(top = 40.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier
                    .size(28.dp)
                    .clickable { navController.popBackStack() }
            )
            Spacer(Modifier.width(16.dp))
            Text(
                text = "Community",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/* ---------------- POST CARD ---------------- */

@Composable
fun CommunityPostCard(post: CommunityPost) {

    Card(
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(3.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            /* USER INFO */
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = post.userAvatarUrl,
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(Modifier.width(12.dp))

                Column {
                    Text(post.userName, fontWeight = FontWeight.Bold)
                    Text(post.postType.uppercase(), fontSize = 12.sp, color = Color.Gray)
                }
            }

            Spacer(Modifier.height(12.dp))

            /* CONTENT */
            Text(post.postTitle, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))
            Text(post.postContentSnippet, fontSize = 14.sp)

            Spacer(Modifier.height(14.dp))

            /* ACTIONS */
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                ActionItem(Icons.Default.Favorite, post.likeCount.toString())
                ActionItem(Icons.Default.ChatBubbleOutline, post.commentCount.toString())
                ActionItem(Icons.Default.Share, "Share")
            }
        }
    }
}

/* ---------------- ACTION ITEM ---------------- */

@Composable
fun ActionItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(6.dp))
        Text(text, fontSize = 12.sp, color = Color.Gray)
    }
}

/* ---------------- STATES ---------------- */

@Composable
fun EmptyState() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("No community posts yet.")
    }
}

@Composable
fun ErrorState(onRetry: () -> Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Failed to load community")
            Spacer(Modifier.height(8.dp))
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}
