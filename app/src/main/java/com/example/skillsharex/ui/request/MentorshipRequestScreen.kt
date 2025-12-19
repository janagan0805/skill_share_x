package com.example.skillsharex.ui.requests

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.skillsharex.R
import com.example.skillsharex.data.sampleRequests
import com.example.skillsharex.model.MentorshipRequest

// ---------- THEME ----------
private val Lavender = Color(0xFFE8E6FF)
private val HeaderPurple = Color(0xFF544DCA)
private val CardBg = Color.White
private val TextPrimary = Color(0xFF1A1A1A)
private val TextSecondary = Color(0xFF555555)
private val Gradient = Brush.horizontalGradient(listOf(Color(0xFF6C47FF), Color(0xFF4BC9FF)))

@Composable
fun MentorshipRequestsScreen(navController: NavController) {

    val context = LocalContext.current

    Scaffold(
        containerColor = Lavender,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(HeaderPurple)
                    .padding(top = 46.dp, bottom = 20.dp, start = 16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {

                    // FIXED: back arrow always visible
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(Modifier.width(10.dp))

                    Text(
                        "Requests",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { inner ->

        LazyColumn(
            modifier = Modifier
                .padding(inner)
                .padding(16.dp)
        ) {

            items(sampleRequests) { request ->

                RequestCard(
                    request = request,
                    onApprove = {
                        request.status = "Approved"
                        Toast.makeText(context, "Request Approved", Toast.LENGTH_SHORT).show()

                        // Open chat automatically
                        navController.navigate("chat/${request.userName}")
                    },
                    onReject = { reason ->
                        request.status = "Rejected"
                        request.rejectionReason = reason

                        Toast.makeText(context, "Rejected: $reason", Toast.LENGTH_SHORT).show()
                    }
                )

                Spacer(Modifier.height(16.dp))
            }

            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}

// ---------- REQUEST CARD ----------
@Composable
fun RequestCard(
    request: MentorshipRequest,
    onApprove: () -> Unit,
    onReject: (String) -> Unit
) {

    var showRejectDialog by remember { mutableStateOf(false) }
    var reasonText by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(CardBg),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            // --- USER INFO ROW ---
            Row(verticalAlignment = Alignment.CenterVertically) {

                Image(
                    painter = painterResource(id = request.userAvatar),
                    contentDescription = "",
                    modifier = Modifier
                        .size(55.dp)
                        .clip(CircleShape)
                )

                Spacer(Modifier.width(12.dp))

                Column {
                    Text(request.userName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(request.timeAgo, color = TextSecondary, fontSize = 12.sp)
                }
            }

            Spacer(Modifier.height(10.dp))

            Text(
                "Requested Skill: ${request.requestedSkill}",
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )

            Spacer(Modifier.height(6.dp))

            Text(request.message, color = TextSecondary)

            Spacer(Modifier.height(16.dp))

            // --- STATUS HANDLING ---
            when (request.status) {
                "Pending" -> ActionButtons(
                    onApprove = onApprove,
                    onReject = { showRejectDialog = true }
                )

                "Approved" -> StatusBadge("Approved", Color(0xFF2ECC71))

                "Rejected" -> StatusBadge("Rejected: ${request.rejectionReason}", Color(0xFFE74C3C))
            }
        }
    }

    // -------- REJECT POPUP --------
    if (showRejectDialog) {
        AlertDialog(
            onDismissRequest = { showRejectDialog = false },
            title = { Text("Reason for Rejection") },
            text = {
                Column {
                    Text("Tell them why you rejected:")
                    Spacer(Modifier.height(10.dp))

                    TextField(
                        value = reasonText,
                        onValueChange = { reasonText = it },
                        placeholder = { Text("Ex: Busy schedule, skill mismatch...") }
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (reasonText.isNotBlank()) {
                        onReject(reasonText)
                        showRejectDialog = false
                    }
                }) { Text("Submit") }
            },
            dismissButton = {
                TextButton(onClick = { showRejectDialog = false }) { Text("Cancel") }
            }
        )
    }
}

// ---------- APPROVE / REJECT BUTTONS ----------
@Composable
fun ActionButtons(onApprove: () -> Unit, onReject: () -> Unit) {
    Row(Modifier.fillMaxWidth()) {

        // Approve
        Box(
            modifier = Modifier
                .weight(1f)
                .background(Gradient, RoundedCornerShape(14.dp))
                .clickable(onClick = onApprove)
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("Approve", color = Color.White, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.width(14.dp))

        // Reject
        Box(
            modifier = Modifier
                .weight(1f)
                .border(1.dp, Gradient, RoundedCornerShape(14.dp))
                .clickable(onClick = onReject)
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("Reject", color = TextPrimary, fontWeight = FontWeight.Bold)
        }
    }
}

// ---------- STATUS BADGE ----------
@Composable
fun StatusBadge(text: String, color: Color) {
    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(text, color = color, fontWeight = FontWeight.Bold)
    }
}
