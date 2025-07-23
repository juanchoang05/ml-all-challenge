import React from 'react';

const ProductTitle = () => (
  <div style={{marginBottom: '1rem'}}>
    <div style={{
      display: 'flex',
      alignItems: 'center',
      gap: '0.5rem',
      marginBottom: '0.5rem'
    }}>
      <span style={{
        background: '#f0f0f0',
        padding: '0.25rem 0.5rem',
        borderRadius: '4px',
        fontSize: '0.75rem',
        color: '#666'
      }}>
        Caja abierta
      </span>
      <span style={{fontSize: '0.875rem', color: '#666'}}>| +5 vendidos</span>
    </div>
    
    <h1 style={{
      margin: 0,
      fontSize: '1.5rem',
      fontWeight: '400',
      color: '#333',
      lineHeight: '1.4'
    }}>
      iPhone 16e (128 Gb) - Negro (Nuevo con caja abierta)
    </h1>
    
    <div style={{
      display: 'flex',
      alignItems: 'center',
      gap: '0.5rem',
      marginTop: '0.5rem'
    }}>
      <div style={{display: 'flex', alignItems: 'center', gap: '0.25rem'}}>
        <span style={{color: '#f0ad4e'}}>★★★★★</span>
        <span style={{fontSize: '0.875rem', color: '#666'}}>4.8</span>
        <span style={{fontSize: '0.875rem', color: '#666'}}>(54 opiniones)</span>
      </div>
    </div>
  </div>
);

export default ProductTitle;
