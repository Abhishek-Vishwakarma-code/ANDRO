import React from 'react';
import './LiveReceipt.css';
import FeesChart from './FeesChart';

export default function LiveReceipt({ agreement, fees }) {
  const displayValue = (val) => val ? `₹${val.toLocaleString('en-IN', { maximumFractionDigits: 0 })}` : '₹0';

  const handlePrintPDF = () => {
    const element = document.getElementById('receipt-content');
    const opt = {
      margin: 10,
      filename: `ADCS_Agreement_${new Date().toLocaleDateString()}.pdf`,
      image: { type: 'jpeg', quality: 0.98 },
      html2canvas: { scale: 2 },
      jsPDF: { orientation: 'portrait', unit: 'mm', format: 'a4' }
    };
    
    import('html2pdf.js').then((html2pdf) => {
      html2pdf.default().set(opt).from(element).save();
    }).catch(err => console.error('PDF error:', err));
  };

  const handleDownloadWord = () => {
    import('docx').then(({ Document, Packer, Paragraph, HeadingLevel }) => {
      const doc = new Document({
        sections: [{
          children: [
            new Paragraph({
              text: 'ADCS - Stamp Duty & Registration Calculator',
              heading: HeadingLevel.HEADING_1,
            }),
            new Paragraph({
              text: `Agreement Details Report - ${new Date().toLocaleDateString()}`,
              style: 'Normal',
            }),
            new Paragraph({ text: '' }),
            new Paragraph({
              text: 'PARTIES INFORMATION',
              heading: HeadingLevel.HEADING_2,
            }),
            new Paragraph(`Licensor: ${agreement.licensorName || 'N/A'}`),
            new Paragraph(`Licensor Contact: ${agreement.licensorContact || 'N/A'}`),
            new Paragraph(`Licensor Address: ${agreement.licensorAddress || 'N/A'}`),
            new Paragraph({ text: '' }),
            new Paragraph(`Licensee: ${agreement.licenseeName || 'N/A'}`),
            new Paragraph(`Licensee Contact: ${agreement.licenseeContact || 'N/A'}`),
            new Paragraph(`Licensee Address: ${agreement.licenseeAddress || 'N/A'}`),
            new Paragraph({ text: '' }),
            new Paragraph({
              text: 'PROPERTY DETAILS',
              heading: HeadingLevel.HEADING_2,
            }),
            new Paragraph(`Property Type: ${agreement.propertyType}`),
            new Paragraph(`Property Area: ${agreement.propertyArea || 'N/A'} Sq. Ft.`),
            new Paragraph(`Location: ${agreement.locationArea}`),
            new Paragraph({ text: '' }),
            new Paragraph({
              text: 'RENTAL DETAILS',
              heading: HeadingLevel.HEADING_2,
            }),
            new Paragraph(`Monthly Rent: ${displayValue(fees.rent)}`),
            new Paragraph(`Lock-in Period: ${agreement.lockInPeriod} months`),
            new Paragraph(`Refundable Deposit: ${displayValue(fees.refundableDeposit)}`),
            new Paragraph(`Non-Refundable Deposit: ${displayValue(fees.nonRefundableDeposit)}`),
            new Paragraph({ text: '' }),
            new Paragraph({
              text: 'CALCULATED FEES',
              heading: HeadingLevel.HEADING_2,
            }),
            new Paragraph(`Stamp Duty (0.25%): ${displayValue(fees.stampDuty)}`),
            new Paragraph(`Registration Fee: ${displayValue(fees.registrationFee)}`),
            new Paragraph(`Professional Services: ${displayValue(fees.professionalServices)}`),
            new Paragraph(`Notional Interest (10% p.a.): ${displayValue(fees.notionalInterest)}`),
            new Paragraph(`GST (18%): ${displayValue(fees.gstTotal)}`),
            new Paragraph({ text: '' }),
            new Paragraph({
              text: `TOTAL PAYABLE: ${displayValue(fees.total)}`,
              bold: true,
              size: 24,
            }),
          ],
        }],
      });

      Packer.toBlob(doc).then(blob => {
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = `ADCS_Agreement_${new Date().toLocaleDateString()}.docx`;
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
      });
    }).catch(err => console.error('Word error:', err));
  };

  const handleCopyText = () => {
    const textContent = `
ADCS - STAMP DUTY & REGISTRATION CALCULATOR
Date: ${new Date().toLocaleDateString()}

PARTIES INFORMATION
Licensor: ${agreement.licensorName || 'N/A'}
Licensor Contact: ${agreement.licensorContact || 'N/A'}
Licensor Address: ${agreement.licensorAddress || 'N/A'}

Licensee: ${agreement.licenseeName || 'N/A'}
Licensee Contact: ${agreement.licenseeContact || 'N/A'}
Licensee Address: ${agreement.licenseeAddress || 'N/A'}

PROPERTY DETAILS
Property Type: ${agreement.propertyType}
Property Area: ${agreement.propertyArea || 'N/A'} Sq. Ft.
Location: ${agreement.locationArea}

RENTAL DETAILS
Monthly Rent: ${displayValue(fees.rent)}
Lock-in Period: ${agreement.lockInPeriod} months
Refundable Deposit: ${displayValue(fees.refundableDeposit)}
Non-Refundable Deposit: ${displayValue(fees.nonRefundableDeposit)}

CALCULATED FEES BREAKDOWN
Stamp Duty (0.25%): ${displayValue(fees.stampDuty)}
Registration Fee: ${displayValue(fees.registrationFee)}
Professional Services: ${displayValue(fees.professionalServices)}
Notional Interest (10% p.a.): ${displayValue(fees.notionalInterest)}
GST (18%): ${displayValue(fees.gstTotal)}

TOTAL PAYABLE: ${displayValue(fees.total)}

Generated by ADCS Calculator - Bombay Stamp Act 1958 (Article 36A) Compliant
    `.trim();

    navigator.clipboard.writeText(textContent).then(() => {
      alert('✅ Summary copied to clipboard!');
    }).catch(() => {
      console.error('Failed to copy');
    });
  };

  return (
    <div className="receipt-section">
      <div className="receipt-header">
        <h2>💰 Live Receipt</h2>
        <span className="badge">● LIVE</span>
      </div>

      <div id="receipt-content">
        <div className="receipt-content">
          <div className="receipt-item">
            <span>Property Type:</span>
            <strong>{agreement.propertyType}</strong>
          </div>
          <div className="receipt-item">
            <span>Lock-in Period:</span>
            <strong>{agreement.lockInPeriod || 0} months</strong>
          </div>
          <div className="receipt-item">
            <span>Monthly Rent:</span>
            <strong>{displayValue(fees.rent)}</strong>
          </div>

          <div className="receipt-divider"></div>

          <div className="receipt-stat">
            <div className="stat-label">Total Consideration</div>
            <div className="stat-value">{displayValue(fees.totalConsideration)}</div>
          </div>

          <div className="receipt-divider"></div>

          <div className="receipt-section-title">Fee Breakdown</div>
          <div className="fees-breakdown">
            <div className="fee-row">
              <span>💵 Stamp Duty (0.25%)</span>
              <span className="amount">{displayValue(fees.stampDuty)}</span>
            </div>
            <div className="fee-row">
              <span>🏛️ Registration Fee</span>
              <span className="amount">{displayValue(fees.registrationFee)}</span>
            </div>
            <div className="fee-row">
              <span>📋 Professional Services</span>
              <span className="amount">{displayValue(fees.professionalServices)}</span>
            </div>
            <div className="fee-row">
              <span>📈 Notional Interest (10% p.a.)</span>
              <span className="amount">{displayValue(fees.notionalInterest)}</span>
            </div>
            <div className="fee-row">
              <span>🧾 GST (18%)</span>
              <span className="amount">{displayValue(fees.gstTotal)}</span>
            </div>
            <div className="fee-row total">
              <span>TOTAL PAYABLE</span>
              <span className="total-amount">{displayValue(fees.total)}</span>
            </div>
          </div>
        </div>

        <div className="receipt-divider"></div>

        {/* Pie Chart */}
        <FeesChart fees={fees} />

        <div className="receipt-divider"></div>

        <div className="receipt-note">
          💡 Note: Total consideration includes rent, deposits, and notional interest on refundable deposits at 10% p.a. for the lock-in period.
        </div>
      </div>

      <div className="action-buttons">
        <button className="btn btn-primary" onClick={handlePrintPDF}>
          📄 PRINT / SAVE DETAILED PDF
        </button>
        <button className="btn btn-secondary" onClick={handleDownloadWord}>
          📝 DOWNLOAD WORD DOCUMENT
        </button>
        <button className="btn btn-tertiary" onClick={handleCopyText}>
          📋 COPY TEXT SUMMARY
        </button>
      </div>
    </div>
  );
}
