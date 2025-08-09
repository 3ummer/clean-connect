
import React, { useState, useEffect } from 'react';
import { createRoot } from 'react-dom/client';
import './styles.css';

const App = () => {
  const [form, setForm] = useState({
    name: '', phone: '', address: '', dateTime: '', cleaningType: '', notes: ''
  });
  const [message, setMessage] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  const serviceTypes = [
    { value: 'standard', label: 'Standard Cleaning', icon: '🧹' },
    { value: 'deep', label: 'Deep Cleaning', icon: '✨' },
    { value: 'move-in', label: 'Move In/Out', icon: '📦' },
    { value: 'post-construction', label: 'Post Construction', icon: '🏗️' }
  ];

  const handleChange = e => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleServiceTypeSelect = (serviceType) => {
    setForm({ ...form, cleaningType: serviceType });
  };

  const handleSubmit = async e => {
    e.preventDefault();
    setIsLoading(true);
    setMessage('');

    try {
      const res = await fetch('/api/bookings', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(form)
      });
      
      if (!res.ok) {
        throw new Error(`HTTP error! status: ${res.status}`);
      }
      
      const response = await res.json();
      setMessage(response.message);
    } catch (error) {
      setMessage('Error submitting booking. Please try again.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="container">
      <header className="header">
        <h1>Clean Connect</h1>
        <p>Professional cleaning services at your fingertips</p>
      </header>

      <div className="form-card">
        <form onSubmit={handleSubmit} className="booking-form">
          <div className="form-row">
            <div className="form-group">
              <label className="form-label">Full Name</label>
              <input
                type="text"
                name="name"
                value={form.name}
                onChange={handleChange}
                className="form-input"
                placeholder="Enter your full name"
                required
              />
            </div>
            <div className="form-group">
              <label className="form-label">Phone Number</label>
              <input
                type="tel"
                name="phone"
                value={form.phone}
                onChange={handleChange}
                className="form-input"
                placeholder="(555) 123-4567"
                required
              />
            </div>
          </div>

          <div className="form-group">
            <label className="form-label">Service Address</label>
            <input
              type="text"
              name="address"
              value={form.address}
              onChange={handleChange}
              className="form-input"
              placeholder="123 Main St, City, State 12345"
              required
            />
          </div>

          <div className="form-group">
            <label className="form-label">Preferred Date & Time</label>
            <input
              type="datetime-local"
              name="dateTime"
              value={form.dateTime}
              onChange={handleChange}
              className="form-input"
              required
            />
          </div>

          <div className="form-group">
            <label className="form-label">Cleaning Service Type</label>
            <div className="service-types">
              {serviceTypes.map(service => (
                <div
                  key={service.value}
                  className={`service-type ${form.cleaningType === service.value ? 'selected' : ''}`}
                  onClick={() => handleServiceTypeSelect(service.value)}
                >
                  <span className="service-icon">{service.icon}</span>
                  {service.label}
                </div>
              ))}
            </div>
          </div>

          <div className="form-group">
            <label className="form-label">Additional Notes (Optional)</label>
            <textarea
              name="notes"
              value={form.notes}
              onChange={handleChange}
              className="form-textarea"
              placeholder="Any special instructions or requests..."
            />
          </div>

          <button 
            type="submit" 
            className={`submit-btn ${isLoading ? 'loading' : ''}`}
            disabled={isLoading}
          >
            {isLoading ? 'Booking...' : 'Book My Cleaning'}
          </button>
        </form>

        {message && (
          <div className={`message ${message.includes('Error') ? 'error' : 'success'}`}>
            {message}
          </div>
        )}
      </div>
    </div>
  );
};

const root = createRoot(document.getElementById('root'));
root.render(<App />);
