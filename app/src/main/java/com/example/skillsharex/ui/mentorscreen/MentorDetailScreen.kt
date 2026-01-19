package com.example.skillsharex.ui.mentorscreen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.skillsharex.network.AuthApiClient
import com.example.skillsharex.viewmodel.MentorDetailViewModel
import androidx.compose.ui.platform.LocalContext


/* ---------------- THEME ---------------- */

private val Lavender = Color(0xFFE8E6FF)
private val HeaderPurple = Color(0xFF544DCA)
private val Gradient = Brush.horizontalGradient(
    listOf(Color(0xFF6C47FF), Color(0xFF4BC9FF))
)


/* ---------------- MENTOR DETAIL SCREEN ---------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MentorDetailScreen(
    navController: NavController,
    mentorId: Int,
    viewModel: MentorDetailViewModel = viewModel()
) {

    LaunchedEffect(mentorId) {
        viewModel.loadMentorDetail(mentorId)
    }


    val context = LocalContext.current

    fun openWhatsAppChat(context: Context, phoneNumber: String) {
        val url = "https://wa.me/$phoneNumber"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)

        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "WhatsApp not installed", Toast.LENGTH_SHORT).show()
        }
    }

    fun openDialer(context: Context, phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        context.startActivity(intent)
    }



    Scaffold(
        containerColor = Lavender,
        topBar = {
            TopAppBar(
                title = { Text("Mentor Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = HeaderPurple,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->

        when {
            viewModel.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            viewModel.errorMessage != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(viewModel.errorMessage ?: "Error", color = Color.Red)
                }
            }

            viewModel.mentor != null -> {
                val mentor = viewModel.mentor!!

                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp)
                ) {

                    /* -------- PROFILE -------- */
                    item {
                        Spacer(Modifier.height(20.dp))

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = mentor.image?.let {
                                        AuthApiClient.IMAGE_BASE_URL + it
                                    }
                                ),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )


                            Spacer(Modifier.height(10.dp))

                            Text(mentor.name, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                            Text(mentor.skill, color = Color.Gray)
                            Text("⭐ ${mentor.rating} (${mentor.ratingCount})", color = Color(0xFFFFC107))

                            Spacer(Modifier.height(16.dp))
                        }
                    }

                    /* -------- ABOUT -------- */
                    item {
                        SectionTitle("About Mentor")
                        Text(mentor.bio, color = Color.DarkGray)
                        Spacer(Modifier.height(14.dp))
                    }

                    /* -------- SKILLS -------- */
                    item {
                        SectionTitle("Skills & Expertise")

                        LazyRow {
                            items(mentor.expertiseList) { skill ->
                                Box(
                                    modifier = Modifier
                                        .padding(end = 10.dp)
                                        .background(Gradient, RoundedCornerShape(20.dp))
                                        .padding(horizontal = 14.dp, vertical = 8.dp)
                                ) {
                                    Text(skill, color = Color.White)
                                }
                            }
                        }

                        Spacer(Modifier.height(20.dp))
                    }

                    // chat and call buttons

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {

                            OutlinedButton(
                                onClick = { openWhatsAppChat(context, "91${mentor.phone}") },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Chat, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Chat")
                            }

                            OutlinedButton(onClick = {
                                openDialer(context, mentor.phone)
                            },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Call, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Call")
                            }
                        }
                    }
                }
            }
        }
    }
}

/* ---------------- COMPONENTS ---------------- */

@Composable
fun SectionTitle(title: String) {
    Text(
        title,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        modifier = Modifier.padding(vertical = 6.dp)
    )
}
