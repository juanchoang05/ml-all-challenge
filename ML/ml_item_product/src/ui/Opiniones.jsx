import React from 'react';

const Opiniones = () => (
  <div style={{
    background: '#fff',
    padding: '1.5rem',
    borderRadius: '8px',
    border: '1px solid #eee',
    marginBottom: '1rem'
  }}>
    <h3 style={{margin: '0 0 1rem 0', fontSize: '1.1rem', color: '#333'}}>
      Opiniones del producto
    </h3>
    
    <div style={{
      display: 'flex',
      alignItems: 'center',
      gap: '0.5rem',
      marginBottom: '1rem'
    }}>
      <span style={{color: '#f0ad4e', fontSize: '1.2rem'}}>★★★★★</span>
      <span style={{fontWeight: '500'}}>4.8 de 5.</span>
      <span style={{color: '#666'}}>54 opiniones.</span>
    </div>

    <div style={{marginBottom: '1rem'}}>
      <h4 style={{
        margin: '0 0 0.5rem 0',
        fontSize: '0.875rem',
        fontWeight: '500',
        color: '#333'
      }}>
        Calificación de características
      </h4>
      <div style={{display: 'flex', flexDirection: 'column', gap: '0.5rem'}}>
        <div style={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}}>
          <span style={{fontSize: '0.875rem'}}>Relación precio-calidad</span>
          <div style={{display: 'flex', alignItems: 'center', gap: '0.25rem'}}>
            <span style={{color: '#f0ad4e'}}>★★★★★</span>
            <span style={{fontSize: '0.875rem', color: '#666'}}>4.7</span>
          </div>
        </div>
        <div style={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}}>
          <span style={{fontSize: '0.875rem'}}>Duración de la batería</span>
          <div style={{display: 'flex', alignItems: 'center', gap: '0.25rem'}}>
            <span style={{color: '#f0ad4e'}}>★★★★★</span>
            <span style={{fontSize: '0.875rem', color: '#666'}}>4.7</span>
          </div>
        </div>
        <div style={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}}>
          <span style={{fontSize: '0.875rem'}}>Calidad de la cámara</span>
          <div style={{display: 'flex', alignItems: 'center', gap: '0.25rem'}}>
            <span style={{color: '#f0ad4e'}}>★★★★★</span>
            <span style={{fontSize: '0.875rem', color: '#666'}}>4.8</span>
          </div>
        </div>
        <div style={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}}>
          <span style={{fontSize: '0.875rem'}}>Durabilidad</span>
          <div style={{display: 'flex', alignItems: 'center', gap: '0.25rem'}}>
            <span style={{color: '#f0ad4e'}}>★★★★★</span>
            <span style={{fontSize: '0.875rem', color: '#666'}}>4.8</span>
          </div>
        </div>
      </div>
    </div>

    <div style={{marginBottom: '1rem'}}>
      <h4 style={{
        margin: '0 0 0.75rem 0',
        fontSize: '0.875rem',
        fontWeight: '500',
        color: '#333'
      }}>
        Opiniones destacadas
      </h4>
      
      <div style={{
        background: '#f8f9fa',
        padding: '1rem',
        borderRadius: '6px',
        marginBottom: '1rem',
        borderLeft: '4px solid #3483fa'
      }}>
        <div style={{
          fontSize: '0.875rem',
          color: '#666',
          marginBottom: '0.5rem'
        }}>
          Resumen de opiniones generado por IA
        </div>
        <p style={{
          margin: 0,
          fontSize: '0.875rem',
          lineHeight: '1.4'
        }}>
          Su batería tiene una duración excepcional, permitiendo un uso intensivo sin necesidad de recargas frecuentes. Además, cuenta con un excelente procesador y cámara, lo que lo convierte en una opción atractiva para quienes buscan calidad y eficiencia en un dispositivo móvil.
        </p>
      </div>

      <div style={{
        border: '1px solid #e9ecef',
        borderRadius: '6px',
        padding: '1rem',
        marginBottom: '0.75rem'
      }}>
        <div style={{
          display: 'flex',
          alignItems: 'center',
          gap: '0.5rem',
          marginBottom: '0.5rem'
        }}>
          <span style={{color: '#f0ad4e'}}>★★★★★</span>
          <span style={{fontSize: '0.875rem', color: '#666'}}>20 mar. 2025</span>
        </div>
        <p style={{
          margin: 0,
          fontSize: '0.875rem',
          lineHeight: '1.4'
        }}>
          Celular nuevo, en caja y con su sellos de seguridad. Es la versión con esim pero se activa super rápido desde un punto físico del operador.
        </p>
        <div style={{
          marginTop: '0.5rem',
          fontSize: '0.75rem',
          color: '#666'
        }}>
          Es útil 👍 11
        </div>
      </div>

      <div style={{
        border: '1px solid #e9ecef',
        borderRadius: '6px',
        padding: '1rem'
      }}>
        <div style={{
          display: 'flex',
          alignItems: 'center',
          gap: '0.5rem',
          marginBottom: '0.5rem'
        }}>
          <span style={{color: '#f0ad4e'}}>★★★★★</span>
          <span style={{fontSize: '0.875rem', color: '#666'}}>07 abr. 2025</span>
        </div>
        <p style={{
          margin: 0,
          fontSize: '0.875rem',
          lineHeight: '1.4'
        }}>
          Un buen producto para su precio, como entrada al mundo iPhone es una buena alternativa, también para los que quieren la calidad Apple con los aspectos necesarios y básicos.
        </p>
        <div style={{
          marginTop: '0.5rem',
          fontSize: '0.75rem',
          color: '#666'
        }}>
          Es útil 👍 8
        </div>
      </div>
    </div>

    <a href="#" style={{
      color: '#3483fa',
      fontSize: '0.875rem',
      textDecoration: 'none'
    }}>
      Mostrar todas las opiniones
    </a>
  </div>
);

export default Opiniones;
