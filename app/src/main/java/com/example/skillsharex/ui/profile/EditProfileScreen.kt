package com.example.skillsharex.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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

/* ---------------- THEME COLORS (UNCHANGED) ---------------- */
private val LavenderBg = Color(0xFFE8E6FF)
private val HeaderPurple = Color(0xFF544DCA)
private val PrimaryBlue = Color(0xFF1022FF)

@Composable
fun EditProfileScreen(
    navController: NavController
) {

    /* ---------------- PROFILE STATES ---------------- */
    var name by remember { mutableStateOf("Jana") }
    var role by remember { mutableStateOf("Mentor • UI/UX & Development") }
    var bio by remember { mutableStateOf("Helping learners grow in Design & Tech 🚀") }

    /* 🔥 Dynamic skills list */
    val skills = remember {
        mutableStateListOf(
            "JavaScript",
            "React.js",
            "Node.js"
        )
    }
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
                            .clickable {
                                navController.popBackStack()
                            }
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

            Spacer(modifier = Modifier.height(20.dp))

            /* ---------------- PROFILE PHOTO (UNCHANGED) ---------------- */
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

            Spacer(modifier = Modifier.height(30.dp))

            /* ---------------- FORM SECTION ---------------- */
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {

                /* ---------- NAME ---------- */
                Text("Full Name", fontWeight = FontWeight.SemiBold, color = PrimaryBlue)
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(14.dp)
                )

                /* ---------- ROLE ---------- */
                Text("Role / Position", fontWeight = FontWeight.SemiBold, color = PrimaryBlue)
                OutlinedTextField(
                    value = role,
                    onValueChange = { role = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(14.dp)
                )

                /* ---------- BIO ---------- */
                Text("Bio / About", fontWeight = FontWeight.SemiBold, color = PrimaryBlue)
                OutlinedTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .padding(bottom = 20.dp),
                    shape = RoundedCornerShape(14.dp)
                )

                /* ---------------- SKILLS SECTION (NEW) ---------------- */
                Text(
                    text = "Skills",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = PrimaryBlue
                )

                Spacer(modifier = Modifier.height(8.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    skills.forEach { skill ->
                        SkillChip(
                            skill = skill,
                            onRemove = { skills.remove(skill) }
                        )
                    }
                }


                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {

                    OutlinedTextField(
                        value = newSkill,
                        onValueChange = { newSkill = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Add skill") },
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            if (newSkill.isNotBlank()) {
                                skills.add(newSkill.trim())
                                newSkill = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(PrimaryBlue)
                    ) {
                        Text("+ Add", color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            /* ---------------- SAVE BUTTON ---------------- */
            Button(
                onClick = {
                    // 🔜 Backend API integration here
                    navController.navigate(Screen.Profile.route) {
                        popUpTo(Screen.Profile.route) { inclusive = true }
                    }
                },
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
