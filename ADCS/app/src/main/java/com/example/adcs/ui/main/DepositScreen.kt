package com.example.adcs.ui.main

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.adcs.data.DataRepository
import com.example.adcs.data.DepositInfo
import com.example.adcs.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DepositScreen(onBack: () -> Unit, onNext: () -> Unit) {
    val state by DataRepository.agreementState.collectAsState()
    var deposit by remember { mutableStateOf(state.deposit) }

    Scaffold(
        containerColor = BackgroundDark,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Deposit Details", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                        Text("Step 4 of 5", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    }
                },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = TextPrimary) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceDark)
            )
        },
        bottomBar = {
            Surface(color = SurfaceDark, tonalElevation = 0.dp) {
                Column(modifier = Modifier.padding(16.dp)) {
                    StepProgressBar(currentStep = 4, totalSteps = 5)
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = { DataRepository.updateDeposit(deposit); onNext() },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                    ) {
                        Text("Continue to Documents →", fontWeight = FontWeight.Bold)
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
            // Refundable Deposit
            SectionCard(title = "REFUNDABLE DEPOSIT", icon = Icons.Default.Savings) {
                OutlinedTextField(
                    value = if (deposit.refundableDeposit == 0.0) "" else deposit.refundableDeposit.toInt().toString(),
                    onValueChange = { deposit = deposit.copy(refundableDeposit = it.toDoubleOrNull() ?: 0.0) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Refundable Security Deposit (₹)", color = TextMuted, style = MaterialTheme.typography.bodySmall) },
                    leadingIcon = { Text("₹", color = BrandGold, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 12.dp)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryPurple, unfocusedBorderColor = InputBorder,
                        focusedContainerColor = InputBackground, unfocusedContainerColor = InputBackground,
                        focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(50000 to "50K", 100000 to "1L", 200000 to "2L", 500000 to "5L").forEach { (amount, label) ->
                        FilterChip(
                            selected = deposit.refundableDeposit == amount.toDouble(),
                            onClick = { deposit = deposit.copy(refundableDeposit = amount.toDouble()) },
                            label = { Text(label, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PrimaryPurple, selectedLabelColor = TextPrimary,
                                containerColor = SurfaceVariantDark, labelColor = TextSecondary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true, selected = deposit.refundableDeposit == amount.toDouble(),
                                selectedBorderColor = PrimaryPurple, borderColor = InputBorder
                            )
                        )
                    }
                }
            }

            // Non-Refundable Deposit
            SectionCard(title = "NON-REFUNDABLE DEPOSIT", icon = Icons.Default.MoneyOff) {
                OutlinedTextField(
                    value = if (deposit.nonRefundableDeposit == 0.0) "" else deposit.nonRefundableDeposit.toInt().toString(),
                    onValueChange = { deposit = deposit.copy(nonRefundableDeposit = it.toDoubleOrNull() ?: 0.0) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Non-Refundable / One-time Premium (₹)", color = TextMuted, style = MaterialTheme.typography.bodySmall) },
                    leadingIcon = { Text("₹", color = ErrorRed, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 12.dp)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryPurple, unfocusedBorderColor = InputBorder,
                        focusedContainerColor = InputBackground, unfocusedContainerColor = InputBackground,
                        focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(0 to "₹0", 10000 to "10K", 25000 to "25K", 50000 to "50K").forEach { (amount, label) ->
                        FilterChip(
                            selected = deposit.nonRefundableDeposit == amount.toDouble(),
                            onClick = { deposit = deposit.copy(nonRefundableDeposit = amount.toDouble()) },
                            label = { Text(label, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PrimaryPurple, selectedLabelColor = TextPrimary,
                                containerColor = SurfaceVariantDark, labelColor = TextSecondary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true, selected = deposit.nonRefundableDeposit == amount.toDouble(),
                                selectedBorderColor = PrimaryPurple, borderColor = InputBorder
                            )
                        )
                    }
                }
            }

            // Heavy Deposit Toggle
            SectionCard(title = "HEAVY DEPOSIT?", icon = Icons.Default.AccountBalanceWallet) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ToggleChip(
                        label = "No",
                        icon = Icons.Default.Close,
                        selected = !deposit.hasHeavyDeposit,
                        modifier = Modifier.weight(1f),
                        onClick = { deposit = deposit.copy(hasHeavyDeposit = false) }
                    )
                    ToggleChip(
                        label = "Yes",
                        icon = Icons.Default.Check,
                        selected = deposit.hasHeavyDeposit,
                        modifier = Modifier.weight(1f),
                        onClick = { deposit = deposit.copy(hasHeavyDeposit = true) }
                    )
                }
            }

            // Notional Interest Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = PrimaryContainer.copy(alpha = 0.4f)),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryPurple.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, null, tint = AccentCyan, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Notional Interest Rule", style = MaterialTheme.typography.titleSmall, color = AccentCyan, fontWeight = FontWeight.SemiBold)
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Refundable security deposits are subject to a notional interest rate of 10% per annum (simple interest) for the agreement duration. This calculated amount is added to the total rent and non-refundable deposits to form the \"Total Consideration\" (Taxable Base).",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}
