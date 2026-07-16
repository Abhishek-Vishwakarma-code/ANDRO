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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.adcs.data.DataRepository
import com.example.adcs.data.DocumentsInfo
import com.example.adcs.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentsScreen(onBack: () -> Unit, onNext: () -> Unit) {
    val state by DataRepository.agreementState.collectAsState()
    var docs by remember { mutableStateOf(state.documents) }
    var customDocInput by remember { mutableStateOf("") }

    Scaffold(
        containerColor = BackgroundDark,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Documents", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                        Text("Step 5 of 5", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    }
                },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = TextPrimary) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceDark)
            )
        },
        bottomBar = {
            Surface(color = SurfaceDark, tonalElevation = 0.dp) {
                Column(modifier = Modifier.padding(16.dp)) {
                    StepProgressBar(currentStep = 5, totalSteps = 5)
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = { DataRepository.updateDocuments(docs); onNext() },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                    ) {
                        Icon(Icons.Default.Receipt, null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("View Summary & Receipt →", fontWeight = FontWeight.Bold)
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
            // GST Toggle
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Receipt, null, tint = BrandGold, modifier = Modifier.size(24.dp))
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Enable GST Billing", style = MaterialTheme.typography.titleSmall, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                        Text("Tax Invoice with 18% GST", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    }
                    Switch(
                        checked = state.enableGst,
                        onCheckedChange = { DataRepository.updateGst(it) },
                        colors = SwitchDefaults.colors(checkedThumbColor = TextPrimary, checkedTrackColor = PrimaryPurple)
                    )
                }
            }

            // Standard Documents Checklist
            SectionCard(title = "DOCUMENTS PROVIDED CHECKLIST", icon = Icons.Default.Checklist) {
                val documentList = listOf(
                    Triple("Aadhaar Card", Icons.Default.CreditCard, docs.hasAadhaar),
                    Triple("PAN Card", Icons.Default.Badge, docs.hasPan),
                    Triple("Electricity Bill", Icons.Default.ElectricBolt, docs.hasElectricityBill),
                    Triple("Index II", Icons.Default.Article, docs.hasIndexII),
                    Triple("Registered Deed", Icons.Default.Verified, docs.hasRegisteredDeed),
                    Triple("Property Tax Receipt", Icons.Default.Receipt, docs.hasPropertyTax)
                )

                documentList.chunked(2).forEach { pair ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        pair.forEach { (name, icon, isChecked) ->
                            DocumentCheckCard(
                                modifier = Modifier.weight(1f),
                                label = name,
                                icon = icon,
                                checked = isChecked,
                                onCheckedChange = { checked ->
                                    docs = when (name) {
                                        "Aadhaar Card" -> docs.copy(hasAadhaar = checked)
                                        "PAN Card" -> docs.copy(hasPan = checked)
                                        "Electricity Bill" -> docs.copy(hasElectricityBill = checked)
                                        "Index II" -> docs.copy(hasIndexII = checked)
                                        "Registered Deed" -> docs.copy(hasRegisteredDeed = checked)
                                        "Property Tax Receipt" -> docs.copy(hasPropertyTax = checked)
                                        else -> docs
                                    }
                                }
                            )
                        }
                        if (pair.size == 1) Spacer(Modifier.weight(1f))
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }

            // Aadhaar/PAN Text for Licensor & Licensee
            SectionCard(title = "IDENTITY DOCUMENT DETAILS", icon = Icons.Default.Badge) {
                AdcsTextField(
                    label = "Licensor — Aadhaar No. / PAN",
                    value = docs.licensorAadhaarPanText,
                    leadingIcon = Icons.Default.Person,
                    onValueChange = { docs = docs.copy(licensorAadhaarPanText = it) }
                )
                Spacer(Modifier.height(12.dp))
                AdcsTextField(
                    label = "Licensee — Aadhaar No. / PAN",
                    value = docs.licenseeAadhaarPanText,
                    leadingIcon = Icons.Default.PersonPin,
                    onValueChange = { docs = docs.copy(licenseeAadhaarPanText = it) }
                )
            }

            // Custom Documents
            SectionCard(title = "ADD CUSTOM DOCUMENT", icon = Icons.Default.AddCircleOutline) {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = customDocInput,
                        onValueChange = { customDocInput = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("e.g. NOC, Maintenance Receipt", color = TextMuted, style = MaterialTheme.typography.bodySmall) },
                        leadingIcon = { Icon(Icons.Default.Description, null, tint = TextSecondary, modifier = Modifier.size(18.dp)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryPurple, unfocusedBorderColor = InputBorder,
                            focusedContainerColor = InputBackground, unfocusedContainerColor = InputBackground,
                            focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                    Spacer(Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            if (customDocInput.isNotBlank()) {
                                docs = docs.copy(customDocuments = docs.customDocuments + customDocInput.trim())
                                customDocInput = ""
                            }
                        },
                        modifier = Modifier.background(PrimaryPurple, RoundedCornerShape(12.dp))
                    ) {
                        Icon(Icons.Default.Add, null, tint = TextPrimary)
                    }
                }

                if (docs.customDocuments.isNotEmpty()) {
                    Spacer(Modifier.height(12.dp))
                    docs.customDocuments.forEach { doc ->
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                            Icon(Icons.Default.Description, null, tint = AccentGreen, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(doc, style = MaterialTheme.typography.bodySmall, color = TextSecondary, modifier = Modifier.weight(1f))
                            IconButton(
                                onClick = { docs = docs.copy(customDocuments = docs.customDocuments - doc) },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(Icons.Default.Close, null, tint = ErrorRed, modifier = Modifier.size(14.dp))
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
fun DocumentCheckCard(
    modifier: Modifier = Modifier,
    label: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = if (checked) AccentGreen.copy(alpha = 0.1f) else SurfaceVariantDark
        ),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (checked) AccentGreen.copy(alpha = 0.5f) else BorderColor
        )
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = AccentGreen,
                    uncheckedColor = TextSecondary,
                    checkmarkColor = BackgroundDark
                ),
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(6.dp))
            Column {
                Icon(icon, null, tint = if (checked) AccentGreen else TextSecondary, modifier = Modifier.size(14.dp))
                Text(label, style = MaterialTheme.typography.labelSmall, color = if (checked) AccentGreen else TextSecondary, fontSize = 10.sp)
            }
        }
    }
}
