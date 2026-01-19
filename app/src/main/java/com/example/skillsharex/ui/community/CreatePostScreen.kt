package com.example.skillsharex.ui.community
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.skillsharex.network.AuthApiClient
import com.example.skillsharex.utils.SessionManager
import com.example.skillsharex.viewmodel.community.CreatePostEvent
import com.example.skillsharex.viewmodel.community.CreatePostViewModel
import kotlinx.coroutines.flow.collectLatest
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
    navController: NavController,
    viewModel: CreatePostViewModel = viewModel()
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val userId = sessionManager.getUserId()
    val uiState = viewModel.uiState
    val snackbarHostState = remember { SnackbarHostState() }
    // Image Picker Launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        viewModel.onImageSelected(uri)
    }
    LaunchedEffect(userId) { if (userId > 0) viewModel.loadUser(userId) }
    LaunchedEffect(viewModel) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is CreatePostEvent.PostSuccess -> {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("community_refresh", true)
                    navController.popBackStack()
                }

                is CreatePostEvent.PostError -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Create Post") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    TextButton(
                        enabled = viewModel.isPostButtonEnabled,
                        onClick = { viewModel.submitPost(context) }
                    ) {
                        if (uiState.isPosting) CircularProgressIndicator(modifier = Modifier.size(20.dp))
                        else Text("Post", fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    ) { padding ->
        if (uiState.user == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                UserHeader(uiState.user.name, uiState.user.role, uiState.user.avatar_url ?: "")
                TopicSelector(uiState.selectedTopic, viewModel::onTopicSelect)

                // Title & Description
                OutlinedTextField(
                    value = uiState.title,
                    onValueChange = viewModel::onTitleChange,
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = uiState.description,
                    onValueChange = viewModel::onDescriptionChange,
                    label = { Text("Content") },
                    modifier = Modifier.fillMaxWidth().height(150.dp)
                )
                // Image Preview
                uiState.selectedImageUri?.let { uri ->
                    Box {
                        Image(
                            painter = rememberAsyncImagePainter(uri),
                            contentDescription = "Selected Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                        IconButton(
                            onClick = { viewModel.onImageSelected(null) },
                            modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)
                                .background(Color.White.copy(alpha=0.7f), CircleShape)
                        ) {
                            Icon(Icons.Default.Close, "Remove")
                        }
                    }
                }
                // Add Image Button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { imagePickerLauncher.launch("image/*") }
                        .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.AddAPhoto, null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(8.dp))
                    Text("Add Photo", color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}
@Composable
fun UserHeader(userName: String, userRole: String, avatarUrl: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(
            model = AuthApiClient.IMAGE_BASE_URL + avatarUrl,
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
