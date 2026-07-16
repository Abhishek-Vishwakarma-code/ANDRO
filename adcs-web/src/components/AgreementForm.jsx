import React from 'react';
import '../pages/Calculator.css';

export default function AgreementForm({ agreement, setAgreement }) {
  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setAgreement(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : (name.includes('Amount') || name.includes('Area') || name.includes('Period') ? parseFloat(value) || 0 : value)
    }));
  };

  const rentalOptions = [10000, 15000, 25000, 50000, 100000, 150000];

  return (
    <div className="form-section">
      <h2>📋 Agreement Parameters</h2>

      <div className="section-title">👥 PARTIES & PROPERTY INFORMATION</div>

      <div className="form-grid-2">
        <div className="form-group">
          <label>LICENSOR NAME</label>
          <input
            type="text"
            name="licensorName"
            placeholder="Full Name"
            value={agreement.licensorName}
            onChange={handleChange}
          />
        </div>
        <div className="form-group">
          <label>CONTACT NUMBER</label>
          <input
            type="tel"
            name="licensorContact"
            placeholder="Phone No."
            value={agreement.licensorContact}
            onChange={handleChange}
          />
        </div>
      </div>

      <div className="form-group">
        <label>LICENSOR ADDRESS</label>
        <input
          type="text"
          name="licensorAddress"
          placeholder="Complete Address"
          value={agreement.licensorAddress}
          onChange={handleChange}
        />
      </div>

      <div className="form-grid-2">
        <div className="form-group">
          <label>LICENSEE NAME</label>
          <input
            type="text"
            name="licenseeName"
            placeholder="Full Name"
            value={agreement.licenseeName}
            onChange={handleChange}
          />
        </div>
        <div className="form-group">
          <label>CONTACT NUMBER</label>
          <input
            type="tel"
            name="licenseeContact"
            placeholder="Phone No."
            value={agreement.licenseeContact}
            onChange={handleChange}
          />
        </div>
      </div>

      <div className="form-group">
        <label>LICENSEE ADDRESS</label>
        <input
          type="text"
          name="licenseeAddress"
          placeholder="Complete Address"
          value={agreement.licenseeAddress}
          onChange={handleChange}
        />
      </div>

      <div className="form-grid-2">
        <div className="form-group">
          <label>PROPERTY TYPE</label>
          <div className="button-group">
            {['Residential', 'Commercial'].map(type => (
              <button
                key={type}
                className={`btn-toggle ${agreement.propertyType === type ? 'active' : ''}`}
                onClick={() => setAgreement(prev => ({ ...prev, propertyType: type }))}
              >
                {type}
              </button>
            ))}
          </div>
        </div>
        <div className="form-group">
          <label>LOCATION</label>
          <div className="button-group">
            {['Urban', 'Rural'].map(loc => (
              <button
                key={loc}
                className={`btn-toggle ${agreement.locationArea === loc ? 'active' : ''}`}
                onClick={() => setAgreement(prev => ({ ...prev, locationArea: loc }))}
              >
                {loc}
              </button>
            ))}
          </div>
        </div>
      </div>

      <div className="form-group">
        <label>AREA (SQ.FT)</label>
        <input
          type="number"
          name="propertyArea"
          placeholder="e.g., 650 sq.ft"
          value={agreement.propertyArea}
          onChange={handleChange}
        />
      </div>

      <div className="form-group">
        <label>AVERAGE MONTHLY RENT</label>
        <input
          type="number"
          name="rentAmount"
          placeholder="₹ 0"
          value={agreement.rentAmount}
          onChange={handleChange}
        />
        <div className="quick-select">
          {rentalOptions.map(amount => (
            <button
              key={amount}
              className="quick-btn"
              onClick={() => setAgreement(prev => ({ ...prev, rentAmount: amount }))}
            >
              ₹{(amount / 1000).toFixed(0)}K
            </button>
          ))}
        </div>
      </div>

      <div className="form-grid-2">
        <div className="form-group">
          <label>LICENSE PERIOD</label>
          <input
            type="number"
            name="lockInPeriod"
            placeholder="0"
            value={agreement.lockInPeriod}
            onChange={handleChange}
          />
          <div className="quick-select">
            {[11, 22, 24, 36, 60].map(months => (
              <button
                key={months}
                className="quick-btn"
                onClick={() => setAgreement(prev => ({ ...prev, lockInPeriod: months }))}
              >
                {months}M
              </button>
            ))}
          </div>
        </div>

        <div className="form-group">
          <label>PROPERTY LOCATION</label>
          <div className="button-group">
            {['Urban Area', 'Rural Area'].map(location => (
              <button
                key={location}
                className="btn-toggle"
                onClick={() => {}}
              >
                {location}
              </button>
            ))}
          </div>
        </div>
      </div>

      <div className="section-title">💰 DEPOSITS</div>

      <div className="form-grid-2">
        <div className="form-group">
          <label>REFUNDABLE DEPOSIT</label>
          <input
            type="number"
            name="refundableDeposit"
            placeholder="₹ 0"
            value={agreement.refundableDeposit}
            onChange={handleChange}
          />
          <div className="quick-select">
            {[50000, 100000, 150000, 200000, 500000].map(amount => (
              <button
                key={amount}
                className="quick-btn"
                onClick={() => setAgreement(prev => ({ ...prev, refundableDeposit: amount }))}
              >
                ₹{(amount / 1000).toFixed(0)}K
              </button>
            ))}
          </div>
        </div>

        <div className="form-group">
          <label>NON-REFUNDABLE DEPOSIT</label>
          <input
            type="number"
            name="nonRefundableDeposit"
            placeholder="₹ 0"
            value={agreement.nonRefundableDeposit}
            onChange={handleChange}
          />
          <div className="quick-select">
            {[0, 10000, 25000, 50000].map(amount => (
              <button
                key={amount}
                className="quick-btn"
                onClick={() => setAgreement(prev => ({ ...prev, nonRefundableDeposit: amount }))}
              >
                ₹{amount === 0 ? '0' : (amount / 1000).toFixed(0) + 'K'}
              </button>
            ))}
          </div>
        </div>
      </div>

      <div className="form-group">
        <label>HEAVY DEPOSIT?</label>
        <div className="button-group">
          {['No', 'Yes'].map(option => (
            <button
              key={option}
              className={`btn-toggle ${agreement.heavyDeposit === (option === 'Yes') ? 'active' : ''}`}
              onClick={() => setAgreement(prev => ({ ...prev, heavyDeposit: option === 'Yes' }))}
            >
              {option}
            </button>
          ))}
        </div>
      </div>
    </div>
  );
}
