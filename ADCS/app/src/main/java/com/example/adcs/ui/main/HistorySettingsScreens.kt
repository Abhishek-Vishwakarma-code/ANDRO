package com.example.adcs.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.adcs.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(onBack: () -> Unit) {
    Scaffold(
        containerColor = BackgroundDark,
        topBar = {
            TopAppBar(
                title = { Text("Agreement History", color = TextPrimary, fontWeight = FontWeight.SemiBold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = TextPrimary) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceDark),
                actions = {
                    IconButton(onClick = {}) { Icon(Icons.Default.Search, null, tint = TextSecondary) }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(Icons.Default.History, null, tint = TextMuted, modifier = Modifier.size(64.dp))
            Spacer(Modifier.height(16.dp))
            Text("No Agreements Yet", style = MaterialTheme.typography.titleMedium, color = TextSecondary, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Text(
                "Create your first agreement to see history here",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
            Spacer(Modifier.height(24.dp))
            Text(
                "Coming soon: Local database storage for all agreements",
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 48.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBack: () -> Unit) {
    Scaffold(
        containerColor = BackgroundDark,
        topBar = {
            TopAppBar(
                title = { Text("Settings", color = TextPrimary, fontWeight = FontWeight.SemiBold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = TextPrimary) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceDark)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // App Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor)
            ) {
                Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier.size(72.dp).background(PrimaryPurple, RoundedCornerShape(18.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("⬡", fontSize = 36.sp, color = BrandGold)
                    }
                    Spacer(Modifier.height(12.dp))
                    Text("ADCS", style = MaterialTheme.typography.titleLarge, color = TextPrimary, fontWeight = FontWeight.Bold)
                    Text("AD CONSULTANT & SERVICE", style = MaterialTheme.typography.labelSmall, color = BrandGold)
                    Spacer(Modifier.height(4.dp))
                    Text("Version 1.0.0", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                    Text("Maharashtra Leave & License Calculator", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                }
            }

            // Legal Info
            SectionCard(title = "LEGAL FRAMEWORK", icon = Icons.Default.Gavel) {
                SettingsInfoRow("Act", "Bombay Stamp Act, 1958")
                SettingsInfoRow("Article", "Article 36A")
                SettingsInfoRow("State", "Maharashtra")
                SettingsInfoRow("Agreement Type", "Leave and License")
                SettingsInfoRow("Stamp Duty Rate", "0.25% of Taxable Base")
                SettingsInfoRow("Notional Interest", "10% p.a. (Simple)")
                SettingsInfoRow("Min. Stamp Duty", "₹500/-")
                SettingsInfoRow("Urban Reg. Fee", "₹1,000/-")
                SettingsInfoRow("Rural Reg. Fee", "₹500/-")
            }

            // Service Charges Info
            SectionCard(title = "FIXED SERVICE CHARGES", icon = Icons.Default.Business) {
                SettingsInfoRow("Legal / Drafting", "₹1,000/-")
                SettingsInfoRow("Visiting / Biometrics", "₹499/-")
                SettingsInfoRow("Police Verification", "₹200/-")
                SettingsInfoRow("Document Handling", "₹300/-")
                SettingsInfoRow("GST (optional)", "18% on services")
            }

            Spacer(Modifier.height(16.dp))

            Text(
                "This app is for estimation purposes only.\nActual stamp duty may vary slightly as determined by the IGR (Inspector General of Registration) office.",
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
private fun SettingsInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        Text(value, style = MaterialTheme.typography.bodySmall, color = TextPrimary, fontWeight = FontWeight.Medium)
    }
}
