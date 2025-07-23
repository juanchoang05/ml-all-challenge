// Componente principal de la UI
import React, { useState, useEffect } from 'react';
import Breadcrumbs from './Breadcrumbs';
import ProductInfo from './ProductInfo';
import Galeria from './Galeria';
import PurchaseSection from './PurchaseSection';
import PaymentMethods from './PaymentMethods';
import Caracteristicas from './Caracteristicas';
import Descripcion from './Descripcion';
import Questions from './Questions';
import Opiniones from './Opiniones';
import Vendedor from './Vendedor';
import { useProductPage } from './hooks/useProductPageSimple.js';

const App = () => {
  const [isMobile, setIsMobile] = useState(false);
  
  // Hook principal que gestiona todo el estado de la página
  const {
    product,
    seller,
    paymentMethods,
    questions,
    reviews,
    navigation,
    selectedImage,
    selectedVariation,
    quantity,
    loading,
    errors,
    actions,
    isLoading,
    currentPrice,
    availableQuantity,
    selectedImageUrl
  } = useProductPage();

  useEffect(() => {
    const checkScreenSize = () => {
      setIsMobile(window.innerWidth <= 768);
    };

    checkScreenSize();
    window.addEventListener('resize', checkScreenSize);

    return () => window.removeEventListener('resize', checkScreenSize);
  }, []);

  return (
    <div style={{background: '#ebebeb', minHeight: '100vh'}}>
      {/* Loading overlay */}
      {isLoading && (
        <div style={{
          position: 'fixed',
          top: 0,
          left: 0,
          right: 0,
          bottom: 0,
          background: 'rgba(255, 255, 255, 0.8)',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          zIndex: 1000
        }}>
          <div>Cargando...</div>
        </div>
      )}
      
      <div style={{maxWidth: '1200px', margin: '0 auto', padding: '0 1rem'}}>
        <Breadcrumbs navigation={navigation} loading={loading.navigation} />
        
        <main style={{
          display: 'grid', 
          gridTemplateColumns: isMobile ? '1fr' : '1fr 400px', 
          gap: '2rem', 
          paddingBottom: '2rem'
        }}>
          {/* Left Column - Product Info */}
          <div style={{background: '#fff', borderRadius: '8px', padding: '1.5rem'}}>
            <div style={{
              display: 'grid', 
              gridTemplateColumns: isMobile ? '1fr' : '378px 1fr', 
              gap: '2rem', 
              marginBottom: '2rem'
            }}>
              <Galeria 
                images={product?.images || []}
                selectedImage={selectedImage}
                onImageSelect={actions.selectImage}
                loading={loading.product}
              />
              <ProductInfo 
                product={product}
                selectedVariation={selectedVariation}
                onVariationSelect={actions.selectVariation}
                loading={loading.product}
              />
            </div>
            
            <Caracteristicas 
              specifications={product?.specifications}
              highlights={product?.highlights}
              loading={loading.product}
            />
            <Descripcion 
              product={product}
              loading={loading.product}
            />
            <Questions 
              questions={questions}
              onAskQuestion={actions.askQuestion}
              loading={loading.questions}
            />
            <Opiniones 
              reviews={reviews}
              onAddReview={actions.addReview}
              loading={loading.reviews}
            />
          </div>
          
          {/* Right Column - Purchase & Seller Info */}
          <div style={{
            order: isMobile ? -1 : 0
          }}>
            <PurchaseSection 
              product={product}
              quantity={quantity}
              availableQuantity={availableQuantity}
              currentPrice={currentPrice}
              onQuantityChange={actions.updateQuantity}
              onAddToCart={actions.addToCart}
              onBuyNow={actions.buyNow}
              loading={loading.product}
            />
            <PaymentMethods 
              paymentMethods={paymentMethods}
              currentPrice={currentPrice}
              loading={loading.payment}
            />
            <Vendedor 
              seller={seller}
              loading={loading.seller}
            />
          </div>
        </main>
      </div>
    </div>
  );
};

export default App;
