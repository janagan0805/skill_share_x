package com.example.skillsharex.ui.community

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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

/* ---------------- THEME COLORS ---------------- */

private val LavenderBg = Color(0xFFE8E6FF)
private val HeaderPurple = Color(0xFF544DCA)
private val PrimaryBlue = Color(0xFF1022FF)

/* ---------------- CREATE POST SCREEN ---------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
    navController: NavController,
    viewModel: CommunityViewModel
) {

    var selectedType by remember { mutableStateOf("Question") }
    var selectedSkill by remember { mutableStateOf("Android") }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val postTypes = listOf("Question", "Tip", "Achievement")
    val skills = listOf("Android", "UI/UX", "Java", "Web", "Design", "Communication")

    Scaffold(
        containerColor = LavenderBg
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            /* ---------------- HEADER ---------------- */

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(HeaderPurple)
                    .clip(RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp))
                    .padding(top = 50.dp, bottom = 20.dp, start = 16.dp, end = 16.dp)
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier
                            .size(28.dp)
                            .clickable { navController.popBackStack() }
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "Create Post",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            /* ---------------- FORM ---------------- */

            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {

                Text("Post Type", fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(6.dp))

                DropdownField(selectedType, postTypes) { selectedType = it }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Skill Category", fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(6.dp))

                DropdownField(selectedSkill, skills) { selectedSkill = it }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Title (optional)", fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("Enter title") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Description", fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = { Text("Write your post here...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(modifier = Modifier.height(30.dp))

                /* ---- POST BUTTON (UPDATED) ---- */
                Button(
                    onClick = {
                        viewModel.createPost(description)
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) {
                    Text(
                        text = "Post",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
/* ---------------- DROPDOWN FIELD ---------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownField(
    selectedValue: String,
    options: List<String>,
    onSelected: (String) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {

        OutlinedTextField(
            value = selectedValue,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            shape = RoundedCornerShape(14.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}
