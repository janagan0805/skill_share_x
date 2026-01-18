package com.example.skillsharex.ui.community
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.skillsharex.network.AuthApiClient
import com.example.skillsharex.viewmodel.community.CommunityViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailScreen(
    navController: NavController,
    postId: String,
    viewModel: CommunityViewModel = viewModel()
) {
    val context = LocalContext.current
    var commentText by remember { mutableStateOf("") }
    LaunchedEffect(postId) {
        viewModel.loadPostDetails(context, postId.toInt())
    }
    val post = viewModel.currentPost
    val comments = viewModel.currentComments
    Scaffold(
        bottomBar = {
            // Comment Input
            Row(
                Modifier
                    .background(Color.White)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = commentText,
                    onValueChange = { commentText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Add a comment...") },
                    shape = RoundedCornerShape(20.dp)
                )
                IconButton(onClick = {
                    if (commentText.isNotBlank()) {
                        viewModel.addComment(context, postId.toInt(), commentText)
                        commentText = ""
                    }
                }) {
                    Icon(Icons.Default.Send, null, tint = MaterialTheme.colorScheme.primary)
                }
            }
        }
    ) { padding ->
        if (viewModel.isDetailsLoading || post == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        } else {
            LazyColumn(modifier = Modifier.padding(padding).padding(16.dp)) {
                // Post Header
                item {

                    // User Info
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = AuthApiClient.IMAGE_BASE_URL + post.userAvatarUrl,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp).clip(CircleShape)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(post.userName, fontWeight = FontWeight.SemiBold)
                    }
                    Spacer(Modifier.height(16.dp))


                    Text(post.postTitle, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Spacer(Modifier.height(8.dp))
                    // Content
                    Text(post.postContent ?: "", fontSize = 16.sp) // Or full content if your API returns it

                    // Image
                    if (!post.postImage.isNullOrEmpty()) {
                        Spacer(Modifier.height(12.dp))
                        AsyncImage(
                            model = AuthApiClient.IMAGE_BASE_URL + post.postImage,
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.FillWidth
                        )
                    }

                    Divider(Modifier.padding(vertical = 16.dp))
                    Text("Comments (${comments.size})", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                }
                // Comments List
                items(comments) { comment ->
                    Row(Modifier.padding(vertical = 8.dp)) {
                        AsyncImage(
                            model = AuthApiClient.IMAGE_BASE_URL + comment.userAvatarUrl,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp).clip(CircleShape)
                        )
                        Spacer(Modifier.width(8.dp))
                        Column {
                            Text(comment.userName, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(comment.content, fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }
}