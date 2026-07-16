import React, { useState } from 'react';
import './Calculator.css';
import AgreementForm from '../components/AgreementForm';
import LiveReceipt from '../components/LiveReceipt';
import DocumentChecklist from '../components/DocumentChecklist';

export default function Calculator() {
  const [agreement, setAgreement] = useState({
    licensorName: '',
    licensorContact: '',
    licensorAddress: '',
    licenseeName: '',
    licenseeContact: '',
    licenseeAddress: '',
    propertyType: 'Residential',
    propertyArea: '',
    locationArea: 'Urban',
    rentAmount: 0,
    lockInPeriod: 0,
    refundableDeposit: 0,
    nonRefundableDeposit: 0,
    heavyDeposit: false,
    witnessed: false,
  });

  const calculateFees = () => {
    const rent = parseFloat(agreement.rentAmount) || 0;
    const refundableDeposit = parseFloat(agreement.refundableDeposit) || 0;
    const nonRefundableDeposit = parseFloat(agreement.nonRefundableDeposit) || 0;
    const lockInPeriod = parseFloat(agreement.lockInPeriod) || 12;
    const location = agreement.locationArea || 'Urban';
    const propertyType = agreement.propertyType || 'Residential';

    // Notional interest calculation
    const notionalInterest = (refundableDeposit * 0.10 * lockInPeriod) / 12;
    const totalConsideration = rent + nonRefundableDeposit + notionalInterest;

    // Stamp Duty (0.25% of consideration)
    const stampDuty = totalConsideration * 0.0025;

    // Registration Fees based on property type and location
    let registrationFee = 0;
    if (propertyType === 'Commercial') {
      registrationFee = 2000;
    } else if (location === 'Rural') {
      registrationFee = 500;
    } else {
      registrationFee = 1000;
    }

    // Professional services (document handling + police verification)
    const documentHandling = 300;
    const policeVerification = 200;
    const professionalServices = documentHandling + policeVerification;

    // Taxable amount for GST
    const applicableForGST = stampDuty + registrationFee + professionalServices;
    const gstTotal = applicableForGST * 0.18;

    // Total
    const total = rent + refundableDeposit + nonRefundableDeposit + stampDuty + registrationFee + professionalServices + gstTotal;

    return {
      rent,
      refundableDeposit,
      nonRefundableDeposit,
      notionalInterest,
      totalConsideration,
      stampDuty,
      registrationFee,
      documentHandling,
      policeVerification,
      professionalServices,
      applicableForGST,
      gstTotal,
      total,
    };
  };

  const fees = calculateFees();

  return (
    <div className="calculator-container">
      <div className="header">
        <div className="logo">📋</div>
        <h1 className="title">ADCS Stamp Duty Calculator</h1>
        <p className="subtitle">Professional <strong>Leave & License Agreement</strong> Fee Calculator</p>
        <p className="subtitle-small">Bombay Stamp Act, 1958 - Article 36A Compliant</p>
      </div>

      <div className="main-content">
        <div className="grid-2">
          <AgreementForm agreement={agreement} setAgreement={setAgreement} />
          <LiveReceipt agreement={agreement} fees={fees} />
        </div>

        <DocumentChecklist agreement={agreement} setAgreement={setAgreement} />
      </div>
    </div>
  );
}
