// Archivo de prueba rápida del sistema de mocks
// Ejecutar con: node src/test-mocks.js

import { getMockContainer } from './mocks/index.js';
import { getConfig } from './config/index.js';

async function testMockSystem() {
  console.log('🧪 Iniciando pruebas del sistema de mocks...\n');

  try {
    // 1. Verificar configuración
    console.log('1️⃣ Verificando configuración...');
    const config = getConfig();
    console.log('- Ambiente:', config.environment);
    console.log('- Región:', config.region?.name);
    console.log('- Mocks activos:', config.features?.useMockData);
    console.log('- Mock delay:', config.mockData?.delay, 'ms\n');

    // 2. Obtener contenedor de mocks
    console.log('2️⃣ Inicializando contenedor de mocks...');
    const mockContainer = getMockContainer();
    console.log('- Container activo:', mockContainer.isActive());
    console.log('- Servicios disponibles:', Object.keys(mockContainer.services));
    console.log();

    if (!mockContainer.isActive()) {
      console.log('⚠️  Los mocks no están activos. Para activarlos:');
      console.log('   - Ejecuta: npm run dev:local');
      console.log('   - O configura: REACT_APP_USE_MOCK_DATA=true\n');
      return;
    }

    // 3. Probar ProductService
    console.log('3️⃣ Probando ProductService...');
    const productService = mockContainer.getProductService();
    
    const product = await productService.getProductById('MCO123456789');
    console.log('- Producto obtenido:', product.title);
    console.log('- Precio:', product.price.amount, product.price.currency_id);
    
    const description = await productService.getProductDescription('MCO123456789');
    console.log('- Descripción disponible:', !!description.content);
    console.log();

    // 4. Probar SellerService
    console.log('4️⃣ Probando SellerService...');
    const sellerService = mockContainer.getSellerService();
    
    const seller = await sellerService.getSellerById(123456);
    console.log('- Vendedor:', seller.nickname);
    console.log('- Reputación:', seller.reputation.level_id);
    console.log();

    // 5. Probar PaymentService
    console.log('5️⃣ Probando PaymentService...');
    const paymentService = mockContainer.getPaymentService();
    
    const methods = await paymentService.getPaymentMethods('MCO');
    console.log('- Métodos de pago disponibles:', methods.length);
    
    const installments = await paymentService.calculateInstallments(4899000, 'credit_card', 'MCO');
    console.log('- Cuotas disponibles:', installments.payer_costs?.length || 0);
    console.log();

    // 6. Probar QuestionsService
    console.log('6️⃣ Probando QuestionsService...');
    const questionsService = mockContainer.getQuestionsService();
    
    const questions = await questionsService.getQuestionsByItem('MCO123456789');
    console.log('- Preguntas del producto:', questions.questions?.length || 0);
    
    const newQuestion = await questionsService.createQuestion(
      'MCO123456789', 
      '¿Incluye cargador original?', 
      'test_user'
    );
    console.log('- Nueva pregunta creada:', newQuestion.id);
    console.log();

    // 7. Probar ReviewsService
    console.log('7️⃣ Probando ReviewsService...');
    const reviewsService = mockContainer.getReviewsService();
    
    const reviews = await reviewsService.getReviewsByItem('MCO123456789');
    console.log('- Opiniones del producto:', reviews.reviews?.length || 0);
    console.log('- Rating promedio:', reviews.rating_average);
    console.log();

    // 8. Probar integración completa
    console.log('8️⃣ Probando integración completa...');
    
    const startTime = Date.now();
    const [productData, sellerData, reviewsData, questionsData] = await Promise.all([
      productService.getProductById('MCO123456789'),
      sellerService.getSellerById(123456),
      reviewsService.getReviewsByItem('MCO123456789'),
      questionsService.getQuestionsByItem('MCO123456789')
    ]);
    const endTime = Date.now();
    
    console.log('- Datos obtenidos en paralelo:', endTime - startTime, 'ms');
    console.log('- Producto:', productData.title);
    console.log('- Vendedor:', sellerData.nickname);
    console.log('- Reviews:', reviewsData.reviews?.length || 0);
    console.log('- Preguntas:', questionsData.questions?.length || 0);
    console.log();

    console.log('✅ Todas las pruebas completadas exitosamente!');
    console.log('\n🎉 El sistema de mocks está funcionando correctamente');
    console.log('📝 Para usar en la aplicación ejecuta: npm run dev:local');

  } catch (error) {
    console.error('❌ Error en las pruebas:', error);
    console.log('\n🔧 Verifica que:');
    console.log('- Los archivos de mock existen en src/mocks/');
    console.log('- La configuración está correcta');
    console.log('- No hay errores de sintaxis en los mocks');
  }
}

// Función para mostrar ayuda
function showHelp() {
  console.log('🧪 Test del Sistema de Mocks - MercadoLibre Product Page\n');
  console.log('Uso:');
  console.log('  node src/test-mocks.js          # Ejecutar todas las pruebas');
  console.log('  node src/test-mocks.js --help   # Mostrar esta ayuda\n');
  console.log('Prerequisitos:');
  console.log('  - Tener los archivos de mock en src/mocks/');
  console.log('  - Configurar ambiente local o development');
  console.log('  - Activar mocks con REACT_APP_USE_MOCK_DATA=true\n');
  console.log('Para activar mocks:');
  console.log('  npm run dev:local  # Activa automáticamente los mocks');
}

// Ejecutar
if (process.argv.includes('--help')) {
  showHelp();
} else {
  testMockSystem().catch(console.error);
}
