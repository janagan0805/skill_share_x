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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.skillsharex.viewmodel.CreatePostEvent
import com.example.skillsharex.viewmodel.CreatePostEventViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
    navController: NavController,
    viewModel: CreatePostEventViewModel = viewModel()
) {

    val state = viewModel.state

    // Navigate back on success
    if (state.isSuccess) {
        navController.popBackStack()
        LaunchedEffect(state.isSuccess) {
            if (state.isSuccess) {
                navController.popBackStack()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Post") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            viewModel.onEvent(CreatePostEvent.SubmitPost)
                        },
                        enabled = !state.isSubmitting
                                && state.title.isNotBlank()
                                && state.description.isNotBlank()
                    ) {
                        if (state.isSubmitting) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            /* ---------- USER HEADER (TEMP) ---------- */
            UserHeader(
                userName = "John Appleseed",
                userRole = "Mentor",
                avatarUrl = "https://i.pravatar.cc/150"
            )

            /* ---------- POST TYPE ---------- */
            TopicSelector(
                selectedTopic = state.postType,
                onTopicSelected = {
                    viewModel.onEvent(CreatePostEvent.PostTypeChanged(it))
                }
            )

            /* ---------- INPUT FIELDS ---------- */
            PostInputFields(
                title = state.title,
                onTitleChange = {
                    viewModel.onEvent(CreatePostEvent.TitleChanged(it))
                },
                description = state.description,
                onDescriptionChange = {
                    viewModel.onEvent(CreatePostEvent.DescriptionChanged(it))
                }
            )

            /* ---------- ACTION BAR ---------- */
            PostActionBar()

            /* ---------- ERROR ---------- */
            state.errorMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

/* ---------------- USER HEADER ---------------- */

@Composable
fun UserHeader(
    userName: String,
    userRole: String,
    avatarUrl: String
) {
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

/* ---------------- TOPIC SELECTOR ---------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicSelector(
    selectedTopic: String,
    onTopicSelected: (String) -> Unit
) {
    val topics = listOf("discussion", "achievement", "poll")
    var expanded = false

    Column {
        Text("Post Type*", fontWeight = FontWeight.Medium)
        Spacer(Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedTopic,
                onValueChange = {},
                readOnly = true,
                label = { Text("Type") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                },
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
                        text = { Text(topic.replaceFirstChar { it.uppercase() }) },
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

/* ---------------- INPUT FIELDS ---------------- */

@Composable
fun PostInputFields(
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Post Title*") },
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

        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            label = { Text("Description*") },
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

/* ---------------- ACTION BAR ---------------- */

@Composable
fun PostActionBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                RoundedCornerShape(12.dp)
            )
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { /* Phase-3 */ }) {
            Icon(
                Icons.Default.AddAPhoto,
                contentDescription = "Add Image",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}
