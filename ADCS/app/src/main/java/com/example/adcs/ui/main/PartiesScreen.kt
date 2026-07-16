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
import com.example.adcs.data.PartiesInfo
import com.example.adcs.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartiesScreen(onBack: () -> Unit, onNext: () -> Unit) {
    val state by DataRepository.agreementState.collectAsState()
    var parties by remember { mutableStateOf(state.parties) }

    Scaffold(
        containerColor = BackgroundDark,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Parties Information", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                        Text("Step 1 of 5", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = TextPrimary) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceDark),
                actions = {
                    TextButton(onClick = {
                        DataRepository.updateParties(parties)
                        onNext()
                    }) { Text("Next →", color = PrimaryPurpleLight, fontWeight = FontWeight.SemiBold) }
                }
            )
        },
        bottomBar = {
            Surface(color = SurfaceDark, tonalElevation = 0.dp) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Step Progress
                    StepProgressBar(currentStep = 1, totalSteps = 5)
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = { DataRepository.updateParties(parties); onNext() },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                    ) {
                        Text("Continue to Property →", fontWeight = FontWeight.Bold)
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
            // Licensor Section
            SectionCard(title = "LICENSOR (Owner)", icon = Icons.Default.Home) {
                AdcsTextField(label = "Full Name", value = parties.licensorName, leadingIcon = Icons.Default.Person,
                    onValueChange = { parties = parties.copy(licensorName = it) })
                Spacer(Modifier.height(12.dp))
                AdcsTextField(label = "Contact Number", value = parties.licensorContact, leadingIcon = Icons.Default.Phone,
                    keyboardType = KeyboardType.Phone, onValueChange = { parties = parties.copy(licensorContact = it) })
                Spacer(Modifier.height(12.dp))
                AdcsTextField(label = "Complete Address", value = parties.licensorAddress, leadingIcon = Icons.Default.LocationOn,
                    onValueChange = { parties = parties.copy(licensorAddress = it) })
                Spacer(Modifier.height(12.dp))
                AdcsTextField(label = "Aadhaar No. / PAN", value = parties.licensorAadhaarPan, leadingIcon = Icons.Default.Badge,
                    onValueChange = { parties = parties.copy(licensorAadhaarPan = it) })
            }

            // Licensee Section
            SectionCard(title = "LICENSEE (Tenant)", icon = Icons.Default.PersonPin) {
                AdcsTextField(label = "Full Name", value = parties.licenseeName, leadingIcon = Icons.Default.Person,
                    onValueChange = { parties = parties.copy(licenseeName = it) })
                Spacer(Modifier.height(12.dp))
                AdcsTextField(label = "Contact Number", value = parties.licenseeContact, leadingIcon = Icons.Default.Phone,
                    keyboardType = KeyboardType.Phone, onValueChange = { parties = parties.copy(licenseeContact = it) })
                Spacer(Modifier.height(12.dp))
                AdcsTextField(label = "Complete Address", value = parties.licenseeAddress, leadingIcon = Icons.Default.LocationOn,
                    onValueChange = { parties = parties.copy(licenseeAddress = it) })
                Spacer(Modifier.height(12.dp))
                AdcsTextField(label = "Aadhaar No. / PAN", value = parties.licenseeAadhaarPan, leadingIcon = Icons.Default.Badge,
                    onValueChange = { parties = parties.copy(licenseeAadhaarPan = it) })
            }

            // Witnesses Section
            SectionCard(title = "WITNESSES", icon = Icons.Default.Group) {
                AdcsTextField(label = "Witness 1 — Name, Address, Aadhaar/PAN", value = parties.witness1,
                    leadingIcon = Icons.Default.Person, onValueChange = { parties = parties.copy(witness1 = it) })
                Spacer(Modifier.height(12.dp))
                AdcsTextField(label = "Witness 2 — Name, Address, Aadhaar/PAN", value = parties.witness2,
                    leadingIcon = Icons.Default.Person, onValueChange = { parties = parties.copy(witness2 = it) })
            }

            // Legal Work By
            SectionCard(title = "LEGAL WORK", icon = Icons.Default.Gavel) {
                AdcsTextField(label = "Lawyer, Executive, or Firm Name", value = parties.legalWorkBy,
                    leadingIcon = Icons.Default.AccountBalance, onValueChange = { parties = parties.copy(legalWorkBy = it) })
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
fun SectionCard(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, null, tint = PrimaryPurpleLight, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(title, style = MaterialTheme.typography.labelMedium, color = PrimaryPurpleLight,
                    fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp)
            }
            Spacer(Modifier.height(16.dp))
            content()
        }
    }
}

@Composable
fun AdcsTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        label = { Text(label, color = TextMuted, style = MaterialTheme.typography.bodySmall) },
        leadingIcon = leadingIcon?.let { { Icon(it, null, tint = TextSecondary, modifier = Modifier.size(18.dp)) } },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PrimaryPurple,
            unfocusedBorderColor = InputBorder,
            focusedContainerColor = InputBackground,
            unfocusedContainerColor = InputBackground,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary,
            focusedLabelColor = PrimaryPurpleLight,
            unfocusedLabelColor = TextMuted
        ),
        shape = RoundedCornerShape(12.dp),
        singleLine = true
    )
}

@Composable
fun StepProgressBar(currentStep: Int, totalSteps: Int) {
    val stepLabels = listOf("Parties", "Property", "Rent", "Deposit", "Docs")
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
        stepLabels.forEachIndexed { index, label ->
            val stepNum = index + 1
            val isDone = stepNum < currentStep
            val isCurrent = stepNum == currentStep
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .height(4.dp)
                        .fillMaxWidth()
                        .background(
                            when {
                                isDone -> AccentGreen
                                isCurrent -> PrimaryPurple
                                else -> BorderColor
                            },
                            RoundedCornerShape(2.dp)
                        )
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    label,
                    style = MaterialTheme.typography.labelSmall,
                    color = when {
                        isDone -> AccentGreen
                        isCurrent -> PrimaryPurpleLight
                        else -> TextMuted
                    },
                    fontSize = 9.sp
                )
            }
        }
    }
}
