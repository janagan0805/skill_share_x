package com.example.skillsharex.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.skillsharex.R
import com.example.skillsharex.navigation.Screen

/* ---------------- THEME COLORS ---------------- */
private val LavenderBg = Color(0xFFE8E6FF)
private val HeaderPurple = Color(0xFF544DCA)
private val PrimaryBlue = Color(0xFF1022FF)

@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel
) {

    var newSkill by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LavenderBg)
    ) {

        Column {

            /* ---------------- HEADER ---------------- */
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(HeaderPurple)
                    .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
                    .padding(top = 50.dp, bottom = 20.dp, start = 20.dp, end = 20.dp)
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier
                            .size(28.dp)
                            .clickable { navController.popBackStack() }
                    )

                    Spacer(Modifier.width(12.dp))

                    Text(
                        text = "Edit Profile",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            /* ---------------- PROFILE PHOTO ---------------- */
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(modifier = Modifier.size(120.dp)) {

                    Image(
                        painter = painterResource(id = R.drawable.jana),
                        contentDescription = null,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )

                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit Photo",
                        tint = Color.White,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(PrimaryBlue)
                            .padding(6.dp)
                    )
                }
            }

            Spacer(Modifier.height(30.dp))

            /* ---------------- FORM SECTION ---------------- */
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {

                Text("Full Name", fontWeight = FontWeight.SemiBold, color = PrimaryBlue)
                OutlinedTextField(
                    value = viewModel.name.value,
                    onValueChange = { viewModel.name.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(Modifier.height(16.dp))

                Text("Role / Position", fontWeight = FontWeight.SemiBold, color = PrimaryBlue)
                OutlinedTextField(
                    value = viewModel.role.value,
                    onValueChange = { viewModel.role.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(Modifier.height(16.dp))

                Text("Bio / About", fontWeight = FontWeight.SemiBold, color = PrimaryBlue)
                OutlinedTextField(
                    value = viewModel.bio.value,
                    onValueChange = { viewModel.bio.value = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp),
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(Modifier.height(20.dp))

                Text("Skills", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = PrimaryBlue)

                Spacer(Modifier.height(8.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    viewModel.skills.forEach { skill ->
                        SkillChip(
                            skill = skill,
                            onRemove = { viewModel.skills.remove(skill) }
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {

                    OutlinedTextField(
                        value = newSkill,
                        onValueChange = { newSkill = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Add skill") }
                    )

                    Spacer(Modifier.width(8.dp))

                    Button(
                        onClick = {
                            if (newSkill.isNotBlank()) {
                                viewModel.skills.add(newSkill.trim())
                                newSkill = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(PrimaryBlue)
                    ) {
                        Text("+ Add", color = Color.White)
                    }
                }
            }

            Spacer(Modifier.height(30.dp))

            /* ---------------- SAVE BUTTON ---------------- */
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(55.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(PrimaryBlue)
            ) {
                Text("Save Changes", color = Color.White, fontSize = 18.sp)
            }
        }
    }
}

/* ---------------- SKILL CHIP ---------------- */
@Composable
fun SkillChip(
    skill: String,
    onRemove: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = HeaderPurple.copy(alpha = 0.15f)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(skill, fontSize = 14.sp)
            Spacer(Modifier.width(6.dp))
            Text(
                text = "✕",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onRemove() }
            )
        }
    }
}
