package com.example.adcs.ui.main

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.adcs.data.DataRepository
import com.example.adcs.data.CalculationResult
import com.example.adcs.data.PropertyLocation
import com.example.adcs.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummaryScreen(onBack: () -> Unit, onGeneratePdf: () -> Unit) {
    val state by DataRepository.agreementState.collectAsState()
    val result = remember(state) { DataRepository.calculate(state) }
    val amountInWords = remember(result.grandTotal) { DataRepository.amountToWords(result.grandTotal) }

    Scaffold(
        containerColor = BackgroundDark,
        topBar = {
            TopAppBar(
                title = { Text("Agreement Summary", color = TextPrimary, fontWeight = FontWeight.SemiBold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = TextPrimary) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceDark)
            )
        },
        bottomBar = {
            Surface(color = SurfaceDark, tonalElevation = 0.dp) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = onGeneratePdf,
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                    ) {
                        Icon(Icons.Default.PictureAsPdf, null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("PRINT / SAVE DETAILED PDF", fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
                    }
                    OutlinedButton(
                        onClick = {},
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor)
                    ) {
                        Icon(Icons.Default.Article, null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("DOWNLOAD WORD DOCUMENT", color = TextSecondary)
                    }
                    OutlinedButton(
                        onClick = {},
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor)
                    ) {
                        Icon(Icons.Default.ContentCopy, null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("COPY TEXT SUMMARY", color = TextSecondary)
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
            // Grand Total Hero Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = PrimaryContainer),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryPurple.copy(alpha = 0.6f))
            ) {
                Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier.size(8.dp).background(AccentGreen, androidx.compose.foundation.shape.CircleShape)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text("LIVE UPDATING", style = MaterialTheme.typography.labelSmall, color = AccentGreen, letterSpacing = 1.sp)
                    }
                    Spacer(Modifier.height(12.dp))
                    Text("TOTAL PAYABLE ESTIMATE", style = MaterialTheme.typography.labelMedium, color = TextSecondary, letterSpacing = 1.sp)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "₹${"%,.0f".format(result.grandTotal)}/-",
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        amountInWords,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Donut Chart — Financial Share
            DonutChartCard(result = result)

            // Agreement Info
            SectionCard(title = "AGREEMENT PARAMETERS", icon = Icons.Default.Description) {
                ReceiptRow("Property Type", state.property.propertyType.name, AccentGreen)
                ReceiptRow("Lock-in Period", "${state.property.lockInPeriod} Months", TextPrimary)
                ReceiptRow("Licensor", state.parties.licensorName.ifEmpty { "—" }, TextPrimary)
                ReceiptRow("Licensee", state.parties.licenseeName.ifEmpty { "—" }, TextPrimary)
                ReceiptRow("Address", state.property.address.ifEmpty { "—" }, TextSecondary)
                ReceiptRow("Property Area", if (state.property.area.isEmpty()) "—" else "${state.property.area} sq.ft", TextPrimary)
                ReceiptRow("Heavy Deposit", if (state.deposit.hasHeavyDeposit) "Yes" else "No", TextPrimary)
            }

            // Taxable Consideration
            SectionCard(title = "TAXABLE CONSIDERATION", icon = Icons.Default.Calculate) {
                ReceiptRow("Total Rent Consideration", "₹${"%,.0f".format(result.totalRentConsideration)}", TextPrimary)
                Text(
                    "${state.rent.monthlyRent.toInt()} × ${state.rent.licensePeriodMonths} months",
                    style = MaterialTheme.typography.bodySmall, color = TextMuted,
                    modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                )
                ReceiptRow("Non-Refundable Deposit", "₹${"%,.0f".format(result.nonRefundableDeposit)}", TextPrimary)
                ReceiptRow("Notional Interest on Deposit", "₹${"%,.0f".format(result.notionalInterestOnDeposit)}", TextPrimary)
                Text(
                    "10% interest p.a. for ${state.rent.licensePeriodMonths} months",
                    style = MaterialTheme.typography.bodySmall, color = TextMuted,
                    modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                )
                Divider(color = DividerColor, modifier = Modifier.padding(vertical = 8.dp))
                ReceiptRow(
                    "Taxable Base (Consideration)",
                    "₹${"%,.0f".format(result.taxableBase)}",
                    AccentGreen,
                    isBold = true
                )
            }

            // Statutory Fees
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AccountBalance, null, tint = BrandGold, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("STATUTORY FEES (PAYABLE TO GOV)", style = MaterialTheme.typography.labelMedium,
                            color = BrandGold, fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp)
                    }
                    Spacer(Modifier.height(16.dp))
                    ReceiptRowWithSub(
                        label = "Stamp Duty",
                        subLabel = "Bombay Stamp Act Art. 36A (0.25%)\n*Actual Stamp Duty may vary slightly in IGR",
                        value = "₹${"%,.0f".format(result.stampDuty)}/-",
                        valueColor = AccentGreen
                    )
                    Spacer(Modifier.height(12.dp))
                    ReceiptRowWithSub(
                        label = "Registration Fees",
                        subLabel = "Fixed rate for ${if (state.property.location == PropertyLocation.URBAN) "Urban" else "Rural"} area",
                        value = "₹${"%,.0f".format(result.registrationFee)}/-",
                        valueColor = TextPrimary
                    )
                    Spacer(Modifier.height(12.dp))
                    ReceiptRowWithSub(
                        label = "Document Handling",
                        subLabel = "Standard scanning & archival",
                        value = "₹${"%,.0f".format(result.documentHandlingCharges)}/-",
                        valueColor = TextPrimary
                    )
                }
            }

            // Fixed Service Charges
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Business, null, tint = PrimaryPurpleLight, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("FIXED SERVICE CHARGES", style = MaterialTheme.typography.labelMedium,
                            color = PrimaryPurpleLight, fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp)
                    }
                    Spacer(Modifier.height(16.dp))
                    ReceiptRowWithSub("Legal Charges", "Drafting & professions fee (Legal is ₹1000)", "₹${"%,.0f".format(result.legalCharges)}/-")
                    Spacer(Modifier.height(12.dp))
                    ReceiptRowWithSub("Visiting Charges", "On-site biometrics & support (Visiting is ₹499)", "₹${"%,.0f".format(result.visitingCharges)}/-")
                    Spacer(Modifier.height(12.dp))
                    ReceiptRowWithSub("Police Verification", "Fixed government processing fee", "₹${"%,.0f".format(result.policeVerificationCharges)}/-")

                    if (result.enableGst && result.gstAmount > 0) {
                        Spacer(Modifier.height(12.dp))
                        ReceiptRowWithSub("GST (18%)", "On professional service charges", "₹${"%,.0f".format(result.gstAmount)}/-")
                    }
                }
            }

            // Final Total
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = AccentGreen.copy(alpha = 0.08f)),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(2.dp, AccentGreen.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("TOTAL PAYABLE ESTIMATE", style = MaterialTheme.typography.labelMedium, color = TextSecondary, letterSpacing = 1.sp)
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "₹${"%,.0f".format(result.grandTotal)}/-",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = AccentGreen
                        )
                    }
                    Icon(Icons.Default.CheckCircle, null, tint = AccentGreen, modifier = Modifier.size(36.dp))
                }
            }

            // Legal work done by
            if (state.parties.legalWorkBy.isNotEmpty()) {
                Text(
                    "Legal Work Done By: ${state.parties.legalWorkBy}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
fun ReceiptRow(label: String, value: String, valueColor: Color, isBold: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = TextSecondary, modifier = Modifier.weight(1f))
        Text(
            value,
            style = MaterialTheme.typography.bodySmall,
            color = valueColor,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun ReceiptRowWithSub(label: String, subLabel: String, value: String, valueColor: Color = TextPrimary) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
            Text(label, style = MaterialTheme.typography.bodyMedium, color = TextPrimary, fontWeight = FontWeight.Medium)
            Text(subLabel, style = MaterialTheme.typography.bodySmall, color = TextMuted, lineHeight = 16.sp)
        }
        Text(value, style = MaterialTheme.typography.bodyMedium, color = valueColor, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun DonutChartCard(result: CalculationResult) {
    val total = result.grandTotal
    val stampPct = if (total > 0) (result.stampDuty / total).toFloat() else 0f
    val regPct = if (total > 0) ((result.registrationFee + result.documentHandlingCharges) / total).toFloat() else 0f
    val profPct = if (total > 0) ((result.legalCharges + result.visitingCharges + result.policeVerificationCharges) / total).toFloat() else 0f

    val animProgress = remember { Animatable(0f) }
    LaunchedEffect(total) {
        animProgress.animateTo(1f, animationSpec = tween(1200, easing = EaseOutCubic))
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Financial Share Analysis", style = MaterialTheme.typography.titleSmall, color = TextPrimary, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Donut chart
                Box(modifier = Modifier.size(140.dp), contentAlignment = Alignment.Center) {
                    Canvas(modifier = Modifier.size(140.dp)) {
                        val strokeWidth = 24f
                        val diameter = size.minDimension - strokeWidth
                        val topLeft = Offset((size.width - diameter) / 2, (size.height - diameter) / 2)
                        val arcSize = Size(diameter, diameter)
                        val sweepStamp = stampPct * 360f * animProgress.value
                        val sweepReg = regPct * 360f * animProgress.value
                        val sweepProf = profPct * 360f * animProgress.value

                        // Background ring
                        drawArc(Color(0xFF2D2D50), 0f, 360f, false, topLeft = topLeft, size = arcSize, style = Stroke(strokeWidth, cap = StrokeCap.Round))
                        // Stamp duty arc
                        drawArc(ChartStampDuty, -90f, sweepStamp, false, topLeft = topLeft, size = arcSize, style = Stroke(strokeWidth, cap = StrokeCap.Round))
                        // Registration arc
                        drawArc(ChartRegistration, -90f + sweepStamp, sweepReg, false, topLeft = topLeft, size = arcSize, style = Stroke(strokeWidth, cap = StrokeCap.Round))
                        // Professional arc
                        drawArc(ChartProfessional, -90f + sweepStamp + sweepReg, sweepProf, false, topLeft = topLeft, size = arcSize, style = Stroke(strokeWidth, cap = StrokeCap.Round))
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("GRAND", style = MaterialTheme.typography.labelSmall, color = TextMuted, fontSize = 9.sp)
                        Text("TOTAL", style = MaterialTheme.typography.labelSmall, color = TextMuted, fontSize = 9.sp)
                        Text("₹${if (total >= 1000) "%.1fK".format(total / 1000) else "%.0f".format(total)}", style = MaterialTheme.typography.titleSmall, color = TextPrimary, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(Modifier.width(20.dp))

                // Legend
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    ChartLegendItem(color = ChartStampDuty, label = "Stamp Duty", pct = (stampPct * 100).toInt())
                    ChartLegendItem(color = ChartRegistration, label = "Registration Fees", pct = (regPct * 100).toInt())
                    ChartLegendItem(color = ChartProfessional, label = "Professional Services", pct = (profPct * 100).toInt())
                }
            }
        }
    }
}

@Composable
fun ChartLegendItem(color: Color, label: String, pct: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(10.dp).background(color, androidx.compose.foundation.shape.CircleShape))
        Spacer(Modifier.width(8.dp))
        Text(label, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        Spacer(Modifier.width(4.dp))
        Text("$pct%", style = MaterialTheme.typography.bodySmall, color = TextPrimary, fontWeight = FontWeight.SemiBold)
    }
}
