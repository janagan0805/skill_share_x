package com.example.skillsharex.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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

private val LavenderBg = Color(0xFFE8E6FF)
private val HeaderPurple = Color(0xFF544DCA)
private val PrimaryBlue = Color(0xFF1022FF)

@Composable
fun SettingsScreen(
    navController: NavController
) {
    var notificationsOn by remember { mutableStateOf(true) }
    var darkModeOn by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LavenderBg)
    ) {

        Column {

            // ---------------- HEADER ----------------
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
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(28.dp)
                            .clickable { navController.popBackStack() }
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "Settings",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ---------------- ACCOUNT SECTION ----------------
            SettingsSectionTitle("Account")

            SettingItem(
                icon = Icons.Default.Person,
                title = "Edit Profile",
                onClick = {}
            )

            SettingItem(
                icon = Icons.Default.Key,
                title = "Change Password",
                onClick = {}
            )

            Divider(thickness = 0.5.dp, color = Color.Gray.copy(alpha = 0.3f))

            // ---------------- PREFERENCES ----------------
            SettingsSectionTitle("Preferences")

            SettingSwitchItem(
                icon = Icons.Default.Notifications,
                title = "Notifications",
                checked = notificationsOn,
                onCheckedChange = { notificationsOn = it }
            )

            SettingSwitchItem(
                icon = Icons.Default.Brightness4,
                title = "Dark Mode",
                checked = darkModeOn,
                onCheckedChange = { darkModeOn = it }
            )

            Divider(thickness = 0.5.dp, color = Color.Gray.copy(alpha = 0.3f))

            // ---------------- SUPPORT ----------------
            SettingsSectionTitle("Support")

            SettingItem(
                icon = Icons.Default.Help,
                title = "Help & Feedback",
                onClick = {}
            )

            SettingItem(
                icon = Icons.Default.Info,
                title = "About App",
                onClick = {}
            )

            Divider(thickness = 0.5.dp, color = Color.Gray.copy(alpha = 0.3f))

            // ---------------- LOGOUT ----------------
            SettingItem(
                icon = Icons.Default.Logout,
                title = "Logout",
                titleColor = Color.Red,
                onClick = {}
            )
        }
    }
}

// ---------------- SECTION TITLE ----------------
@Composable
fun SettingsSectionTitle(title: String) {
    Text(
        text = title,
        color = Color.Gray,
        fontSize = 14.sp,
        modifier = Modifier.padding(start = 20.dp, top = 10.dp, bottom = 6.dp)
    )
}

// ---------------- SETTINGS ITEM ----------------
@Composable
fun SettingItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    titleColor: Color = Color.Black,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(icon, contentDescription = null, tint = PrimaryBlue)

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = title,
            color = titleColor,
            fontSize = 16.sp
        )
    }
}

// ---------------- SWITCH ITEM ----------------
@Composable
fun SettingSwitchItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(icon, contentDescription = null, tint = PrimaryBlue)

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = title,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
