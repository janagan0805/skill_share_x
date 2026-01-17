package com.example.skillsharex.ui.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.skillsharex.viewmodel.CreateCourseViewModel
import io.ktor.websocket.Frame
import coil.compose.AsyncImage


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCourseScreen(
    navController: NavController,
    viewModel: CreateCourseViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val context = androidx.compose.ui.platform.LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.initSession(context)
    }

    if (viewModel.isSuccess.value) {
        LaunchedEffect(Unit) {
            navController.popBackStack()
        }
    }

    val imagePicker =
        androidx.activity.compose.rememberLauncherForActivityResult(
            contract = androidx.activity.result.contract.ActivityResultContracts.GetContent()
        ) { uri ->
            uri?.let { viewModel.setImage(it) }
        }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Course") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->

        androidx.compose.foundation.layout.Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(14.dp)
        ) {

            // 🔹 Image Preview
            viewModel.imageUri.value?.let { uri ->
                AsyncImage(
                    model = uri,
                    contentDescription = "Course Image Preview",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .padding(top = 8.dp),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
            }


            // 🔹 Image Picker
            androidx.compose.material3.OutlinedButton(
                onClick = { imagePicker.launch("image/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (viewModel.imageUri.value == null)
                        "Select Course Image"
                    else
                        "Image Selected"
                )
            }

            // 🔹 Title
            androidx.compose.material3.OutlinedTextField(
                value = viewModel.title.value,
                onValueChange = { viewModel.title.value = it },
                label = { Text("Course Title") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // 🔹 Description
            androidx.compose.material3.OutlinedTextField(
                value = viewModel.description.value,
                onValueChange = { viewModel.description.value = it },
                label = { Text("Course Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            // 🔹 Status Selector (ACTIVE / INACTIVE)
            Text("Course Status")

            Row(verticalAlignment = Alignment.CenterVertically) {

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


            // 🔹 Error
            viewModel.errorMessage.value?.let {
                Text(text = it, color = Color.Red)
            }

            // 🔹 Submit
            androidx.compose.material3.Button(
                onClick = { viewModel.createCourse(context) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !viewModel.isLoading.value
            ) {
                Text(
                    if (viewModel.isLoading.value)
                        "Creating..."
                    else
                        "Create Course"
                )
            }
        }
    }
}
