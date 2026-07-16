package com.example.adcs.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Agreement domain models
data class PartiesInfo(
    val licensorName: String = "",
    val licensorContact: String = "",
    val licensorAddress: String = "",
    val licensorAadhaarPan: String = "",
    val licenseeName: String = "",
    val licenseeContact: String = "",
    val licenseeAddress: String = "",
    val licenseeAadhaarPan: String = "",
    val witness1: String = "",
    val witness2: String = "",
    val legalWorkBy: String = ""
)

data class PropertyInfo(
    val propertyType: PropertyType = PropertyType.RESIDENTIAL,
    val location: PropertyLocation = PropertyLocation.URBAN,
    val area: String = "",
    val address: String = "",
    val lockInPeriod: Int = 0
)

enum class PropertyType { RESIDENTIAL, COMMERCIAL }
enum class PropertyLocation { URBAN, RURAL }

data class RentInfo(
    val monthlyRent: Double = 0.0,
    val licensePeriodMonths: Int = 0,
    val escalationType: EscalationType = EscalationType.FIXED
)

enum class EscalationType { FIXED, VARYING }

data class DepositInfo(
    val refundableDeposit: Double = 0.0,
    val nonRefundableDeposit: Double = 0.0,
    val hasHeavyDeposit: Boolean = false
)

data class DocumentsInfo(
    val hasAadhaar: Boolean = false,
    val hasPan: Boolean = false,
    val hasElectricityBill: Boolean = false,
    val hasIndexII: Boolean = false,
    val hasRegisteredDeed: Boolean = false,
    val hasPropertyTax: Boolean = false,
    val licensorAadhaarPanText: String = "",
    val licenseeAadhaarPanText: String = "",
    val customDocuments: List<String> = emptyList()
)

data class AgreementState(
    val parties: PartiesInfo = PartiesInfo(),
    val property: PropertyInfo = PropertyInfo(),
    val rent: RentInfo = RentInfo(),
    val deposit: DepositInfo = DepositInfo(),
    val documents: DocumentsInfo = DocumentsInfo(),
    val enableGst: Boolean = false
)

// Calculation result
data class CalculationResult(
    val totalRentConsideration: Double = 0.0,
    val nonRefundableDeposit: Double = 0.0,
    val notionalInterestOnDeposit: Double = 0.0,
    val taxableBase: Double = 0.0,
    val stampDuty: Double = 0.0,
    val registrationFee: Double = 0.0,
    val documentHandlingCharges: Double = 300.0,
    val legalCharges: Double = 1000.0,
    val visitingCharges: Double = 499.0,
    val policeVerificationCharges: Double = 200.0,
    val gstAmount: Double = 0.0,
    val grandTotal: Double = 2999.0,
    val enableGst: Boolean = false
)

// In-memory data repository (replace with Room later)
object DataRepository {
    private val _agreementState = MutableStateFlow(AgreementState())
    val agreementState: StateFlow<AgreementState> = _agreementState.asStateFlow()

    fun updateParties(parties: PartiesInfo) {
        _agreementState.value = _agreementState.value.copy(parties = parties)
    }

    fun updateProperty(property: PropertyInfo) {
        _agreementState.value = _agreementState.value.copy(property = property)
    }

    fun updateRent(rent: RentInfo) {
        _agreementState.value = _agreementState.value.copy(rent = rent)
    }

    fun updateDeposit(deposit: DepositInfo) {
        _agreementState.value = _agreementState.value.copy(deposit = deposit)
    }

    fun updateDocuments(documents: DocumentsInfo) {
        _agreementState.value = _agreementState.value.copy(documents = documents)
    }

    fun updateGst(enabled: Boolean) {
        _agreementState.value = _agreementState.value.copy(enableGst = enabled)
    }

    fun resetAgreement() {
        _agreementState.value = AgreementState()
    }

    // Stamp duty calculation engine (Bombay Stamp Act, 1958 - Article 36A)
    fun calculate(state: AgreementState): CalculationResult {
        val rent = state.rent
        val deposit = state.deposit
        val property = state.property

        val totalRent = rent.monthlyRent * rent.licensePeriodMonths
        val notionalInterest = deposit.refundableDeposit * 0.10 * (rent.licensePeriodMonths / 12.0)
        val taxableBase = totalRent + deposit.nonRefundableDeposit + notionalInterest
        val rawStampDuty = taxableBase * 0.0025
        val stampDuty = maxOf(rawStampDuty, 500.0)
        val registrationFee = if (property.location == PropertyLocation.URBAN) 1000.0 else 500.0
        val serviceCharges = 1000.0 + 499.0 + 200.0
        val gstAmount = if (state.enableGst) serviceCharges * 0.18 else 0.0
        val grandTotal = stampDuty + registrationFee + 300.0 + serviceCharges + gstAmount

        return CalculationResult(
            totalRentConsideration = totalRent,
            nonRefundableDeposit = deposit.nonRefundableDeposit,
            notionalInterestOnDeposit = notionalInterest,
            taxableBase = taxableBase,
            stampDuty = stampDuty,
            registrationFee = registrationFee,
            documentHandlingCharges = 300.0,
            legalCharges = 1000.0,
            visitingCharges = 499.0,
            policeVerificationCharges = 200.0,
            gstAmount = gstAmount,
            grandTotal = grandTotal,
            enableGst = state.enableGst
        )
    }

    fun amountToWords(amount: Double): String {
        val rupees = amount.toLong()
        return "${convertNumber(rupees)} RUPEES ONLY"
    }

    private val ones = arrayOf("", "ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE",
        "TEN", "ELEVEN", "TWELVE", "THIRTEEN", "FOURTEEN", "FIFTEEN", "SIXTEEN", "SEVENTEEN", "EIGHTEEN", "NINETEEN")
    private val tens = arrayOf("", "", "TWENTY", "THIRTY", "FORTY", "FIFTY", "SIXTY", "SEVENTY", "EIGHTY", "NINETY")

    private fun convertNumber(n: Long): String = when {
        n == 0L -> "ZERO"
        n < 20 -> ones[n.toInt()]
        n < 100 -> tens[(n / 10).toInt()] + if (n % 10 != 0L) " ${ones[(n % 10).toInt()]}" else ""
        n < 1000 -> ones[(n / 100).toInt()] + " HUNDRED" + if (n % 100 != 0L) " AND ${convertNumber(n % 100)}" else ""
        n < 100000 -> convertNumber(n / 1000) + " THOUSAND" + if (n % 1000 != 0L) " ${convertNumber(n % 1000)}" else ""
        n < 10000000 -> convertNumber(n / 100000) + " LAKH" + if (n % 100000 != 0L) " ${convertNumber(n % 100000)}" else ""
        else -> convertNumber(n / 10000000) + " CRORE" + if (n % 10000000 != 0L) " ${convertNumber(n % 10000000)}" else ""
    }
}
