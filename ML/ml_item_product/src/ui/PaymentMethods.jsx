import React from 'react';

const PaymentMethods = () => (
  <div style={{
    background: '#fff',
    padding: '1.5rem',
    borderRadius: '8px',
    border: '1px solid #eee',
    marginBottom: '1rem'
  }}>
    <h3 style={{margin: '0 0 1rem 0', fontSize: '1.1rem', color: '#333'}}>
      Medios de pago
    </h3>
    
    <div style={{marginBottom: '1rem'}}>
      <h4 style={{
        margin: '0 0 0.5rem 0',
        fontSize: '0.875rem',
        fontWeight: '500',
        color: '#333'
      }}>
        Tarjetas de crédito
      </h4>
      <div style={{fontSize: '0.875rem', color: '#00a650', marginBottom: '0.5rem'}}>
        ¡Paga en hasta 48 cuotas!
      </div>
      <div style={{display: 'flex', gap: '0.5rem', flexWrap: 'wrap'}}>
        <div style={{
          background: '#f8f9fa',
          padding: '0.5rem',
          borderRadius: '4px',
          fontSize: '0.75rem',
          border: '1px solid #e9ecef'
        }}>
          Visa
        </div>
        <div style={{
          background: '#f8f9fa',
          padding: '0.5rem',
          borderRadius: '4px',
          fontSize: '0.75rem',
          border: '1px solid #e9ecef'
        }}>
          Mastercard
        </div>
        <div style={{
          background: '#f8f9fa',
          padding: '0.5rem',
          borderRadius: '4px',
          fontSize: '0.75rem',
          border: '1px solid #e9ecef'
        }}>
          American Express
        </div>
        <div style={{
          background: '#f8f9fa',
          padding: '0.5rem',
          borderRadius: '4px',
          fontSize: '0.75rem',
          border: '1px solid #e9ecef'
        }}>
          Crédito Fácil Codensa
        </div>
      </div>
    </div>

    <div style={{marginBottom: '1rem'}}>
      <h4 style={{
        margin: '0 0 0.5rem 0',
        fontSize: '0.875rem',
        fontWeight: '500',
        color: '#333'
      }}>
        Tarjetas de débito
      </h4>
      <div style={{display: 'flex', gap: '0.5rem', flexWrap: 'wrap'}}>
        <div style={{
          background: '#f8f9fa',
          padding: '0.5rem',
          borderRadius: '4px',
          fontSize: '0.75rem',
          border: '1px solid #e9ecef'
        }}>
          Visa Débito
        </div>
        <div style={{
          background: '#f8f9fa',
          padding: '0.5rem',
          borderRadius: '4px',
          fontSize: '0.75rem',
          border: '1px solid #e9ecef'
        }}>
          Mastercard Débito
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
        Efectivo
      </h4>
      <div style={{display: 'flex', gap: '0.5rem', flexWrap: 'wrap'}}>
        <div style={{
          background: '#f8f9fa',
          padding: '0.5rem',
          borderRadius: '4px',
          fontSize: '0.75rem',
          border: '1px solid #e9ecef'
        }}>
          Efecty
        </div>
      </div>
    </div>

    <a href="#" style={{
      color: '#3483fa',
      fontSize: '0.875rem',
      textDecoration: 'none',
      marginTop: '1rem',
      display: 'inline-block'
    }}>
      Conoce otros medios de pago
    </a>
  </div>
);

export default PaymentMethods;
