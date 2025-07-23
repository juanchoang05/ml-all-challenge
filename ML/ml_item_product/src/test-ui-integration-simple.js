// Script para probar la integración UI con datos mock
// Ejecutar con: npm run test:ui

console.log('🧪 Probando integración UI con datos mock...\n');

async function testUIIntegration() {
  try {
    console.log('1️⃣ Verificando estructura de archivos...');
    
    const fs = await import('fs');
    
    const requiredFiles = [
      'src/ui/hooks/useProductPageSimple.js',
      'src/ui/App.jsx',
      'src/ui/ProductInfo.jsx',
      'src/ui/Galeria.jsx',
      'src/ui/PurchaseSection.jsx'
    ];
    
    let allFilesExist = true;
    
    for (const file of requiredFiles) {
      if (fs.existsSync(file)) {
        console.log(`  ✅ ${file}`);
      } else {
        console.log(`  ❌ ${file} - NO ENCONTRADO`);
        allFilesExist = false;
      }
    }
    
    if (!allFilesExist) {
      console.log('\n❌ Algunos archivos necesarios no existen');
      return;
    }
    
    console.log('\n2️⃣ Verificando hook useProductPageSimple...');
    
    const hookContent = fs.readFileSync('src/ui/hooks/useProductPageSimple.js', 'utf8');
    
    const hookChecks = [
      { name: 'exporta useProductPage', pattern: /export.*useProductPage/ },
      { name: 'tiene datos mock', pattern: /mockProduct.*=/ },
      { name: 'gestiona loading states', pattern: /loading:.*{/ },
      { name: 'tiene acciones', pattern: /actions.*=.*{/ }
    ];
    
    for (const check of hookChecks) {
      if (check.pattern.test(hookContent)) {
        console.log(`  ✅ ${check.name}`);
      } else {
        console.log(`  ❌ ${check.name}`);
      }
    }
    
    console.log('\n3️⃣ Verificando App.jsx...');
    
    const appContent = fs.readFileSync('src/ui/App.jsx', 'utf8');
    if (appContent.includes('useProductPageSimple')) {
      console.log('  ✅ App.jsx usa el hook simplificado');
    } else {
      console.log('  ❌ App.jsx no importa el hook correcto');
    }
    
    console.log('\n✅ Pruebas completadas!');
    console.log('\n🚀 Para probar:');
    console.log('   npm run dev:local');
    console.log('   Abrir: http://localhost:5173');
    
  } catch (error) {
    console.error('❌ Error:', error.message);
  }
}

testUIIntegration().catch(console.error);
