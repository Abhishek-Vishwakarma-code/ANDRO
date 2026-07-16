package com.example.adcs.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.adcs.data.DataRepository
import com.example.adcs.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfScreen(onBack: () -> Unit, onDone: () -> Unit) {
    val state by DataRepository.agreementState.collectAsState()
    val result = remember(state) { DataRepository.calculate(state) }

    Scaffold(
        containerColor = BackgroundDark,
        topBar = {
            TopAppBar(
                title = { Text("PDF Export", color = TextPrimary, fontWeight = FontWeight.SemiBold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = TextPrimary) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceDark)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(Modifier.height(16.dp))

            // PDF Icon
            Box(
                modifier = Modifier.size(100.dp).background(PrimaryContainer, RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.PictureAsPdf, null, tint = PrimaryPurpleLight, modifier = Modifier.size(52.dp))
            }

            Spacer(Modifier.height(8.dp))

            Text("Ready to Export", style = MaterialTheme.typography.headlineSmall, color = TextPrimary, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Text(
                "Your stamp duty receipt for ${state.parties.licensorName.ifEmpty { "Agreement" }} is ready",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            // Grand Total Preview
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = AccentGreen.copy(alpha = 0.08f)),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, AccentGreen.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total Payable", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                    Text("₹${"%,.0f".format(result.grandTotal)}/-", style = MaterialTheme.typography.titleLarge, color = AccentGreen, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(Modifier.height(8.dp))

            // Action Buttons
            Button(
                onClick = { /* PDF generation coming in Phase 4 */ },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
            ) {
                Icon(Icons.Default.Download, null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("DOWNLOAD PDF", fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
                onClick = { /* Share intent */ },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryPurple)
            ) {
                Icon(Icons.Default.Share, null, tint = PrimaryPurpleLight, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("SHARE VIA WHATSAPP / EMAIL", color = PrimaryPurpleLight)
            }

            OutlinedButton(
                onClick = { /* Copy text */ },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor)
            ) {
                Icon(Icons.Default.ContentCopy, null, tint = TextSecondary, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("COPY TEXT SUMMARY", color = TextSecondary)
            }

            Spacer(Modifier.weight(1f))

            TextButton(onClick = onDone) {
                Text("← Back to Dashboard", color = PrimaryPurpleLight)
            }

            Text(
                "PDF generation with full ADCS branding coming in Phase 4",
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}
