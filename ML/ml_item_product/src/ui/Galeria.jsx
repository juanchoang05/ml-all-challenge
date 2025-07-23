import React, { useState, useEffect } from 'react';

const Galeria = ({ 
  images = [], 
  selectedImage = 0, 
  onImageSelect, 
  loading = false 
}) => {
  const [hoveredThumb, setHoveredThumb] = useState(null);
  const [hoveredMain, setHoveredMain] = useState(false);
  const [isMobile, setIsMobile] = useState(false);

  // Default images if none provided
  const defaultImages = [
    "https://http2.mlstatic.com/D_Q_NP_2X_952892-MLA82578193512_032025-R.webp",
    "https://http2.mlstatic.com/D_NQ_NP_2X_952892-MLA82578193512_032025-F.webp",
    "https://http2.mlstatic.com/D_Q_NP_2X_947859-MLU86339427239_062025-R.webp",
    "https://http2.mlstatic.com/D_Q_NP_2X_960729-MLA82578174454_032025-R.webp"
  ];

  const displayImages = images.length > 0 ? images : defaultImages;

  // Hook para detectar tamaño de pantalla
  useEffect(() => {
    const checkScreenSize = () => {
      setIsMobile(window.innerWidth <= 768);
    };

    checkScreenSize();
    window.addEventListener('resize', checkScreenSize);

    return () => window.removeEventListener('resize', checkScreenSize);
  }, []);

  const handleImageSelect = (index) => {
    if (onImageSelect) {
      onImageSelect(index);
    }
  };

  if (loading) {
    return (
      <div style={{
        width: isMobile ? '100%' : '358px',
        height: isMobile ? 'auto' : '504px',
        aspectRatio: isMobile ? '358/504' : undefined,
        background: '#f5f5f5',
        borderRadius: '8px',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        color: '#666'
      }}>
        Cargando imágenes...
      </div>
    );
  }

  // Estilos para desktop (358x504)
  const desktopImageStyle = {
    width: '358px',
    height: '504px',
    objectFit: 'cover',
    borderRadius: '8px',
    border: '1px solid #eee',
    transition: 'transform 0.3s ease, box-shadow 0.3s ease',
    transform: hoveredMain ? 'scale(1.05)' : 'scale(1)',
    boxShadow: hoveredMain ? '0 8px 24px rgba(0,0,0,0.15)' : '0 2px 8px rgba(0,0,0,0.1)',
    cursor: 'pointer'
  };

  // Estilos para móvil (responsivo)
  const mobileImageStyle = {
    width: '100%',
    height: 'auto',
    aspectRatio: '358/504',
    objectFit: 'cover',
    borderRadius: '8px',
    border: '1px solid #eee',
    transition: 'transform 0.3s ease, box-shadow 0.3s ease',
    transform: hoveredMain ? 'scale(1.02)' : 'scale(1)',
    boxShadow: hoveredMain ? '0 8px 24px rgba(0,0,0,0.15)' : '0 2px 8px rgba(0,0,0,0.1)',
    cursor: 'pointer'
  };

  return (
    <div style={{display: 'flex', flexDirection: 'column', gap: '1rem'}}>
      <div style={{position: 'relative'}}>
        <img 
          src={displayImages[selectedImage]?.url || displayImages[selectedImage]}
          alt={displayImages[selectedImage]?.alt || `Imagen ${selectedImage + 1}`}
          style={isMobile ? mobileImageStyle : desktopImageStyle}
          onMouseEnter={() => setHoveredMain(true)}
          onMouseLeave={() => setHoveredMain(false)}
        />
        {displayImages.length > 4 && (
          <div style={{
            position: 'absolute',
            top: '0.5rem',
            right: '0.5rem',
            background: 'rgba(0,0,0,0.5)',
            color: '#fff',
            padding: '0.25rem 0.5rem',
            borderRadius: '4px',
            fontSize: '0.75rem'
          }}>
            +{displayImages.length - 4}
          </div>
        )}
      </div>
      
      <div style={{
        display: 'grid',
        gridTemplateColumns: 'repeat(4, 1fr)',
        gap: '0.5rem'
      }}>
        {displayImages.slice(0, 4).map((image, index) => (
          <button
            key={`thumb-${index}`}
            style={{
              width: '100%',
              aspectRatio: '1',
              objectFit: 'cover',
              borderRadius: '4px',
              border: selectedImage === index ? '2px solid #3483fa' : '1px solid #eee',
              cursor: 'pointer',
              transition: 'transform 0.2s ease, box-shadow 0.2s ease, border-color 0.2s ease',
              transform: hoveredThumb === index ? 'scale(1.1)' : 'scale(1)',
              boxShadow: hoveredThumb === index ? '0 4px 12px rgba(52, 131, 250, 0.3)' : 'none',
              opacity: hoveredThumb === index ? 0.9 : 1,
              background: 'none',
              padding: 0,
              overflow: 'hidden'
            }}
            onMouseEnter={() => setHoveredThumb(index)}
            onMouseLeave={() => setHoveredThumb(null)}
            onClick={() => handleImageSelect(index)}
          >
            <img 
              src={image?.url || image}
              alt={image?.alt || `Miniatura ${index + 1}`}
              style={{
                width: '100%',
                height: '100%',
                objectFit: 'cover'
              }}
            />
          </button>
        ))}
      </div>
    </div>
  );
};

export default Galeria;
