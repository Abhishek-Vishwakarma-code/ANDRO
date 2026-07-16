package com.example.adcs.ui.main

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.adcs.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigateToDashboard: () -> Unit) {
    val scale = remember { Animatable(0f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        scale.animateTo(1f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium))
        alpha.animateTo(1f, animationSpec = tween(600))
        delay(2000)
        onNavigateToDashboard()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(PrimaryContainer, BackgroundDark),
                    radius = 1200f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            // Logo circle
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .scale(scale.value)
                    .background(
                        Brush.radialGradient(listOf(PrimaryPurpleLight, PrimaryPurple)),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("⬡", fontSize = 52.sp, color = BrandGold)
            }

            Spacer(Modifier.height(24.dp))

            // Brand name
            Text(
                text = "ADCS",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.scale(scale.value)
            )
            Text(
                text = "AD CONSULTANT & SERVICE",
                style = MaterialTheme.typography.labelMedium,
                color = BrandGold,
                letterSpacing = 3.sp
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Stamp Duty & Registration Calculator",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(48.dp))

            CircularProgressIndicator(
                color = PrimaryPurple,
                modifier = Modifier.size(32.dp),
                strokeWidth = 2.dp
            )
        }

        // Version at bottom
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Text(
                text = "v1.0 • Maharashtra Leave & License • Bombay Stamp Act 1958",
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                modifier = Modifier.padding(bottom = 32.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}
