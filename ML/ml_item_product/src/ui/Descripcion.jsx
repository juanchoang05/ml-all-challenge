import React from 'react';

const Descripcion = () => (
  <div style={{
    border: '1px solid #e9ecef',
    borderRadius: '8px',
    overflow: 'hidden',
    marginTop: '2rem'
  }}>
    <h3 style={{
      margin: 0,
      padding: '1rem',
      fontSize: '1.1rem',
      color: '#333',
      background: '#f8f9fa',
      borderBottom: '1px solid #e9ecef'
    }}>
      Descripción
    </h3>
    
    <div style={{padding: '1rem'}}>
      <div style={{
        background: '#e8f4fd',
        padding: '0.75rem',
        borderRadius: '6px',
        marginBottom: '1rem',
        fontSize: '0.875rem'
      }}>
        <div style={{fontWeight: '500', marginBottom: '0.25rem'}}>
          Producto con la caja abierta
        </div>
        <div style={{color: '#666'}}>
          Es un producto nuevo que fue abierto e incluye sus accesorios originales.
        </div>
      </div>

      <div style={{
        fontSize: '0.875rem',
        lineHeight: '1.6',
        color: '#333',
        marginBottom: '1rem'
      }}>
        <p style={{margin: '0 0 1rem 0'}}>
          El iPhone 16e está diseñado para Apple Intelligence y tiene toda la potencia del 
          chip A18. Te permite tomar fotos en superalta resolución con la cámara Fusion 
          de 48 MP. Y como trae una superbatería, tienes más tiempo para mandar mensajes, 
          navegar y mucho más.
        </p>
        
        <div style={{fontWeight: '500', marginBottom: '0.5rem'}}>
          DISEÑADO PARA APPLE INTELLIGENCE
        </div>
        <p style={{margin: '0 0 1rem 0'}}>
          Personal, privado y poderoso. Escribe, exprésate y haz de todo con mucha facilidad.
        </p>
        
        <div style={{fontWeight: '500', marginBottom: '0.5rem'}}>
          CHIP A18. A LA VELOCIDAD DEL FUTURO
        </div>
        <p style={{margin: '0 0 1rem 0'}}>
          El chip A18 te permite disfrutar Apple Intelligence, los juegos y las 
          actualizaciones periódicas de iOS para que tu iPhone funcione como el primer día.
        </p>
        
        <div style={{fontWeight: '500', marginBottom: '0.5rem'}}>
          SUPERBATERÍA
        </div>
        <p style={{margin: '0 0 1rem 0'}}>
          Manda mensajes, navega y disfruta hasta 26 horas de reproducción de video: 
          la mayor duración de batería en un iPhone de 6.1 pulgadas.
        </p>
        
        <div style={{fontWeight: '500', marginBottom: '0.5rem'}}>
          CÁMARAS
        </div>
        <p style={{margin: '0 0 1rem 0'}}>
          El sistema de cámara dos en uno cuenta con una cámara Fusion de 48 MP 
          para fotos en superalta resolución y una cámara teleobjetivo de calidad 
          óptica de 2x. Y toma increíbles selfies con la cámara frontal de 12 MP.
        </p>
      </div>

      <div style={{
        background: '#f8f9fa',
        padding: '1rem',
        borderRadius: '6px',
        fontSize: '0.875rem'
      }}>
        <div style={{
          display: 'flex',
          alignItems: 'center',
          gap: '0.5rem',
          marginBottom: '0.5rem'
        }}>
          <span style={{color: '#00a650', fontWeight: '500'}}>✓</span>
          <span>Envío gratis a todo el país</span>
        </div>
        <div style={{fontSize: '0.75rem', color: '#666'}}>
          Conoce los tiempos y las formas de envío.
        </div>
      </div>
    </div>
  </div>
);

export default Descripcion;
