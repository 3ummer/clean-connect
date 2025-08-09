
import React, { useState, useEffect } from 'react';
import { createRoot } from 'react-dom/client';

const App = () => {
  const [form, setForm] = useState({
    name: '', phone: '', address: '', dateTime: '', cleaningType: '', notes: ''
  });
  const [message, setMessage] = useState('');

  const handleChange = e => setForm({ ...form, [e.target.name]: e.target.value });
  const handleSubmit = async e => {
    e.preventDefault();
    const res = await fetch('/booking', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(form)
    });
    setMessage(await res.text());
  };

  return (
    <div>
      <h1>Book a Cleaning</h1>
      <form onSubmit={handleSubmit}>
        {['name', 'phone', 'address', 'dateTime', 'cleaningType'].map(f => (
          <input key={f} name={f} placeholder={f} value={form[f]} onChange={handleChange} required />
        ))}
        <textarea name="notes" placeholder="Additional notes" value={form.notes} onChange={handleChange} />
        <button type="submit">Submit</button>
      </form>
      <p>{message}</p>
    </div>
  );
};

const root = createRoot(document.getElementById('root'));
root.render(<App />);
