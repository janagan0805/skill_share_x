package com.example.skillsharex.ui.community

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.skillsharex.model.community.CommunityPost
import com.example.skillsharex.network.AuthApiClient
import com.example.skillsharex.utils.RefreshBus
import com.example.skillsharex.utils.RefreshEvent
import com.example.skillsharex.viewmodel.community.CommunityViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CommunityScreen(
    navController: NavController,
    viewModel: CommunityViewModel = viewModel()
) {
    val context = LocalContext.current
    var shouldScrollToTop by remember { mutableStateOf(false) }
    var previousListSize by remember { mutableIntStateOf(0) }

    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = viewModel.isLoading
    )


    // ✅ LOAD ONCE
    LaunchedEffect(Unit) {
        viewModel.loadCommunityFeed(context)
    }



    LaunchedEffect(viewModel.feedPosts) {
        previousListSize = viewModel.feedPosts.size
    }

    val savedStateHandle =
        navController.currentBackStackEntry?.savedStateHandle

    val listState = rememberLazyListState()

    LaunchedEffect(savedStateHandle) {
        savedStateHandle
            ?.getStateFlow("community_refresh", false)
            ?.collect { shouldRefresh ->
                if (shouldRefresh) {
                    viewModel.loadCommunityFeed(context, force = true)

                    // 🔑 request scroll AFTER data arrives
                    shouldScrollToTop = true

                    savedStateHandle["community_refresh"] = false
                }
            }
    }

    LaunchedEffect(shouldScrollToTop) {
        if (!shouldScrollToTop) return@LaunchedEffect

        snapshotFlow { viewModel.feedPosts.size }
            .collect { newSize ->
                if (newSize > previousListSize && newSize > 0) {

                    // 🔥 Guaranteed: item exists & layout is ready
                    listState.animateScrollToItem(0)

                    previousListSize = newSize
                    shouldScrollToTop = false

                    // stop collecting
                    return@collect
                }
            }
    }

    Scaffold(
        topBar = { CommunityHeader(navController) },
        floatingActionButton = {
            AnimatedVisibility(
                visible = viewModel.feedPosts.isNotEmpty(),
                enter = scaleIn(tween(200)) + fadeIn(),
                exit = scaleOut(tween(120)) + fadeOut()
            ) {
                FloatingActionButton(
                    onClick = { navController.navigate("create_post") }
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                }
            }
        }

    ) { padding ->

        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                viewModel.loadCommunityFeed(context, force = true)
            }
        ) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding)
            ) {

                AnimatedContent(
                    targetState = viewModel.feedPosts.isNotEmpty(),
                    transitionSpec = {
                        fadeIn(tween(220)) togetherWith fadeOut(tween(120))
                    },
                    label = "community-content"
                ) { hasContent ->

                    if (!hasContent) {
                        EmptyState()
                    } else {
                        LazyColumn(
                            state = listState,
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(
                                items = viewModel.feedPosts,
                                key = { it.postId }
                            ) { post ->
                                Box(Modifier.animateItemPlacement()) {
                                    CommunityPostCard(
                                        post = post,
                                        onClick = {
                                            navController.navigate("post_detail/${post.postId}")
                                        },
                                        onLikeClick = {
                                            viewModel.toggleLike(context, post)
                                        }
                                    )
                                }
                            }
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
            .padding(top = 20.dp, bottom = 20.dp, start = 25.dp, end = 20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

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
fun CommunityPostCard(
    post: CommunityPost,
    onClick: () -> Unit,
    onLikeClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        // ... shapes ...

        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(3.dp),
    ) {
        Column(Modifier.padding(16.dp)) {
            // User Info Row ...

            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = AuthApiClient.IMAGE_BASE_URL + post.userAvatarUrl,
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

            Spacer(Modifier.height(16.dp))

            Text(post.postTitle, fontWeight = FontWeight.Bold)
            Text(post.postContent ?: "", maxLines = 3)

            // Image Preview in Feed (Optional)
            if (!post.postImage.isNullOrEmpty()) {

                Spacer(Modifier.height(16.dp))

                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(AuthApiClient.IMAGE_BASE_URL + post.postImage)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )

            }

            Spacer(Modifier.height(16.dp))
            // Actions
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                // Like Button
                Row(Modifier.clickable { onLikeClick() }) {
                    Icon(
                        if (post.isLiked) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (post.isLiked) Color.Red else Color.Gray
                    )
                    Text(" ${post.likeCount}")
                }
                // Comment Icon
                Row {
                    Icon(Icons.Default.ChatBubbleOutline, null)
                    Text(" ${post.commentCount}")
                }
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
