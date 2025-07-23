import React from 'react';

const PurchaseSection = ({ 
  product, 
  quantity = 1, 
  availableQuantity = 0, 
  currentPrice, 
  onQuantityChange, 
  onAddToCart, 
  onBuyNow, 
  loading = false 
}) => {
  if (loading) {
    return (
      <div style={{
        background: '#fff',
        padding: '1.5rem',
        borderRadius: '8px',
        border: '1px solid #eee',
        marginBottom: '1rem'
      }}>
        <div style={{
          height: '200px',
          background: '#f5f5f5',
          borderRadius: '4px',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          color: '#666'
        }}>
          Cargando información de compra...
        </div>
      </div>
    );
  }

  if (!product) {
    return (
      <div style={{
        background: '#fff',
        padding: '1.5rem',
        borderRadius: '8px',
        border: '1px solid #eee',
        marginBottom: '1rem'
      }}>
        <div style={{color: '#666', textAlign: 'center'}}>
          No se pudo cargar la información de compra
        </div>
      </div>
    );
  }

  const formatPrice = (price) => {
    if (!price) return 'Precio no disponible';
    return new Intl.NumberFormat('es-CO', {
      style: 'currency',
      currency: price.currency_id || 'COP'
    }).format(price.amount);
  };

  const handleQuantityChange = (newQuantity) => {
    if (onQuantityChange) {
      onQuantityChange(Math.max(1, Math.min(newQuantity, availableQuantity)));
    }
  };

  const isOutOfStock = availableQuantity === 0;
  const canAddMore = quantity < availableQuantity;

  return (
    <div style={{
      background: '#fff',
      padding: '1.5rem',
      borderRadius: '8px',
      border: '1px solid #eee',
      marginBottom: '1rem'
    }}>
      {/* Price Display */}
      {currentPrice && (
        <div style={{marginBottom: '1rem'}}>
          <div style={{
            fontSize: '1.5rem',
            fontWeight: '300',
            color: '#333',
            marginBottom: '0.5rem'
          }}>
            {formatPrice(currentPrice)}
          </div>
        </div>
      )}

      {/* Shipping Info */}
      <div style={{marginBottom: '1rem'}}>
        <div style={{color: '#00a650', fontWeight: '500', marginBottom: '0.5rem'}}>
          {product.shipping?.free_shipping ? 'Envío gratis a todo el país' : 'Consultar envío'}
        </div>
        <a href="#" style={{color: '#3483fa', fontSize: '0.875rem', textDecoration: 'none'}}>
          Calcular cuándo llega
        </a>
      </div>

      {/* Stock and Quantity */}
      <div style={{marginBottom: '1.5rem'}}>
        {isOutOfStock ? (
          <div style={{color: '#f23645', fontWeight: '500', marginBottom: '0.25rem'}}>
            Sin stock disponible
          </div>
        ) : (
          <div style={{color: '#00a650', fontWeight: '500', marginBottom: '0.25rem'}}>
            Stock disponible
          </div>
        )}
        
        <div style={{
          display: 'flex',
          alignItems: 'center',
          gap: '0.5rem',
          marginBottom: '0.5rem'
        }}>
          <span style={{fontSize: '0.875rem', color: '#666'}}>Cantidad:</span>
          <div style={{display: 'flex', alignItems: 'center', gap: '0.5rem'}}>
            <button
              onClick={() => handleQuantityChange(quantity - 1)}
              disabled={quantity <= 1}
              style={{
                width: '32px',
                height: '32px',
                border: '1px solid #ddd',
                background: quantity <= 1 ? '#f5f5f5' : '#fff',
                borderRadius: '4px',
                cursor: quantity <= 1 ? 'not-allowed' : 'pointer',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center'
              }}
            >
              -
            </button>
            <span style={{
              minWidth: '32px',
              textAlign: 'center',
              fontSize: '0.875rem'
            }}>
              {quantity}
            </span>
            <button
              onClick={() => handleQuantityChange(quantity + 1)}
              disabled={!canAddMore}
              style={{
                width: '32px',
                height: '32px',
                border: '1px solid #ddd',
                background: !canAddMore ? '#f5f5f5' : '#fff',
                borderRadius: '4px',
                cursor: !canAddMore ? 'not-allowed' : 'pointer',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center'
              }}
            >
              +
            </button>
          </div>
          <span style={{fontSize: '0.875rem', color: '#666'}}>
            ({availableQuantity} disponibles)
          </span>
        </div>
      </div>

      {/* Action Buttons */}
      <div style={{display: 'flex', flexDirection: 'column', gap: '0.75rem'}}>
        <button 
          onClick={onBuyNow}
          disabled={isOutOfStock}
          style={{
            background: isOutOfStock ? '#ddd' : '#3483fa',
            color: '#fff',
            border: 'none',
            padding: '0.875rem 1.5rem',
            borderRadius: '6px',
            fontSize: '1rem',
            fontWeight: '500',
            cursor: isOutOfStock ? 'not-allowed' : 'pointer',
            width: '100%'
          }}
        >
          {isOutOfStock ? 'Sin stock' : 'Comprar ahora'}
        </button>
        <button 
          onClick={onAddToCart}
          disabled={isOutOfStock}
          style={{
            background: isOutOfStock ? '#f5f5f5' : 'rgba(52, 131, 250, 0.1)',
            color: isOutOfStock ? '#999' : '#3483fa',
            border: `1px solid ${isOutOfStock ? '#ddd' : '#3483fa'}`,
            padding: '0.875rem 1.5rem',
            borderRadius: '6px',
            fontSize: '1rem',
            fontWeight: '500',
            cursor: isOutOfStock ? 'not-allowed' : 'pointer',
            width: '100%'
          }}
        >
          {isOutOfStock ? 'Sin stock' : 'Agregar al carrito'}
        </button>
      </div>

      {/* Additional Info */}
      <div style={{marginTop: '1rem', fontSize: '0.875rem', color: '#666'}}>
        <div style={{marginBottom: '0.5rem'}}>
          <span style={{color: '#00a650'}}>Devolución gratis.</span> Tienes 30 días desde que lo recibes.
        </div>
        <div style={{marginBottom: '0.5rem'}}>
          <span style={{color: '#3483fa'}}>Compra Protegida</span>, recibe el producto que esperabas o te devolvemos tu dinero.
        </div>
        {product.warranty && (
          <div>{product.warranty}</div>
        )}
      </div>
    </div>
  );
};

export default PurchaseSection;
