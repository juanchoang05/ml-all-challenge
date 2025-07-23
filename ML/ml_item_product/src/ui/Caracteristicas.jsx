import React from 'react';

const Caracteristicas = () => (
  <div style={{
    border: '1px solid #e9ecef',
    borderRadius: '8px',
    overflow: 'hidden'
  }}>
    <h3 style={{
      margin: 0,
      padding: '1rem',
      fontSize: '1.1rem',
      color: '#333',
      background: '#f8f9fa',
      borderBottom: '1px solid #e9ecef'
    }}>
      Características del producto
    </h3>
    
    <div style={{padding: '1rem'}}>
      <div style={{marginBottom: '1rem'}}>
        <div style={{
          fontSize: '0.875rem',
          color: '#666',
          marginBottom: '0.5rem'
        }}>
          Tamaño de la pantalla: 6.1" (14.67 cm x 7.15 cm x 7.8 mm)
        </div>
        <div style={{
          fontSize: '0.875rem',
          color: '#666',
          marginBottom: '0.5rem'
        }}>
          Memoria interna: 128 GB
        </div>
        <div style={{
          fontSize: '0.875rem',
          color: '#666',
          marginBottom: '0.5rem'
        }}>
          Desbloqueo: Reconocimiento facial
        </div>
        <div style={{
          fontSize: '0.875rem',
          color: '#666',
          marginBottom: '0.5rem'
        }}>
          Cámara trasera principal: 48 Mpx
        </div>
        <div style={{
          fontSize: '0.875rem',
          color: '#666'
        }}>
          Con NFC: Sí
        </div>
      </div>

      <div style={{
        display: 'grid',
        gridTemplateColumns: 'repeat(2, 1fr)',
        gap: '1rem'
      }}>
        <div>
          <h4 style={{
            margin: '0 0 0.5rem 0',
            fontSize: '0.875rem',
            fontWeight: '500',
            color: '#333'
          }}>
            Características generales
          </h4>
          <div style={{fontSize: '0.75rem', color: '#666', marginBottom: '0.25rem'}}>
            <div style={{display: 'flex', justifyContent: 'space-between', padding: '0.25rem 0', borderBottom: '1px solid #f0f0f0'}}>
              <span>Marca</span>
              <span>Apple</span>
            </div>
            <div style={{display: 'flex', justifyContent: 'space-between', padding: '0.25rem 0', borderBottom: '1px solid #f0f0f0'}}>
              <span>Modelo</span>
              <span>iPhone 16e</span>
            </div>
            <div style={{display: 'flex', justifyContent: 'space-between', padding: '0.25rem 0', borderBottom: '1px solid #f0f0f0'}}>
              <span>Color</span>
              <span>Negro</span>
            </div>
          </div>
        </div>

        <div>
          <h4 style={{
            margin: '0 0 0.5rem 0',
            fontSize: '0.875rem',
            fontWeight: '500',
            color: '#333'
          }}>
            Pantalla
          </h4>
          <div style={{fontSize: '0.75rem', color: '#666'}}>
            <div style={{display: 'flex', justifyContent: 'space-between', padding: '0.25rem 0', borderBottom: '1px solid #f0f0f0'}}>
              <span>Tamaño</span>
              <span>6.1"</span>
            </div>
            <div style={{display: 'flex', justifyContent: 'space-between', padding: '0.25rem 0', borderBottom: '1px solid #f0f0f0'}}>
              <span>Resolución</span>
              <span>2532 px x 1170 px</span>
            </div>
            <div style={{display: 'flex', justifyContent: 'space-between', padding: '0.25rem 0', borderBottom: '1px solid #f0f0f0'}}>
              <span>Tecnología</span>
              <span>OLED Retina</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
);

export default Caracteristicas;
