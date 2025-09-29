
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
    { value: 'standard', label: '基礎清潔', icon: '🧹' },
    { value: 'deep', label: '深度清潔', icon: '✨' },
    { value: 'move-in', label: '搬家清潔', icon: '📦' },
    { value: 'post-construction', label: '裝潢後清潔', icon: '🏗️' }
  ];

  const handleChange = e => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleServiceTypeSelect = (serviceType) => {
    setForm({ ...form, cleaningType: serviceType });
  };

  const handleSubmit = async e => {
    e.preventDefault();
    console.log('🚀 Form submission started');
    console.log('📋 Form data:', form);
    
    setIsLoading(true);
    setMessage('');

    try {
      // Use /api which gets rewritten to Cloud Run by Firebase hosting
      const API_BASE_URL = '/api';
      console.log('🌐 API_BASE_URL:', API_BASE_URL);
      
      const requestUrl = `${API_BASE_URL}/bookings`;
      console.log('📡 Making request to:', requestUrl);
      console.log('📦 Request payload:', JSON.stringify(form, null, 2));
      
      const res = await fetch(requestUrl, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(form)
      });
      
      console.log('📨 Response status:', res.status);
      console.log('📨 Response ok:', res.ok);
      
      if (!res.ok) {
        console.error('❌ HTTP error! status:', res.status);
        throw new Error(`HTTP error! status: ${res.status}`);
      }
      
      const response = await res.json();
      console.log('✅ Response data:', response);
      setMessage(response.message);
    } catch (error) {
      console.error('❌ Error in handleSubmit:', error);
      console.error('❌ Error message:', error.message);
      console.error('❌ Error stack:', error.stack);
      setMessage('提交預約時發生錯誤，請稍後再試。');
    } finally {
      console.log('🏁 Form submission finished, setting loading to false');
      setIsLoading(false);
    }
  };

  return (
    <div className="container">
      <header className="header">
        <h1>潔淨管家</h1>
        <p>專業居家清潔服務，讓您的家煥然一新</p>
      </header>

      <div className="form-card">
        <form onSubmit={handleSubmit} className="booking-form">
          <div className="form-row">
            <div className="form-group">
              <label className="form-label">姓名</label>
              <input
                type="text"
                name="name"
                value={form.name}
                onChange={handleChange}
                className="form-input"
                placeholder="請輸入您的姓名"
                required
              />
            </div>
            <div className="form-group">
              <label className="form-label">聯絡電話</label>
              <input
                type="tel"
                name="phone"
                value={form.phone}
                onChange={handleChange}
                className="form-input"
                placeholder="09XX-XXX-XXX"
                required
              />
            </div>
          </div>

          <div className="form-group">
            <label className="form-label">服務地址</label>
            <input
              type="text"
              name="address"
              value={form.address}
              onChange={handleChange}
              className="form-input"
              placeholder="台北市大安區忠孝東路四段XXX號"
              required
            />
          </div>

          <div className="form-group">
            <label className="form-label">預約日期與時間</label>
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
            <label className="form-label">清潔服務類型</label>
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
            <label className="form-label">備註事項（選填）</label>
            <textarea
              name="notes"
              value={form.notes}
              onChange={handleChange}
              className="form-textarea"
              placeholder="如有特殊需求或注意事項，請在此說明..."
            />
          </div>

          <button
            type="submit"
            className={`submit-btn ${isLoading ? 'loading' : ''}`}
            disabled={isLoading}
          >
            {isLoading ? '預約中...' : '立即預約清潔服務'}
          </button>
        </form>

        {message && (
          <div className={`message ${message.includes('錯誤') || message.includes('Error') ? 'error' : 'success'}`}>
            {message}
          </div>
        )}
      </div>
    </div>
  );
};

const root = createRoot(document.getElementById('root'));
root.render(<App />);
