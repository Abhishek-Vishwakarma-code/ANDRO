import React from 'react';
import { PieChart, Pie, Cell, Legend, Tooltip, ResponsiveContainer } from 'recharts';
import './FeesChart.css';

export default function FeesChart({ fees }) {
  const data = [
    {
      name: 'Stamp Duty',
      value: Math.max(fees.stampDuty, 0),
      color: '#6d28d9'
    },
    {
      name: 'Registration Fees',
      value: Math.max(fees.registrationFee, 0),
      color: '#22c55e'
    },
    {
      name: 'Professional Services',
      value: Math.max(fees.professionalServices, 0),
      color: '#fbbf24'
    },
    {
      name: 'GST',
      value: Math.max(fees.gstTotal, 0),
      color: '#f97316'
    }
  ].filter(item => item.value > 0);

  const totalFees = data.reduce((sum, item) => sum + item.value, 0);

  if (totalFees === 0) {
    return (
      <div className="chart-container">
        <p className="chart-placeholder">Enter agreement details to see fee breakdown chart</p>
      </div>
    );
  }

  return (
    <div className="chart-container">
      <h3 className="chart-title">Financial Share Analysis</h3>
      <div className="chart-wrapper">
        <ResponsiveContainer width="100%" height={280}>
          <PieChart>
            <Pie
              data={data}
              cx="50%"
              cy="50%"
              innerRadius={60}
              outerRadius={100}
              paddingAngle={2}
              dataKey="value"
            >
              {data.map((entry, index) => (
                <Cell key={`cell-${index}`} fill={entry.color} />
              ))}
            </Pie>
            <Tooltip 
              formatter={(value) => `₹${value.toLocaleString('en-IN', { maximumFractionDigits: 0 })}`}
              contentStyle={{ backgroundColor: '#1a2f4a', border: '1px solid #46a6ff' }}
            />
            <Legend />
          </PieChart>
        </ResponsiveContainer>

        <div className="chart-center-text">
          <div className="chart-label">GRAND TOTAL</div>
          <div className="chart-value">₹{(fees.total || 0).toLocaleString('en-IN', { maximumFractionDigits: 0 })}</div>
        </div>
      </div>

      <div className="fee-percentages">
        {data.map((item, idx) => {
          const percentage = ((item.value / totalFees) * 100).toFixed(1);
          return (
            <div key={idx} className="percentage-row">
              <span className="percentage-label">{item.name}</span>
              <span className="percentage-value">{percentage}%</span>
            </div>
          );
        })}
      </div>
    </div>
  );
}
