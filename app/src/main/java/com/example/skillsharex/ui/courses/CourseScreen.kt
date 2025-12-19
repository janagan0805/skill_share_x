package com.example.skillsharex.ui.courses

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skillsharex.R

// Backgrounds
private val PageBg = Color(0xFFE8E4FF)
private val HeaderBg = Color(0xFF544DCA)
private val CircleBorder = Color(0xFF5A52D6)

data class CategoryItem(val title: String, val icon: Int)

private val categories = listOf(
    CategoryItem("Java", R.drawable.ic_java),
    CategoryItem("Python", R.drawable.ic_python),
    CategoryItem("UI/UX Design", R.drawable.ic_ui_ux_design),
    CategoryItem("Android", R.drawable.android),
    CategoryItem("Graphics", R.drawable.ic_graphics),
    CategoryItem("Figma", R.drawable.ic_figma),
    CategoryItem("Photoshop", R.drawable.ic_photoshop),
    CategoryItem("Music", R.drawable.ic_music),
    CategoryItem("Communication", R.drawable.ic_communication),
    CategoryItem("Time Management", R.drawable.ic_time),
    CategoryItem("Reading", R.drawable.ic_reading)
)

@Composable
fun CourseCategoriesScreen(
    onBack: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onCategoryClick: (String) -> Unit = {}
) {
    Scaffold(
        containerColor = PageBg,
        topBar = {
            CourseTopBar(onBack = onBack, onMenuClick = onMenuClick)
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 12.dp, vertical = 12.dp)
                .fillMaxSize()
        ) {

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),   // 🔥 FIXED → 2 COLUMNS ONLY
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalArrangement = Arrangement.spacedBy(18.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(categories) { item ->
                    CategoryCard(item.title, item.icon) {
                        onCategoryClick(item.title)
                    }
                }
            }
        }
    }
}

@Composable
fun CourseTopBar(onBack: () -> Unit, onMenuClick: () -> Unit) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(HeaderBg)
            .clip(RoundedCornerShape(bottomStart = 26.dp, bottomEnd = 26.dp))
            .padding(top = 50.dp, bottom = 22.dp, start = 20.dp, end = 20.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {

            // BACK ICON
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier
                    .size(30.dp)
                    .clickable { onBack() }
            )

            // TITLE
            Text(
                text = "Categories",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            // MENU ICON
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More",
                tint = Color.White,
                modifier = Modifier
                    .size(30.dp)
                    .clickable { onMenuClick() }
            )
        }
    }
}




@Composable
fun CategoryCard(title: String, icon: Int, onClick: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(90.dp) // 🔥 Balanced size for all devices
                .clip(CircleShape)
                .background(Color.White)
                .border(2.dp, CircleBorder, CircleShape)
                .padding(14.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = title,
                modifier = Modifier.size(50.dp) // 🔥 Perfect fit
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = title,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
    }
}
