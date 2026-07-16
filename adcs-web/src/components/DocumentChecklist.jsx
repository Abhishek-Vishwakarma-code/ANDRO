import React, { useState } from 'react';
import './DocumentChecklist.css';

export default function DocumentChecklist({ agreement, setAgreement }) {
  const [uploadedDocs, setUploadedDocs] = useState({
    aadhaarCard: null,
    panCard: null,
    electricityBill: null,
    indexII: null,
    registeredDeed: null,
    propertyTaxReceipt: null,
  });

  const [customDocs, setCustomDocs] = useState([]);
  const [customDocInput, setCustomDocInput] = useState('');
  const [legalWork, setLegalWork] = useState('');

  const handleFileUpload = (docKey, event) => {
    const file = event.target.files?.[0];
    if (file) {
      setUploadedDocs(prev => ({
        ...prev,
        [docKey]: {
          name: file.name,
          size: file.size,
          type: file.type,
          uploadDate: new Date().toLocaleDateString()
        }
      }));
    }
  };

  const handleAddCustomDoc = () => {
    if (customDocInput.trim()) {
      setCustomDocs(prev => [...prev, {
        id: Date.now(),
        name: customDocInput,
        file: null
      }]);
      setCustomDocInput('');
    }
  };

  const handleCustomDocUpload = (docId, event) => {
    const file = event.target.files?.[0];
    if (file) {
      setCustomDocs(prev => prev.map(doc => 
        doc.id === docId ? {
          ...doc,
          file: {
            name: file.name,
            size: file.size,
            type: file.type,
            uploadDate: new Date().toLocaleDateString()
          }
        } : doc
      ));
    }
  };

  const documents = [
    { key: 'aadhaarCard', label: 'Aadhaar Card' },
    { key: 'panCard', label: 'PAN Card' },
    { key: 'electricityBill', label: 'Electricity Bill' },
    { key: 'indexII', label: 'Index II' },
    { key: 'registeredDeed', label: 'Registered Deed' },
    { key: 'propertyTaxReceipt', label: 'Property Tax Receipt' },
  ];

  return (
    <div className="checklist-section">
      <h2>📚 Calculation Rules & Document Checklist</h2>
      <p>Everything you need to know about Leave and License agreement stamp duty in Maharashtra.</p>

      <div className="faq-grid">
        <details className="faq-item">
          <summary>How is Stamp Duty calculated?</summary>
          <div className="faq-content">
            <p>The stamp duty is calculated at <strong>0.25%</strong> of the total consideration. This is calculated on the sum of total rent, non-refundable deposits, and notional interest to form the "Total Consideration".</p>
          </div>
        </details>

        <details className="faq-item">
          <summary>What are the Registration Fees?</summary>
          <div className="faq-content">
            <p><strong>Urban Area:</strong> ₹1,000</p>
            <p><strong>Rural Area:</strong> ₹500</p>
            <p><strong>Commercial Property:</strong> ₹2,000</p>
            <p>Registration fees are fixed government processing fees based on location and property type.</p>
          </div>
        </details>

        <details className="faq-item">
          <summary>What is Notional Interest?</summary>
          <div className="faq-content">
            <p>The state government assumes that if a landlord receives a refundable security deposit, they will earn interest on it. Therefore, a notional interest rate of <strong>10% per annum (simple interest)</strong> for the refundable security deposit for the duration of the license period is calculated and added to the total consideration.</p>
          </div>
        </details>

        <details className="faq-item">
          <summary>Is GST included?</summary>
          <div className="faq-content">
            <p>Yes. GST at <strong>18%</strong> is applied on stamp duty, registration fees, and professional services (document handling + police verification charges).</p>
          </div>
        </details>
      </div>

      <div className="documents-checklist">
        <h3>📄 DOCUMENTS PROVIDED CHECKLIST</h3>
        <div className="checklist-grid">
          {documents.map(doc => (
            <div key={doc.key} className="checklist-item">
              <div className="checkbox-wrapper">
                <input
                  type="checkbox"
                  id={doc.key}
                  checked={!!uploadedDocs[doc.key]}
                  onChange={() => {}}
                  disabled
                />
                <label htmlFor={doc.key}>{doc.label}</label>
              </div>
              <label className="upload-btn">
                📤 {uploadedDocs[doc.key] ? '✓ Uploaded' : 'Upload'}
                <input
                  type="file"
                  style={{ display: 'none' }}
                  onChange={(e) => handleFileUpload(doc.key, e)}
                  accept=".pdf,.jpg,.jpeg,.png,.doc,.docx"
                />
              </label>
              {uploadedDocs[doc.key] && (
                <div style={{ fontSize: '0.75rem', color: '#22c55e', marginTop: '4px' }}>
                  {uploadedDocs[doc.key].name}
                </div>
              )}
            </div>
          ))}
        </div>

        <div className="custom-docs">
          <h4>➕ Add Custom Document</h4>
          <div className="custom-input">
            <input 
              type="text" 
              placeholder="e.g. NOC, Maintenance Receipt, Affidavit" 
              value={customDocInput}
              onChange={(e) => setCustomDocInput(e.target.value)}
              onKeyPress={(e) => e.key === 'Enter' && handleAddCustomDoc()}
            />
            <button className="btn-add" onClick={handleAddCustomDoc}>+ Add</button>
          </div>

          {customDocs.length > 0 && (
            <div style={{ marginTop: '15px' }}>
              {customDocs.map(doc => (
                <div key={doc.id} style={{ 
                  display: 'flex', 
                  gap: '10px', 
                  marginBottom: '10px',
                  padding: '10px',
                  background: 'rgba(255,255,255,0.05)',
                  borderRadius: '6px'
                }}>
                  <span style={{ flex: 1 }}>{doc.name}</span>
                  <label className="upload-btn" style={{ margin: 0 }}>
                    📤 {doc.file ? '✓' : 'Upload'}
                    <input
                      type="file"
                      style={{ display: 'none' }}
                      onChange={(e) => handleCustomDocUpload(doc.id, e)}
                      accept=".pdf,.jpg,.jpeg,.png,.doc,.docx"
                    />
                  </label>
                </div>
              ))}
            </div>
          )}
        </div>

        <div className="legal-handled">
          <h4>⚖️ LEGAL WORK HANDLED BY</h4>
          <input 
            type="text" 
            placeholder="Lawyer, Executive, or Firm Name" 
            value={legalWork}
            onChange={(e) => setLegalWork(e.target.value)}
          />
        </div>

        <div className="legal-handled" style={{ borderTop: '1px solid rgba(255,255,255,0.1)', paddingTop: '15px', marginTop: '15px' }}>
          <h4>📊 UPLOAD STATUS SUMMARY</h4>
          <div style={{ fontSize: '0.9rem', color: '#b4bfc9', lineHeight: '1.8' }}>
            <p>✅ Standard Documents: <strong style={{ color: '#22c55e' }}>{Object.values(uploadedDocs).filter(Boolean).length}/{documents.length}</strong></p>
            <p>✅ Custom Documents: <strong style={{ color: '#22c55e' }}>{customDocs.filter(d => d.file).length}/{customDocs.length}</strong></p>
            <p>⚖️ Legal Handler: <strong style={{ color: legalWork ? '#22c55e' : '#f97316' }}>{legalWork || 'Not specified'}</strong></p>
          </div>
        </div>
      </div>
    </div>
  );
}
