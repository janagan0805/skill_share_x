package com.example.skillsharex.ui.splash

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skillsharex.R

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun OnboardingScreen(
    onFinish: () -> Unit,
    // ✅ DEFAULT VALUE ADDED (THIS FIXES AppNavHost ERROR)
    onNavigateToAuth: (Boolean) -> Unit = {}
) {
    var page by remember { mutableStateOf(0) }

    val pages = listOf(
        Onboard("Step Into A World\nOf Learning Excellence", R.drawable.onboard1),
        Onboard("Explore Endless\nPossibilities", R.drawable.onboard2),
        Onboard("Empower Your\nSkill Journey", R.drawable.onboard3),
        Onboard("Join Now To\nExchange Your\nSkills", R.drawable.onboard4)
    )

    val bgGradient = Brush.verticalGradient(
        listOf(Color(0xFFDCCFFB), Color(0xFFEDE9FF))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgGradient)
            .padding(20.dp)
    ) {

        // ---------- SKIP BUTTON ----------
        if (page < pages.lastIndex) {
            Text(
                text = "SKIP",
                color = Color.Black,
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .clickable { onFinish() }
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            // ---------- IMAGE ----------
            AnimatedContent(
                targetState = page,
                transitionSpec = {
                    fadeIn(tween(300)) with fadeOut(tween(250))
                }
            ) { index ->
                Image(
                    painter = painterResource(id = pages[index].image),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .height(280.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ---------- TITLE ----------
            AnimatedContent(
                targetState = page,
                transitionSpec = {
                    fadeIn(tween(280)) with fadeOut(tween(200))
                }
            ) { i ->
                Text(
                    text = pages[i].title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF18181B),
                    lineHeight = 34.sp,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .align(Alignment.CenterHorizontally),
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // ---------- DOT INDICATORS ----------
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(bottom = 10.dp)
            ) {
                repeat(pages.size) { i ->
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(if (i == page) 12.dp else 8.dp)
                            .clip(RoundedCornerShape(50))
                            .background(
                                if (i == page) Color(0xFF544DCA)
                                else Color(0xFFBDB6E8)
                            )
                            .clickable { page = i }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ---------- BUTTONS ----------
            if (page < pages.lastIndex) {
                Button(
                    onClick = { page++ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF544DCA))
                ) {
                    Text("CONTINUE", color = Color.White, fontWeight = FontWeight.Bold)
                }
            } else {
                // -------- SIGN IN --------
                Button(
                    onClick = { onNavigateToAuth(true) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF1022FF))
                ) {
                    Text("SIGN IN", color = Color.White, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // -------- SIGN UP --------
                OutlinedButton(
                    onClick = { onNavigateToAuth(false) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("SIGN UP")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // -------- GUEST --------
                Text(
                    text = "Continue as Guest",
                    color = Color.Gray,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable { onFinish() }
                        .padding(bottom = 10.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

data class Onboard(val title: String, val image: Int)
