package com.example.skillsharex.ui.feed

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skillsharex.R

// ---------- THEME ----------
private val LavenderBg = Color(0xFFE8E6FF)
private val HeaderPurple = Color(0xFF544DCA)
private val CardBg = Color.White
private val TitleText = Color(0xFF1A1A1A)
private val SecondaryText = Color(0xFF555555)
private val TagText = Color(0xFF6C47FF)
private val AccentGradient =
    Brush.horizontalGradient(listOf(Color(0xFF6C47FF), Color(0xFF4BC9FF)))

// ---------- DATA ----------
data class FeedPost(
    val id: String,
    val userName: String,
    val userAvatarRes: Int,
    val timeAgo: String,
    val text: String,
    val imageRes: Int?,
    val tags: List<String>,
    val likes: Int,
    val comments: Int,
    val views: Int
)

data class StoryItem(val id: String, val title: String, val avatarRes: Int)

private val sampleStories = listOf(
    StoryItem("s1", "You", R.drawable.jana),
    StoryItem("s2", "Top Mentor", R.drawable.karthick),
    StoryItem("s3", "Design", R.drawable.ic_ui_ux_design),
    StoryItem("s4", "Android", R.drawable.android)
)

private val sampleFeed = mutableStateListOf(
    FeedPost(
        "p1", "Peter Parker", R.drawable.karthick, "1h",
        "Just finished the session — amazing learning experience!",
        R.drawable.android, listOf("#Android", "#Kotlin"), 15, 4, 120
    ),
    FeedPost(
        "p2", "Tony Stark", R.drawable.dilip, "5h",
        "Let's teach new technologies together!",
        null, listOf("#TeamWork", "#Tech"), 15, 7, 210
    )
)


// ---------- MAIN FEED SCREEN ----------
@Composable
fun FeedScreen(
    onBack: () -> Unit = {},
    openPost: (String) -> Unit = {},
    openCreatePost: () -> Unit = {},
    openProfile: (String) -> Unit = {}
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LavenderBg)
    ) {

        Column {

            // ---------- HEADER ----------
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
                            .clickable { onBack() }
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "Community Feed",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )

                    Spacer(Modifier.weight(1f))

                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add",
                        tint = Color.White,
                        modifier = Modifier
                            .size(30.dp)
                            .clickable { openCreatePost() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ---------- CREATE POST BAR ----------
            CreatePostBar(openCreatePost)

            Spacer(modifier = Modifier.height(16.dp))

            // ---------- STORIES ----------
            StoriesRow(sampleStories)

            Spacer(modifier = Modifier.height(16.dp))

            // ---------- FEED ----------
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp),
            ) {
                items(sampleFeed) { post ->
                    PostCard(
                        post = post,
                        onClick = { openPost(post.id) },
                        onProfileClick = { openProfile(post.userName) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        // ---------- FLOATING BUTTON ----------
        FloatingActionButton(
            onClick = openCreatePost,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp),
            containerColor = Color.Transparent
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(AccentGradient, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(30.dp))
            }
        }
    }
}


// ---------- CREATE POST BAR ----------
@Composable
private fun CreatePostBar(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(Color.White, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = painterResource(id = R.drawable.jana),
            contentDescription = "You",
            modifier = Modifier.size(40.dp).clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text("Share something...", color = SecondaryText)

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            painter = painterResource(id = R.drawable.ic_camera),
            contentDescription = "Camera",
            tint = Color.Unspecified,
            modifier = Modifier.size(22.dp)
        )
    }
}


// ---------- STORIES ----------
@Composable
private fun StoriesRow(stories: List<StoryItem>) {

    LazyRow(modifier = Modifier.padding(horizontal = 16.dp)) {
        items(stories) { s ->

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(end = 14.dp)
            ) {

                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(AccentGradient),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = s.avatarRes),
                        contentDescription = s.title,
                        modifier = Modifier
                            .size(58.dp)
                            .clip(CircleShape)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(s.title, fontSize = 12.sp, color = SecondaryText)
            }
        }
    }
}


// ---------- POST CARD ----------
@Composable
private fun PostCard(post: FeedPost, onClick: () -> Unit, onProfileClick: () -> Unit) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            // USER HEADER
            Row(verticalAlignment = Alignment.CenterVertically) {

                Image(
                    painter = painterResource(id = post.userAvatarRes),
                    contentDescription = post.userName,
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .clickable { onProfileClick() }
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(Modifier.weight(1f)) {
                    Text(post.userName, fontWeight = FontWeight.Bold)
                    Text(post.timeAgo, color = SecondaryText, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(post.text, color = TitleText)

            if (post.tags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                TagRow(post.tags)
            }

            post.imageRes?.let {
                Spacer(modifier = Modifier.height(10.dp))
                Image(
                    painter = painterResource(id = it),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onClick() }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // FOOTER
            Row(verticalAlignment = Alignment.CenterVertically) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {

                    Icon(
                        painter = painterResource(id = R.drawable.ic_heart),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("${post.likes}", color = SecondaryText)

                    Spacer(modifier = Modifier.width(14.dp))

                    Icon(
                        painter = painterResource(id = R.drawable.ic_comment),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("${post.comments}", color = SecondaryText)
                }

                Text("${post.views} views", color = SecondaryText, fontSize = 12.sp)
            }
        }
    }
}


// ---------- TAG ROW ----------
@Composable
private fun TagRow(tags: List<String>) {
    Row {
        tags.forEach { tag ->
            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .background(Color(0xFFF0EEFF), RoundedCornerShape(14.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(tag, color = TagText, fontSize = 12.sp)
            }
        }
    }
}
