package com.example.skillsharex.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.skillsharex.network.AuthApiClient
import com.example.skillsharex.viewmodel.EditCourseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCourseDetailScreen(
    navController: NavController,
    viewModel: EditCourseViewModel = viewModel()
) {
    val context = LocalContext.current
    val courseId =
        navController.currentBackStackEntry
            ?.arguments
            ?.getInt("courseId") ?: return

    LaunchedEffect(Unit) {
        viewModel.initSession(context)
        viewModel.loadCourse(courseId)
    }

    if (viewModel.isUpdated.value) {
        LaunchedEffect(Unit) {
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Course") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->

        when {
            viewModel.isLoading.value -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    // Image Preview (existing image)
                    viewModel.imagePath.value?.let {
                        AsyncImage(
                            model = AuthApiClient.IMAGE_BASE_URL + it,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp),
                            contentScale = ContentScale.Crop
                        )
                    }

                    OutlinedTextField(
                        value = viewModel.title.value,
                        onValueChange = { viewModel.title.value = it },
                        label = { Text("Course Title") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = viewModel.description.value,
                        onValueChange = { viewModel.description.value = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )

                    Text("Status")

                    Row {
                        RadioButton(
                            selected = viewModel.status.value == "active",
                            onClick = { viewModel.status.value = "active" }
                        )
                        Text("Active")

                        Spacer(Modifier.width(16.dp))

                        RadioButton(
                            selected = viewModel.status.value == "inactive",
                            onClick = { viewModel.status.value = "inactive" }
                        )
                        Text("Inactive")
                    }

                    viewModel.errorMessage.value?.let {
                        Text(text = it, color = Color.Red)
                    }

                    Button(
                        onClick = { viewModel.updateCourse(courseId) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Update Course")
                    }
                }
            }
        }
    }
}
