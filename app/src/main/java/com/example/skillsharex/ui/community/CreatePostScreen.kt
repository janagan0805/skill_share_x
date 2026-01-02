
package com.example.skillsharex.ui.community

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
    navController: NavController,
    communityViewModel: CommunityViewModel,
    createPostViewModel: CreatePostViewModel = viewModel()
) {
    val uiState = createPostViewModel.uiState
    val isPostEnabled = createPostViewModel.isPostButtonEnabled

    // Listen for events from the ViewModel
    LaunchedEffect(Unit) {
        createPostViewModel.events.collectLatest { event ->
            when (event) {
                is CreatePostEvent.PostSuccess -> {
                    // Navigate back to the community feed on success
                    navController.popBackStack()
                }
                is CreatePostEvent.PostError -> {
                    // TODO: Show a snackbar or toast with the error message
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Post") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(
                        onClick = { createPostViewModel.submitPost(communityViewModel) },
                        enabled = isPostEnabled && !uiState.isPosting
                    ) {
                        if (uiState.isPosting) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Post", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()) // Make the screen scrollable
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // In a real app, user data would come from a user repository or session manager
            UserHeader(
                userName = "John Appleseed", // Replace with actual user data
                userRole = "Mentor",          // Replace with actual user data
                avatarUrl = "https://i.pravatar.cc/150?u=a042581f4e29026704d" // Replace with actual user data
            )

            TopicSelector(
                selectedTopic = uiState.selectedTopic,
                onTopicSelected = createPostViewModel::onTopicSelect
            )

            PostInputFields(
                title = uiState.title,
                onTitleChange = createPostViewModel::onTitleChange,
                description = uiState.description,
                onDescriptionChange = createPostViewModel::onDescriptionChange
            )

            PostActionBar()

        }
    }
}


@Composable
fun UserHeader(userName: String, userRole: String, avatarUrl: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(
            model = avatarUrl,
            contentDescription = "User Avatar",
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.width(12.dp))
        Column {
            Text(userName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(
                text = userRole,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicSelector(selectedTopic: String, onTopicSelected: (String) -> Unit) {
    val topics = listOf(
        "Android Development", "Web Development", "UI/UX", "Career Guidance", "General Discussion"
    )
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text("Select a Topic*", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(8.dp))
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedTopic,
                onValueChange = {},
                readOnly = true,
                label = { Text("Topic") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                shape = RoundedCornerShape(12.dp)
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                topics.forEach { topic ->
                    DropdownMenuItem(
                        text = { Text(topic) },
                        onClick = {
                            onTopicSelected(topic)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PostInputFields(
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Post Title
        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Post Title*") },
            placeholder = { Text("A clear, concise title for your post") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            supportingText = {
                Text(
                    text = "${title.length}/80",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End
                )
            }
        )

        // Post Description
        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            label = { Text("Description*") },
            placeholder = { Text("Share your thoughts, questions, or tips...") },
            shape = RoundedCornerShape(12.dp),
            supportingText = {
                Text(
                    text = "${description.length}/500",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End
                )
            }
        )
    }
}

@Composable
fun PostActionBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { /* TODO: Implement add image */ }) {
            Icon(Icons.Default.AddAPhoto, contentDescription = "Add Image", tint = MaterialTheme.colorScheme.primary)
        }
        // Add more actions here if needed (e.g., add link, skill tags)
    }
}
