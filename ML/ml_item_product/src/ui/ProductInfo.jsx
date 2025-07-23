import React from 'react';

const ProductInfo = ({ 
  product, 
  selectedVariation, 
  onVariationSelect, 
  loading = false 
}) => {
  if (loading) {
    return (
      <div style={{maxWidth: '340px'}}>
        <div style={{
          height: '200px',
          background: '#f5f5f5',
          borderRadius: '4px',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          color: '#666'
        }}>
          Cargando información del producto...
        </div>
      </div>
    );
  }

  if (!product) {
    return (
      <div style={{maxWidth: '340px'}}>
        <div style={{color: '#666', textAlign: 'center', padding: '2rem'}}>
          No se pudo cargar la información del producto
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

  const currentPrice = selectedVariation?.price || product.price;
  const condition = product.condition === 'new' ? 'Nuevo' : 'Usado';
  const soldQuantity = product.sold_quantity || 0;

  return (
    <div style={{maxWidth: '340px'}}>
      {/* Subtitle */}
      <div style={{marginBottom: '0.5rem'}}>
        <span style={{
          fontSize: '0.875rem',
          color: '#666',
          fontWeight: '400'
        }}>
          {condition} | {soldQuantity > 0 ? `+${soldQuantity} vendidos` : 'Nuevo en MercadoLibre'}
        </span>
      </div>

      {/* Title with Bookmark */}
      <div style={{
        display: 'flex',
        alignItems: 'flex-start',
        gap: '0.75rem',
        marginBottom: '1rem'
      }}>
        <h1 style={{
          margin: 0,
          fontSize: '1.5rem',
          fontWeight: '400',
          color: '#333',
          lineHeight: '1.4',
          flex: 1
        }}>
          {product.title}
        </h1>
        <button style={{
          background: 'none',
          border: 'none',
          cursor: 'pointer',
          padding: '0.25rem',
          color: '#666',
          fontSize: '1.2rem',
          lineHeight: '1',
          marginTop: '0.25rem'
        }}>
          ♡
        </button>
      </div>

      {/* Rating */}
      {product.reviews && (
        <div style={{
          display: 'flex',
          alignItems: 'center',
          gap: '0.5rem',
          marginBottom: '1rem'
        }}>
          <span style={{fontSize: '0.875rem', fontWeight: '500'}}>
            {product.reviews.rating_average || 'Sin calificar'}
          </span>
          <div style={{display: 'flex', gap: '2px'}}>
            {[...Array(5)].map((_, i) => (
              <span 
                key={i} 
                style={{
                  color: i < Math.floor(product.reviews.rating_average || 0) ? '#f0ad4e' : '#ddd', 
                  fontSize: '0.75rem'
                }}
              >
                ★
              </span>
            ))}
          </div>
          <span style={{fontSize: '0.875rem', color: '#666'}}>
            ({product.reviews.total_reviews || 0})
          </span>
        </div>
      )}

      {/* Price */}
      <div style={{marginBottom: '1rem'}}>
        <div style={{
          fontSize: '2.25rem',
          fontWeight: '300',
          color: '#333',
          marginBottom: '0.25rem'
        }}>
          {formatPrice(currentPrice)}
        </div>
        {product.installments && (
          <div style={{
            fontSize: '1.125rem',
            color: '#00a650',
            marginBottom: '0.5rem'
          }}>
            <span style={{color: '#333'}}>en</span> {product.installments.quantity} cuotas de{' '}
            <span style={{fontWeight: '500'}}>
              {formatPrice({ 
                amount: product.installments.amount, 
                currency_id: currentPrice?.currency_id 
              })}
            </span>
            {product.installments.rate === 0 && ' con 0% interés'}
          </div>
        )}
        <a href="#" style={{
          color: '#3483fa',
          fontSize: '0.875rem',
          textDecoration: 'none'
        }}>
          Ver los medios de pago
        </a>
      </div>

      {/* Variations Selector */}
      {product.variations && product.variations.length > 0 && (
        <div style={{marginBottom: '1.5rem'}}>
          <div style={{
            fontSize: '0.875rem',
            color: '#333',
            marginBottom: '0.5rem'
          }}>
            <span style={{fontWeight: '500'}}>Variación: </span>
            <span style={{fontWeight: '500'}}>
              {selectedVariation?.name || 'Selecciona una opción'}
            </span>
          </div>
          <div style={{display: 'flex', gap: '0.5rem', flexWrap: 'wrap'}}>
            {product.variations.map((variation) => (
              <button
                key={variation.id}
                onClick={() => onVariationSelect?.(variation.id)}
                style={{
                  width: '48px',
                  height: '48px',
                  border: selectedVariation?.id === variation.id ? '2px solid #3483fa' : '1px solid #ddd',
                  borderRadius: '8px',
                  overflow: 'hidden',
                  cursor: 'pointer',
                  background: 'none',
                  padding: 0
                }}
              >
                {variation.picture_ids?.[0] ? (
                  <img 
                    src={variation.picture_ids[0]}
                    alt={variation.name}
                    style={{
                      width: '100%',
                      height: '100%',
                      objectFit: 'cover'
                    }}
                  />
                ) : (
                  <div style={{
                    width: '100%',
                    height: '100%',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    fontSize: '0.75rem',
                    color: '#666'
                  }}>
                    {variation.name}
                  </div>
                )}
              </button>
            ))}
          </div>
        </div>
      )}

      {/* Highlighted Features */}
      {product.highlights && product.highlights.length > 0 && (
        <div style={{
          padding: '1rem',
          borderRadius: '8px',
          marginBottom: '1rem'
        }}>
          <h3 style={{
            margin: '0 0 0.75rem 0',
            fontSize: '0.875rem',
            fontWeight: '600',
            color: '#333'
          }}>
            {product.highlights[0]?.title || 'Características destacadas'}
          </h3>
          <ul style={{
            margin: '0 0 0.75rem 0',
            paddingLeft: '1rem',
            fontSize: '0.875rem',
            color: '#333'
          }}>
            {product.highlights.slice(0, 3).map((highlight, index) => (
              <li key={index} style={{marginBottom: '0.25rem'}}>
                {highlight.description}
              </li>
            ))}
          </ul>
          {product.warranty && (
            <p style={{
              margin: '0 0 0.75rem 0',
              fontSize: '0.875rem',
              color: '#666'
            }}>
              {product.warranty}
            </p>
          )}
          <a href="#highlighted_specs_attrs" style={{
            color: '#3483fa',
            fontSize: '0.875rem',
            textDecoration: 'none'
          }}>
            Ver características
          </a>
        </div>
      )}
    </div>
  );
};

export default ProductInfo;
