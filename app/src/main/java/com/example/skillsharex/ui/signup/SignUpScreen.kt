package com.example.skillsharex.ui.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skillsharex.network.AuthApiClient
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    onSignUpSuccess: () -> Unit,
    onBackToLogin: () -> Unit
) {

    val scope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var errorMsg by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF151358), Color(0xFF241C87))
                )
            ),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {

            Text(
                text = "Create Account",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = textFieldColors()
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = textFieldColors()
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                colors = textFieldColors()
            )

            Spacer(Modifier.height(16.dp))

            if (errorMsg.isNotEmpty()) {
                Text(errorMsg, color = Color.Red)
                Spacer(Modifier.height(8.dp))
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading,
                shape = RoundedCornerShape(12.dp),
                onClick = {

                    if (name.isBlank() || email.isBlank() || password.isBlank()) {
                        errorMsg = "All fields are required"
                        return@Button
                    }

                    loading = true
                    errorMsg = ""

                    scope.launch {
                        try {
                            val response = AuthApiClient.api.register(
                                name = name,
                                email = email,
                                password = password
                            )

                            if (response.isSuccessful) {
                                val body = response.body()
                                if (body?.status == true) {
                                    onSignUpSuccess()
                                } else {
                                    errorMsg = body?.message ?: "Registration failed"
                                }
                            } else {
                                errorMsg = "Server error"
                            }

                        } catch (e: Exception) {
                            errorMsg = e.message ?: "Network error"
                        } finally {
                            loading = false
                        }
                    }
                }
            ) {
                Text(if (loading) "Creating..." else "Sign Up")
            }

            Spacer(Modifier.height(12.dp))

            TextButton(onClick = onBackToLogin) {
                Text(
                    text = "Already have an account? Login",
                    color = Color.White
                )
            }
        }
    }
}

/* ---------- TEXT FIELD COLORS (UNCHANGED THEME) ---------- */

@Composable
private fun textFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    cursorColor = Color.White,
    focusedBorderColor = Color.White,
    unfocusedBorderColor = Color.White.copy(alpha = 0.6f),
    focusedLabelColor = Color.White,
    unfocusedLabelColor = Color.White.copy(alpha = 0.7f)
)
