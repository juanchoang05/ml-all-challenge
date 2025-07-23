import React from 'react';

const Breadcrumbs = () => (
  <nav style={{
    padding: '0.75rem 0',
    fontSize: '0.875rem',
    color: '#666'
  }}>
    <a href="#" style={{color: '#3483fa', textDecoration: 'none'}}>Celulares y Teléfonos</a>
    <span style={{margin: '0 0.5rem', color: '#999'}}>&gt;</span>
    <a href="#" style={{color: '#3483fa', textDecoration: 'none'}}>Celulares y Smartphones</a>
    <span style={{margin: '0 0.5rem', color: '#999'}}>&gt;</span>
    <a href="#" style={{color: '#3483fa', textDecoration: 'none'}}>iPhone</a>
  </nav>
);

export default Breadcrumbs;
