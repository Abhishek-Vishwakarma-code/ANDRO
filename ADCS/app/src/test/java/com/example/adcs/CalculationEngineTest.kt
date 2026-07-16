package com.example.adcs

import com.example.adcs.data.*
import org.junit.Assert.assertEquals
import org.junit.Test

class CalculationEngineTest {

    @Test
    fun testUrbanResidentialCalculations() {
        val state = AgreementState(
            rent = RentInfo(monthlyRent = 20000.0, licensePeriodMonths = 22),
            deposit = DepositInfo(refundableDeposit = 100000.0, nonRefundableDeposit = 0.0),
            property = PropertyInfo(location = PropertyLocation.URBAN),
            enableGst = false
        )

        val result = DataRepository.calculate(state)

        // 1. Total Rent Consideration = 20,000 * 22 = 440,000
        assertEquals(440000.0, result.totalRentConsideration, 0.01)

        // 2. Notional Interest on Refundable Deposit = 100,000 * 10% * (22/12) = 18,333.33
        assertEquals(18333.33, result.notionalInterestOnDeposit, 0.05)

        // 3. Taxable Base = 440,000 + 0 + 18,333.33 = 458,333.33
        assertEquals(458333.33, result.taxableBase, 0.05)

        // 4. Stamp Duty = 0.25% of 458,333.33 = 1145.83, which is > 500
        assertEquals(1145.83, result.stampDuty, 0.05)

        // 5. Urban Registration Fee = 1000
        assertEquals(1000.0, result.registrationFee, 0.01)

        // 6. Service Charges = 1000 + 499 + 200 = 1699
        assertEquals(1699.0, result.legalCharges + result.visitingCharges + result.policeVerificationCharges, 0.01)

        // 7. Grand Total = Stamp Duty (1145.83) + Registration (1000) + Doc Handling (300) + Service Charges (1699) + GST (0) = 4144.83
        assertEquals(4144.83, result.grandTotal, 0.05)
    }

    @Test
    fun testRuralZeroRentCalculations() {
        // Zero rent with deposit only (heavy deposit edge case)
        val state = AgreementState(
            rent = RentInfo(monthlyRent = 0.0, licensePeriodMonths = 12),
            deposit = DepositInfo(refundableDeposit = 500000.0, nonRefundableDeposit = 10000.0),
            property = PropertyInfo(location = PropertyLocation.RURAL),
            enableGst = false
        )

        val result = DataRepository.calculate(state)

        assertEquals(0.0, result.totalRentConsideration, 0.01)
        // Notional Interest = 500,000 * 10% * 1 = 50,000
        assertEquals(50000.0, result.notionalInterestOnDeposit, 0.01)
        // Taxable Base = 10,000 + 50,000 = 60,000
        assertEquals(60000.0, result.taxableBase, 0.01)
        // Stamp Duty = 0.25% of 60,000 = 150 -> Less than minimum 500, so should be 500
        assertEquals(500.0, result.stampDuty, 0.01)
        // Rural Registration Fee = 500
        assertEquals(500.0, result.registrationFee, 0.01)
    }

    @Test
    fun testGstBillingEnabled() {
        val state = AgreementState(
            rent = RentInfo(monthlyRent = 15000.0, licensePeriodMonths = 11),
            deposit = DepositInfo(refundableDeposit = 50000.0, nonRefundableDeposit = 0.0),
            property = PropertyInfo(location = PropertyLocation.URBAN),
            enableGst = true
        )

        val result = DataRepository.calculate(state)

        // Service charges = 1000 + 499 + 200 = 1699
        // GST (18%) = 1699 * 0.18 = 305.82
        assertEquals(305.82, result.gstAmount, 0.01)
    }

    @Test
    fun testAmountToWordsConversion() {
        assertEquals("FOUR THOUSAND ONE HUNDRED AND FORTY FOUR RUPEES ONLY", DataRepository.amountToWords(4144.0))
        assertEquals("FIVE HUNDRED RUPEES ONLY", DataRepository.amountToWords(500.0))
    }
}
