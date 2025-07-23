# Integración UI + Casos de Uso

Este documento explica cómo la UI está integrada con los casos de uso para obtener y gestionar datos de forma reactiva.

## 🏗️ Arquitectura de Integración

```
UI Components ← Hook (useProductPage) ← Use Cases ← Adapters ← Infrastructure Services ← Mock Services
```

## 🎯 Hook Principal: `useProductPage`

El hook `useProductPage` es el punto central que conecta toda la UI con los casos de uso:

```javascript
const {
  product,           // Datos del producto
  seller,           // Información del vendedor  
  paymentMethods,   // Métodos de pago
  questions,        // Preguntas del producto
  reviews,          // Opiniones y calificaciones
  navigation,       // Breadcrumbs y navegación
  
  loading,          // Estados de carga por sección
  errors,           // Errores por sección
  actions,          // Acciones para interactuar
  
  selectedVariation, // Variación seleccionada
  selectedImage,     // Imagen seleccionada
  quantity,          // Cantidad seleccionada
  
  isLoading,         // Loading general
  currentPrice,      // Precio actual (con variación)
  availableQuantity  // Stock disponible
} = useProductPage(productId);
```

## 📱 Componentes Actualizados

### App.jsx
**Cambios principales:**
- Integra el hook `useProductPage`
- Pasa datos dinámicos a todos los componentes hijos
- Gestiona estado de loading global
- Maneja errores de forma centralizada

```jsx
const App = () => {
  const {
    product, seller, paymentMethods, questions, reviews,
    navigation, loading, actions, // ... otros datos
  } = useProductPage();

  return (
    <div>
      <Breadcrumbs navigation={navigation} loading={loading.navigation} />
      <Galeria 
        images={product?.images}
        selectedImage={selectedImage}
        onImageSelect={actions.selectImage}
        loading={loading.product}
      />
      {/* ... otros componentes */}
    </div>
  );
};
```

### ProductInfo.jsx
**Datos dinámicos:**
- Título del producto desde `product.title`
- Precio desde `currentPrice` (incluye variaciones)
- Calificaciones desde `product.reviews`
- Variaciones desde `product.variations`
- Características destacadas desde `product.highlights`

```jsx
const ProductInfo = ({ 
  product, 
  selectedVariation, 
  onVariationSelect, 
  loading 
}) => {
  const formatPrice = (price) => {
    return new Intl.NumberFormat('es-CO', {
      style: 'currency',
      currency: price.currency_id
    }).format(price.amount);
  };

  if (loading) return <LoadingComponent />;
  
  return (
    <div>
      <h1>{product.title}</h1>
      <div>{formatPrice(currentPrice)}</div>
      {/* Selector de variaciones */}
      {product.variations?.map(variation => (
        <button onClick={() => onVariationSelect(variation.id)}>
          {variation.name}
        </button>
      ))}
    </div>
  );
};
```

### Galeria.jsx
**Datos dinámicos:**
- Imágenes desde `product.images`
- Selección de imagen desde `selectedImage`
- Callback `onImageSelect` para cambiar imagen

```jsx
const Galeria = ({ 
  images, 
  selectedImage, 
  onImageSelect, 
  loading 
}) => {
  if (loading) return <LoadingGallery />;
  
  return (
    <div>
      <img src={images[selectedImage]?.url} />
      {images.map((img, index) => (
        <button onClick={() => onImageSelect(index)}>
          <img src={img.url} />
        </button>
      ))}
    </div>
  );
};
```

### PurchaseSection.jsx
**Datos dinámicos:**
- Precio actual desde `currentPrice`
- Stock desde `availableQuantity`
- Cantidad desde `quantity`
- Callbacks para acciones de compra

```jsx
const PurchaseSection = ({ 
  product, 
  quantity, 
  availableQuantity, 
  currentPrice,
  onQuantityChange, 
  onAddToCart, 
  onBuyNow 
}) => {
  return (
    <div>
      <div>{formatPrice(currentPrice)}</div>
      <div>Stock: {availableQuantity} disponibles</div>
      
      <div>
        <button onClick={() => onQuantityChange(quantity - 1)}>-</button>
        <span>{quantity}</span>
        <button onClick={() => onQuantityChange(quantity + 1)}>+</button>
      </div>
      
      <button onClick={onBuyNow}>Comprar ahora</button>
      <button onClick={onAddToCart}>Agregar al carrito</button>
    </div>
  );
};
```

## 🔄 Flujo de Datos

### 1. Inicialización
```javascript
// useProductPage.js
useEffect(() => {
  const useCases = initializeUseCases();
  
  // Cargar datos en paralelo
  Promise.allSettled([
    loadProductData(useCases),
    loadPaymentMethods(useCases),
    loadQuestions(useCases),
    loadReviews(useCases),
    loadNavigation(useCases)
  ]);
}, [productId]);
```

### 2. Casos de Uso
```javascript
// Cada caso de uso obtiene datos de adapters
const loadProductData = async (useCases) => {
  try {
    updateLoading('product', true);
    const productData = await useCases.getProductInfo.execute(productId);
    updateState({ product: productData });
    updateLoading('product', false);
  } catch (error) {
    updateError('product', error.message);
  }
};
```

### 3. Adapters
```javascript
// Los adapters usan servicios (mock o real)
class ProductAdapter {
  async getProductInfo(productId) {
    // El servicio detecta automáticamente si usar mocks
    const product = await this.productService.getById(productId);
    return this.transformProduct(product);
  }
}
```

### 4. Servicios Mock
```javascript
// En desarrollo, los servicios usan mocks automáticamente
class ProductService {
  async getById(productId) {
    if (this.shouldUseMocks()) {
      const mockService = this.mockContainer.getProductService();
      return await mockService.getProductById(productId);
    }
    // API real...
  }
}
```

## 🎮 Interacciones de Usuario

### Seleccionar Variación
```javascript
// UI Component
<button onClick={() => actions.selectVariation(variation.id)}>
  {variation.name}
</button>

// Hook
selectVariation: (variationId) => {
  updateState({ selectedVariation: variationId });
  // El precio se actualiza automáticamente via computed value
}
```

### Cambiar Cantidad
```javascript
// UI Component
<button onClick={() => actions.updateQuantity(quantity + 1)}>+</button>

// Hook
updateQuantity: (newQuantity) => {
  updateState({ 
    quantity: Math.max(1, Math.min(newQuantity, availableQuantity)) 
  });
}
```

### Agregar al Carrito
```javascript
// UI Component
<button onClick={actions.addToCart}>Agregar al carrito</button>

// Hook
addToCart: () => {
  // Lógica para agregar al carrito
  console.log('Adding to cart:', {
    productId,
    variation: selectedVariation,
    quantity
  });
}
```

## 📊 Estados de Loading

Cada sección tiene su propio estado de loading:

```javascript
loading: {
  product: true,      // ProductInfo, Galeria, Caracteristicas
  seller: true,       // Vendedor
  payment: true,      // PaymentMethods  
  questions: true,    // Questions
  reviews: true,      // Opiniones
  navigation: true    // Breadcrumbs
}
```

### Componentes de Loading
```jsx
// Cada componente maneja su loading state
const ProductInfo = ({ product, loading }) => {
  if (loading) {
    return (
      <div style={{background: '#f5f5f5'}}>
        Cargando información del producto...
      </div>
    );
  }
  
  if (!product) {
    return <div>No se pudo cargar el producto</div>;
  }
  
  return <ProductInfoContent product={product} />;
};
```

## 🔧 Configuración y Mocks

### Activar Mocks
```bash
# Los mocks se activan automáticamente en local
npm run dev:local

# O configurar manualmente
REACT_APP_USE_MOCK_DATA=true npm run dev
```

### Datos Mock Automáticos
- **Productos**: iPhone y Notebook con datos regionales (COL/ARG)
- **Precios**: En COP y ARS con cuotas realistas  
- **Imágenes**: URLs de MercadoLibre reales
- **Stock**: Cantidades variables por producto
- **Reseñas**: Calificaciones y comentarios
- **Vendedores**: Datos de reputación realistas

## 🚀 Pruebas

### Verificar Integración
```bash
# Probar integración UI + Use Cases
npm run test:ui

# Probar sistema de mocks
npm run test:mocks

# Ejecutar aplicación con mocks
npm run dev:local
```

### Casos de Prueba
1. **Carga inicial**: Todos los datos se cargan correctamente
2. **Loading states**: Cada sección muestra loading independiente
3. **Variaciones**: Cambiar variación actualiza precio e imágenes
4. **Cantidad**: Controles de cantidad respetan stock disponible
5. **Interacciones**: Botones de compra ejecutan callbacks
6. **Errores**: Manejo graceful de errores de red

## 📋 Checklist de Integración

- ✅ Hook `useProductPage` creado y funcional
- ✅ App.jsx actualizado para usar hook
- ✅ ProductInfo.jsx con datos dinámicos
- ✅ Galeria.jsx con imágenes dinámicas
- ✅ PurchaseSection.jsx con funcionalidad completa
- ✅ Estados de loading por sección
- ✅ Manejo de errores
- ✅ Interacciones de usuario implementadas
- ✅ Integración automática con mocks
- ✅ Datos regionalzados (Colombia/Argentina)
- ✅ Formateo de precios por moneda
- ✅ Gestión de stock y disponibilidad

## 🎯 Próximos Pasos

1. **Completar componentes restantes**: Vendedor, Questions, Opiniones, etc.
2. **Implementar funcionalidad de carrito**: Persistencia en localStorage
3. **Agregar navegación**: Router para múltiples productos
4. **Optimizar performance**: Lazy loading, memoization
5. **Tests unitarios**: Testing de componentes y hooks
6. **Responsive design**: Optimización móvil
7. **SEO**: Meta tags dinámicos por producto

¡La UI ya está completamente integrada con los casos de uso y funcionando con datos dinámicos! 🎉
