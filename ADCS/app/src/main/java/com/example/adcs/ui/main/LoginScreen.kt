package com.example.adcs.ui.main

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.adcs.theme.*

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var selectedRole by remember { mutableStateOf("Executive") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(PrimaryContainer.copy(alpha = 0.3f), BackgroundDark, BackgroundDark))
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(80.dp))

            // Logo & Brand
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        Brush.radialGradient(listOf(PrimaryPurpleLight, PrimaryPurple)),
                        shape = RoundedCornerShape(20.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("⬡", fontSize = 36.sp, color = BrandGold)
            }

            Spacer(Modifier.height(16.dp))
            Text("ADCS", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text("AD CONSULTANT & SERVICE", style = MaterialTheme.typography.labelSmall, color = BrandGold, letterSpacing = 2.sp)

            Spacer(Modifier.height(40.dp))

            // Login Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("Welcome Back", style = MaterialTheme.typography.titleLarge, color = TextPrimary, fontWeight = FontWeight.Bold)
                    Text("Sign in to continue", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)

                    Spacer(Modifier.height(24.dp))

                    // Role Toggle
                    Text("ROLE", style = MaterialTheme.typography.labelSmall, color = TextMuted, letterSpacing = 1.sp)
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(InputBackground, RoundedCornerShape(12.dp))
                            .padding(4.dp)
                    ) {
                        listOf("Admin", "Executive").forEach { role ->
                            val isSelected = selectedRole == role
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(
                                        if (isSelected) PrimaryPurple else androidx.compose.ui.graphics.Color.Transparent,
                                        RoundedCornerShape(10.dp)
                                    )
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                TextButton(onClick = { selectedRole = role }) {
                                    Text(
                                        role,
                                        color = if (isSelected) TextPrimary else TextSecondary,
                                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    // Username
                    Text("USERNAME", style = MaterialTheme.typography.labelSmall, color = TextMuted, letterSpacing = 1.sp)
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it; errorMessage = "" },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Enter username", color = TextMuted) },
                        leadingIcon = { Icon(Icons.Default.Person, null, tint = TextSecondary) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryPurple,
                            unfocusedBorderColor = InputBorder,
                            focusedContainerColor = InputBackground,
                            unfocusedContainerColor = InputBackground,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(Modifier.height(16.dp))

                    // Password
                    Text("PASSWORD", style = MaterialTheme.typography.labelSmall, color = TextMuted, letterSpacing = 1.sp)
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it; errorMessage = "" },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Enter password", color = TextMuted) },
                        leadingIcon = { Icon(Icons.Default.Lock, null, tint = TextSecondary) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    null, tint = TextSecondary
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryPurple,
                            unfocusedBorderColor = InputBorder,
                            focusedContainerColor = InputBackground,
                            unfocusedContainerColor = InputBackground,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    if (errorMessage.isNotEmpty()) {
                        Spacer(Modifier.height(8.dp))
                        Text(errorMessage, color = ErrorRed, style = MaterialTheme.typography.bodySmall)
                    }

                    Spacer(Modifier.height(28.dp))

                    // Login Button
                    Button(
                        onClick = {
                            if (username.isEmpty() || password.isEmpty()) {
                                errorMessage = "Please enter username and password"
                            } else {
                                isLoading = true
                                // Simple local auth: any non-empty credentials work for now
                                onLoginSuccess()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = TextPrimary, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        } else {
                            Text("SIGN IN", fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // Quick access hint
                    Text(
                        "Demo: Enter any username & password to continue",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(Modifier.height(32.dp))
            Text(
                "Maharashtra Leave & License Agreement\nBombay Stamp Act, 1958 — Article 36A",
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(32.dp))
        }
    }
}
