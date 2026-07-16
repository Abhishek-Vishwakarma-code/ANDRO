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
import com.example.adcs.data.PropertyInfo
import com.example.adcs.data.PropertyLocation
import com.example.adcs.data.PropertyType
import com.example.adcs.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertyScreen(onBack: () -> Unit, onNext: () -> Unit) {
    val state by DataRepository.agreementState.collectAsState()
    var property by remember { mutableStateOf(state.property) }

    Scaffold(
        containerColor = BackgroundDark,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Property Details", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                        Text("Step 2 of 5", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = TextPrimary) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceDark)
            )
        },
        bottomBar = {
            Surface(color = SurfaceDark, tonalElevation = 0.dp) {
                Column(modifier = Modifier.padding(16.dp)) {
                    StepProgressBar(currentStep = 2, totalSteps = 5)
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = { DataRepository.updateProperty(property); onNext() },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                    ) {
                        Text("Continue to Rent →", fontWeight = FontWeight.Bold)
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
            // Property Type Toggle
            SectionCard(title = "PROPERTY TYPE", icon = Icons.Default.Apartment) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ToggleChip(
                        label = "Residential",
                        icon = Icons.Default.Home,
                        selected = property.propertyType == PropertyType.RESIDENTIAL,
                        modifier = Modifier.weight(1f),
                        onClick = { property = property.copy(propertyType = PropertyType.RESIDENTIAL) }
                    )
                    ToggleChip(
                        label = "Commercial",
                        icon = Icons.Default.Business,
                        selected = property.propertyType == PropertyType.COMMERCIAL,
                        modifier = Modifier.weight(1f),
                        onClick = { property = property.copy(propertyType = PropertyType.COMMERCIAL) }
                    )
                }
            }

            // Location Toggle
            SectionCard(title = "PROPERTY LOCATION", icon = Icons.Default.LocationOn) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ToggleChip(
                        label = "Urban Area",
                        icon = Icons.Default.LocationCity,
                        selected = property.location == PropertyLocation.URBAN,
                        modifier = Modifier.weight(1f),
                        onClick = { property = property.copy(location = PropertyLocation.URBAN) }
                    )
                    ToggleChip(
                        label = "Rural Area",
                        icon = Icons.Default.NaturePeople,
                        selected = property.location == PropertyLocation.RURAL,
                        modifier = Modifier.weight(1f),
                        onClick = { property = property.copy(location = PropertyLocation.RURAL) }
                    )
                }
                Spacer(Modifier.height(8.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (property.location == PropertyLocation.URBAN) PrimaryContainer.copy(alpha = 0.5f)
                        else AccentGreen.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        if (property.location == PropertyLocation.URBAN)
                            "Urban: Registration Fee ₹1,000/-"
                        else
                            "Rural: Registration Fee ₹500/-",
                        modifier = Modifier.padding(10.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }

            // Address & Area
            SectionCard(title = "PROPERTY DETAILS", icon = Icons.Default.Home) {
                AdcsTextField(
                    label = "Flat No, Floor, Wing, Area, City",
                    value = property.address,
                    leadingIcon = Icons.Default.LocationOn,
                    onValueChange = { property = property.copy(address = it) }
                )
                Spacer(Modifier.height(12.dp))
                AdcsTextField(
                    label = "Property Area (sq.ft)",
                    value = property.area,
                    leadingIcon = Icons.Default.SquareFoot,
                    keyboardType = KeyboardType.Number,
                    onValueChange = { property = property.copy(area = it) }
                )
            }

            // Lock-in Period
            SectionCard(title = "LOCK-IN PERIOD", icon = Icons.Default.Lock) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = if (property.lockInPeriod == 0) "" else property.lockInPeriod.toString(),
                        onValueChange = { property = property.copy(lockInPeriod = it.toIntOrNull() ?: 0) },
                        modifier = Modifier.weight(1f),
                        label = { Text("Lock-in (Months)", color = TextMuted, style = MaterialTheme.typography.bodySmall) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryPurple, unfocusedBorderColor = InputBorder,
                            focusedContainerColor = InputBackground, unfocusedContainerColor = InputBackground,
                            focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                }
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(6, 11, 12, 24).forEach { months ->
                        FilterChip(
                            selected = property.lockInPeriod == months,
                            onClick = { property = property.copy(lockInPeriod = months) },
                            label = { Text("${months}M") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PrimaryPurple,
                                selectedLabelColor = TextPrimary,
                                containerColor = SurfaceVariantDark,
                                labelColor = TextSecondary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true, selected = property.lockInPeriod == months,
                                selectedBorderColor = PrimaryPurple, borderColor = InputBorder
                            )
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
fun ToggleChip(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, selected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val bgColor = if (selected) PrimaryPurple else SurfaceVariantDark
    val textColor = if (selected) TextPrimary else TextSecondary
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Icon(icon, null, tint = textColor, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text(label, color = textColor, fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
