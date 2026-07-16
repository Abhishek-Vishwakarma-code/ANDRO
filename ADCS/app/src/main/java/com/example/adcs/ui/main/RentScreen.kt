package com.example.adcs.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.adcs.data.DataRepository
import com.example.adcs.data.EscalationType
import com.example.adcs.data.RentInfo
import com.example.adcs.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RentScreen(onBack: () -> Unit, onNext: () -> Unit) {
    val state by DataRepository.agreementState.collectAsState()
    var rent by remember { mutableStateOf(state.rent) }

    Scaffold(
        containerColor = BackgroundDark,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Rent Details", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                        Text("Step 3 of 5", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    }
                },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = TextPrimary) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceDark)
            )
        },
        bottomBar = {
            Surface(color = SurfaceDark, tonalElevation = 0.dp) {
                Column(modifier = Modifier.padding(16.dp)) {
                    StepProgressBar(currentStep = 3, totalSteps = 5)
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = { DataRepository.updateRent(rent); onNext() },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                    ) {
                        Text("Continue to Deposit →", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Monthly Rent
            SectionCard(title = "MONTHLY RENT", icon = Icons.Default.CurrencyRupee) {
                OutlinedTextField(
                    value = if (rent.monthlyRent == 0.0) "" else rent.monthlyRent.toInt().toString(),
                    onValueChange = { rent = rent.copy(monthlyRent = it.toDoubleOrNull() ?: 0.0) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Monthly Rent Amount (₹)", color = TextMuted, style = MaterialTheme.typography.bodySmall) },
                    leadingIcon = { Text("₹", color = AccentGreen, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 12.dp)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryPurple, unfocusedBorderColor = InputBorder,
                        focusedContainerColor = InputBackground, unfocusedContainerColor = InputBackground,
                        focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                Spacer(Modifier.height(12.dp))
                Text("QUICK PRESETS", style = MaterialTheme.typography.labelSmall, color = TextMuted, letterSpacing = 1.sp)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    listOf(10000, 15000, 25000, 50000, 100000).forEachIndexed { i, amount ->
                        val labels = listOf("10K", "15K", "25K", "50K", "1L")
                        FilterChip(
                            selected = rent.monthlyRent == amount.toDouble(),
                            onClick = { rent = rent.copy(monthlyRent = amount.toDouble()) },
                            label = { Text(labels[i], fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PrimaryPurple, selectedLabelColor = TextPrimary,
                                containerColor = SurfaceVariantDark, labelColor = TextSecondary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true, selected = rent.monthlyRent == amount.toDouble(),
                                selectedBorderColor = PrimaryPurple, borderColor = InputBorder
                            )
                        )
                    }
                }
            }

            // License Period
            SectionCard(title = "LICENSE PERIOD", icon = Icons.Default.CalendarMonth) {
                OutlinedTextField(
                    value = if (rent.licensePeriodMonths == 0) "" else rent.licensePeriodMonths.toString(),
                    onValueChange = { rent = rent.copy(licensePeriodMonths = it.toIntOrNull() ?: 0) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("License Period (Months)", color = TextMuted, style = MaterialTheme.typography.bodySmall) },
                    leadingIcon = { Icon(Icons.Default.CalendarMonth, null, tint = TextSecondary, modifier = Modifier.size(18.dp)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryPurple, unfocusedBorderColor = InputBorder,
                        focusedContainerColor = InputBackground, unfocusedContainerColor = InputBackground,
                        focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                Spacer(Modifier.height(12.dp))
                Text("COMMON PERIODS", style = MaterialTheme.typography.labelSmall, color = TextMuted, letterSpacing = 1.sp)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(11, 22, 24, 36, 60).forEach { months ->
                        FilterChip(
                            selected = rent.licensePeriodMonths == months,
                            onClick = { rent = rent.copy(licensePeriodMonths = months) },
                            label = { Text("${months}M", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PrimaryPurple, selectedLabelColor = TextPrimary,
                                containerColor = SurfaceVariantDark, labelColor = TextSecondary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true, selected = rent.licensePeriodMonths == months,
                                selectedBorderColor = PrimaryPurple, borderColor = InputBorder
                            )
                        )
                    }
                }

                // Live preview
                if (rent.monthlyRent > 0 && rent.licensePeriodMonths > 0) {
                    Spacer(Modifier.height(12.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = AccentGreen.copy(alpha = 0.1f)),
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, AccentGreen.copy(alpha = 0.3f))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total Rent Consideration", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                            Text(
                                "₹${"%,.0f".format(rent.monthlyRent * rent.licensePeriodMonths)}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = AccentGreen
                            )
                        }
                    }
                }
            }

            // Escalation Type
            SectionCard(title = "RENTAL ESCALATION STRATEGY", icon = Icons.Default.TrendingUp) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ToggleChip(
                        label = "Fixed Monthly",
                        icon = Icons.Default.Lock,
                        selected = rent.escalationType == EscalationType.FIXED,
                        modifier = Modifier.weight(1f),
                        onClick = { rent = rent.copy(escalationType = EscalationType.FIXED) }
                    )
                    ToggleChip(
                        label = "Varying",
                        icon = Icons.Default.TrendingUp,
                        selected = rent.escalationType == EscalationType.VARYING,
                        modifier = Modifier.weight(1f),
                        onClick = { rent = rent.copy(escalationType = EscalationType.VARYING) }
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}
