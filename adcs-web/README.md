# ADCS Stamp Duty & Registration Calculator

A comprehensive web and mobile application for calculating stamp duty and registration fees for Leave and License agreements under the Bombay Stamp Act, 1958 (Article 36A) applicable in Maharashtra.

## Features

- **Live Receipt Calculation**: Real-time updates as you input agreement details
- **Comprehensive Fee Breakdown**: Stamp duty, registration fees, GST, and notional interest
- **Document Checklist**: Track required documents for the agreement
- **Property Type Support**: Residential, Commercial, Urban, Rural
- **Mobile & Web**: Works seamlessly on both Android (via Capacitor) and web browsers
- **PDF Export**: Generate detailed PDF agreements
- **Multi-platform**: Single codebase for web, Android, and iOS

## Getting Started

### Installation

```bash
cd adcs-web
npm install
```

### Development

```bash
npm run dev
```

Opens at `http://localhost:5173`

### Build

```bash
npm run build
```

## Project Structure

```
adcs-web/
├── src/
│   ├── pages/
│   │   └── Calculator.jsx      # Main calculator page
│   ├── components/
│   │   ├── AgreementForm.jsx   # Form for agreement details
│   │   ├── LiveReceipt.jsx     # Live fee calculation display
│   │   └── DocumentChecklist.jsx # Document tracking
│   ├── main.jsx
│   └── index.css
├── public/
│   └── index.html
├── package.json
└── vite.config.js
```

## Technology Stack

- **React 18**: UI framework
- **Vite**: Build tool
- **Capacitor**: Cross-platform mobile bridge
- **Recharts**: Data visualization (planned)

## Calculation Logic

### Stamp Duty
- Calculated at **0.25%** of total consideration
- Total consideration = Rent + Non-refundable deposits + Notional interest

### Notional Interest
- **10% per annum** (simple interest) on refundable deposits
- Calculated for the license period duration

### Registration Fees
- **Urban**: ₹1,000
- **Rural**: ₹500
- **Commercial**: ₹2,000

### GST
- **18%** on stamp duty and professional services

## Legal Compliance

This calculator is based on:
- **Bombay Stamp Act, 1958** - Article 36A
- **Maharashtra Tax & Law Department** regulations
- **Current stamp duty rates** effective in Maharashtra

## License

Proprietary - AD Consultant & Service
