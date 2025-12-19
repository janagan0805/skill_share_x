package com.example.skillsharex.ui.courses

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.skillsharex.ui.theme.DarkBg
import com.example.skillsharex.ui.theme.TextWhite

@Composable
fun CourseDetailScreen(courseId: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .padding(20.dp)
    ) {
        Text("Course Detail", color = TextWhite)
        Text("Selected Course ID: $courseId", color = TextWhite)
    }
}
