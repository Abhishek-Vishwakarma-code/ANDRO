import express from 'express';
import cors from 'cors';
import multer from 'multer';
import path from 'path';
import fs from 'fs';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const app = express();
const PORT = 3001;

// Middleware
app.use(cors());
app.use(express.json());

// Create uploads directory if it doesn't exist
const uploadsDir = path.join(__dirname, 'uploads');
if (!fs.existsSync(uploadsDir)) {
  fs.mkdirSync(uploadsDir, { recursive: true });
}

// Multer configuration
const storage = multer.diskStorage({
  destination: (req, file, cb) => {
    cb(null, uploadsDir);
  },
  filename: (req, file, cb) => {
    const uniqueName = `${Date.now()}_${file.originalname}`;
    cb(null, uniqueName);
  }
});

const upload = multer({
  storage,
  limits: { fileSize: 10 * 1024 * 1024 },
  fileFilter: (req, file, cb) => {
    const allowedMimes = ['application/pdf', 'image/jpeg', 'image/png', 'application/msword', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'];
    if (allowedMimes.includes(file.mimetype)) {
      cb(null, true);
    } else {
      cb(new Error('Invalid file type'));
    }
  }
});

// Routes
app.get('/api/health', (req, res) => {
  res.json({ status: 'ADCS Backend is running ✅' });
});

app.post('/api/upload', upload.single('file'), (req, res) => {
  if (!req.file) {
    return res.status(400).json({ error: 'No file provided' });
  }

  res.json({
    message: 'File uploaded successfully',
    file: {
      name: req.file.originalname,
      size: req.file.size,
      path: `/uploads/${req.file.filename}`,
      uploadDate: new Date().toISOString()
    }
  });
});

app.post('/api/calculate', (req, res) => {
  const { rent, refundableDeposit, nonRefundableDeposit, lockInPeriod, propertyType, locationArea } = req.body;

  const rf = parseFloat(refundableDeposit) || 0;
  const nrf = parseFloat(nonRefundableDeposit) || 0;
  const r = parseFloat(rent) || 0;
  const lp = parseFloat(lockInPeriod) || 12;

  const notionalInterest = (rf * 0.10 * lp) / 12;
  const totalConsideration = r + nrf + notionalInterest;
  const stampDuty = totalConsideration * 0.0025;

  let registrationFee = 1000;
  if (propertyType === 'Commercial') registrationFee = 2000;
  if (locationArea === 'Rural') registrationFee = 500;

  const professionalServices = 500;
  const gstTotal = (stampDuty + registrationFee + professionalServices) * 0.18;
  const total = r + rf + nrf + stampDuty + registrationFee + professionalServices + gstTotal;

  res.json({
    rent: r,
    refundableDeposit: rf,
    nonRefundableDeposit: nrf,
    notionalInterest,
    totalConsideration,
    stampDuty,
    registrationFee,
    documentHandling: 300,
    policeVerification: 200,
    professionalServices,
    applicableForGST: stampDuty + registrationFee + professionalServices,
    gstTotal,
    total
  });
});

app.use((err, req, res, next) => {
  console.error(err);
  res.status(500).json({ error: err.message || 'Internal server error' });
});

app.listen(PORT, () => {
  console.log(`✅ ADCS Backend running at http://localhost:${PORT}`);
  console.log(`📁 Uploads directory: ${uploadsDir}`);
  console.log(`🔗 Health check: http://localhost:${PORT}/api/health`);
});
