package com.example.adcs.ui.main

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.adcs.data.DataRepository
import com.example.adcs.theme.*

@Composable
fun DashboardScreen(
    onNewAgreement: () -> Unit,
    onHistory: () -> Unit,
    onSettings: () -> Unit
) {
    val scrollState = rememberScrollState()
    val state by DataRepository.agreementState.collectAsState()

    Scaffold(
        containerColor = BackgroundDark,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { DataRepository.resetAgreement(); onNewAgreement() },
                containerColor = PrimaryPurple,
                contentColor = TextPrimary,
                icon = { Icon(Icons.Default.Add, null) },
                text = { Text("New Agreement", fontWeight = FontWeight.SemiBold) }
            )
        },
        bottomBar = {
            NavigationBar(containerColor = SurfaceDark, tonalElevation = 0.dp) {
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = { Icon(Icons.Default.Home, null) },
                    label = { Text("Dashboard") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimaryPurple,
                        selectedTextColor = PrimaryPurple,
                        indicatorColor = PrimaryContainer
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onHistory,
                    icon = { Icon(Icons.Default.History, null) },
                    label = { Text("History") },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onSettings,
                    icon = { Icon(Icons.Default.Settings, null) },
                    label = { Text("Settings") },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    )
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(listOf(PrimaryContainer.copy(alpha = 0.5f), Color.Transparent))
                    )
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(PrimaryPurple, RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("⬡", fontSize = 22.sp, color = BrandGold)
                        }
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text("ADCS", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text("AD CONSULTANT & SERVICE", style = MaterialTheme.typography.labelSmall, color = BrandGold, letterSpacing = 1.5.sp)
                        }
                    }
                    Spacer(Modifier.height(20.dp))
                    Text("Good Evening 👋", style = MaterialTheme.typography.headlineSmall, color = TextPrimary, fontWeight = FontWeight.Bold)
                    Text("Stamp Duty & Registration Calculator", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                }
            }

            Spacer(Modifier.height(4.dp))

            // Stats Cards Row
            Row(modifier = Modifier.padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard(modifier = Modifier.weight(1f), title = "Total", value = "0", subtitle = "Agreements", color = PrimaryPurple, icon = Icons.Default.Description)
                StatCard(modifier = Modifier.weight(1f), title = "This Month", value = "0", subtitle = "New", color = AccentGreen, icon = Icons.Default.TrendingUp)
                StatCard(modifier = Modifier.weight(1f), title = "Revenue", value = "₹0", subtitle = "Collected", color = BrandGold, icon = Icons.Default.AccountBalance)
            }

            Spacer(Modifier.height(24.dp))

            // Quick Actions
            Padding(16.dp) {
                Text("Quick Actions", style = MaterialTheme.typography.titleMedium, color = TextPrimary, fontWeight = FontWeight.SemiBold)
            }
            Spacer(Modifier.height(12.dp))

            Row(modifier = Modifier.padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                QuickActionCard(
                    modifier = Modifier.weight(1f),
                    title = "New Agreement",
                    subtitle = "Create stamp duty receipt",
                    icon = Icons.Default.AddCircle,
                    color = PrimaryPurple,
                    onClick = { DataRepository.resetAgreement(); onNewAgreement() }
                )
                QuickActionCard(
                    modifier = Modifier.weight(1f),
                    title = "History",
                    subtitle = "View saved agreements",
                    icon = Icons.Default.History,
                    color = AccentCyan,
                    onClick = onHistory
                )
            }

            Spacer(Modifier.height(12.dp))

            Row(modifier = Modifier.padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                QuickActionCard(
                    modifier = Modifier.weight(1f),
                    title = "Calculator",
                    subtitle = "Quick stamp duty estimate",
                    icon = Icons.Default.Calculate,
                    color = AccentGreen,
                    onClick = { DataRepository.resetAgreement(); onNewAgreement() }
                )
                QuickActionCard(
                    modifier = Modifier.weight(1f),
                    title = "Settings",
                    subtitle = "App configuration",
                    icon = Icons.Default.Settings,
                    color = BrandGold,
                    onClick = onSettings
                )
            }

            Spacer(Modifier.height(24.dp))

            // Info Banner
            Padding(16.dp) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = PrimaryContainer),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryPurple.copy(alpha = 0.4f))
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, null, tint = AccentCyan, modifier = Modifier.size(24.dp))
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text("Maharashtra Leave & License", style = MaterialTheme.typography.titleSmall, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                            Text("Bombay Stamp Act, 1958 — Article 36A", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        }
                    }
                }
            }

            Spacer(Modifier.height(100.dp)) // FAB clearance
        }
    }
}

@Composable
private fun Padding(size: androidx.compose.ui.unit.Dp, content: @Composable () -> Unit) {
    Box(modifier = Modifier.padding(horizontal = size)) { content() }
}

@Composable
fun StatCard(modifier: Modifier = Modifier, title: String, value: String, subtitle: String, color: Color, icon: ImageVector) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, null, tint = color, modifier = Modifier.size(20.dp))
            Spacer(Modifier.height(8.dp))
            Text(value, style = MaterialTheme.typography.titleLarge, color = TextPrimary, fontWeight = FontWeight.Bold)
            Text(title, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
        }
    }
}

@Composable
fun QuickActionCard(modifier: Modifier = Modifier, title: String, subtitle: String, icon: ImageVector, color: Color, onClick: () -> Unit) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(color.copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = color, modifier = Modifier.size(24.dp))
            }
            Spacer(Modifier.height(12.dp))
            Text(title, style = MaterialTheme.typography.titleSmall, color = TextPrimary, fontWeight = FontWeight.SemiBold)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }
    }
}
