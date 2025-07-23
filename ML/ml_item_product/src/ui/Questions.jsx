import React from 'react';

const Questions = () => (
  <div style={{
    background: '#fff',
    padding: '1.5rem',
    borderRadius: '8px',
    border: '1px solid #eee',
    marginBottom: '1rem'
  }}>
    <h3 style={{margin: '0 0 1rem 0', fontSize: '1.1rem', color: '#333'}}>
      Preguntas
    </h3>
    
    <div style={{
      fontSize: '0.875rem',
      color: '#666',
      marginBottom: '1rem'
    }}>
      Tiempo aproximado de respuesta: 49 minutos
    </div>

    <button style={{
      background: '#3483fa',
      color: '#fff',
      border: 'none',
      padding: '0.75rem 1.5rem',
      borderRadius: '6px',
      fontSize: '0.875rem',
      fontWeight: '500',
      cursor: 'pointer'
    }}>
      Preguntar
    </button>
  </div>
);

export default Questions;
