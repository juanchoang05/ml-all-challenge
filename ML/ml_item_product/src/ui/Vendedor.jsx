import React from 'react';

const Vendedor = () => (
  <div style={{
    background: '#fff',
    padding: '1.5rem',
    borderRadius: '8px',
    border: '1px solid #eee',
    marginBottom: '1rem'
  }}>
    <h3 style={{margin: '0 0 1rem 0', fontSize: '1.1rem', color: '#333'}}>
      Vendido por DOGUINLOPEZ
    </h3>
    
    <div style={{
      display: 'flex',
      alignItems: 'center',
      gap: '1rem',
      marginBottom: '1rem'
    }}>
      <div style={{
        background: '#f8f9fa',
        borderRadius: '50%',
        width: '60px',
        height: '60px',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        fontSize: '1.5rem',
        color: '#666'
      }}>
        DL
      </div>
      
      <div>
        <div style={{fontWeight: '500', color: '#333'}}>+25 Productos</div>
        <div style={{fontSize: '0.875rem', color: '#666'}}>+25 Ventas concretadas</div>
      </div>
    </div>

    <div style={{marginBottom: '1rem'}}>
      <div style={{
        display: 'flex',
        alignItems: 'center',
        gap: '0.5rem',
        marginBottom: '0.25rem'
      }}>
        <span style={{color: '#00a650'}}>✓</span>
        <span style={{fontSize: '0.875rem'}}>Brinda buena atención</span>
      </div>
      <div style={{
        display: 'flex',
        alignItems: 'center',
        gap: '0.5rem'
      }}>
        <span style={{color: '#00a650'}}>✓</span>
        <span style={{fontSize: '0.875rem'}}>Entrega sus productos a tiempo</span>
      </div>
    </div>

    <a 
      href="https://listado.mercadolibre.com.co/_CustId_474533265?item_id=MCO2951177370&category_id=MCO1055&seller_id=474533265&client=recoview-selleritems&recos_listing=true#origin=pdp&component=sellerData&typeSeller=classic" 
      target="_blank" 
      rel="noopener noreferrer"
      style={{
        color: '#3483fa',
        fontSize: '0.875rem',
        textDecoration: 'none'
      }}
    >
      Ver más productos del vendedor
    </a>
  </div>
);

export default Vendedor;
