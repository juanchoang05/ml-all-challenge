#!/usr/bin/env node

/**
 * Script para cambiar fácilmente entre ambientes y regiones
 * Compatible con Windows, macOS y Linux
 * Uso: node scripts/env-switch.js [ambiente] [region]
 */

import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// Colores para output en terminal
const colors = {
  red: '\x1b[31m',
  green: '\x1b[32m',
  yellow: '\x1b[33m',
  blue: '\x1b[34m',
  reset: '\x1b[0m'
};

// Configuraciones por ambiente
const environmentConfigs = {
  development: {
    NODE_ENV: 'development',
    REACT_APP_API_BASE_URL: 'https://api.mercadolibre.com',
    REACT_APP_API_TIMEOUT: '10000',
    REACT_APP_ENABLE_LOGGING: 'true',
    REACT_APP_ENABLE_CACHE: 'true',
    REACT_APP_ENABLE_REVIEWS: 'true',
    REACT_APP_ENABLE_QUESTIONS: 'true',
    REACT_APP_ENABLE_RECOMMENDATIONS: 'true',
    REACT_APP_USE_MOCK_DATA: 'false',
    REACT_APP_MOCK_DELAY: '1000',
    REACT_APP_CACHE_TTL: '300000'
  },
  production: {
    NODE_ENV: 'production',
    REACT_APP_API_BASE_URL: 'https://api.mercadolibre.com',
    REACT_APP_API_TIMEOUT: '15000',
    REACT_APP_ENABLE_LOGGING: 'false',
    REACT_APP_ENABLE_CACHE: 'true',
    REACT_APP_ENABLE_REVIEWS: 'true',
    REACT_APP_ENABLE_QUESTIONS: 'true',
    REACT_APP_ENABLE_RECOMMENDATIONS: 'true',
    REACT_APP_USE_MOCK_DATA: 'false',
    REACT_APP_MOCK_DELAY: '0',
    REACT_APP_CACHE_TTL: '600000'
  },
  testing: {
    NODE_ENV: 'testing',
    REACT_APP_API_BASE_URL: 'https://api.mercadolibre.com',
    REACT_APP_API_TIMEOUT: '5000',
    REACT_APP_ENABLE_LOGGING: 'true',
    REACT_APP_ENABLE_CACHE: 'false',
    REACT_APP_ENABLE_REVIEWS: 'true',
    REACT_APP_ENABLE_QUESTIONS: 'true',
    REACT_APP_ENABLE_RECOMMENDATIONS: 'false',
    REACT_APP_USE_MOCK_DATA: 'true',
    REACT_APP_MOCK_DELAY: '100',
    REACT_APP_CACHE_TTL: '0'
  },
  local: {
    NODE_ENV: 'development',
    REACT_APP_API_BASE_URL: 'http://localhost:3001/api',
    REACT_APP_API_TIMEOUT: '8000',
    REACT_APP_ENABLE_LOGGING: 'true',
    REACT_APP_ENABLE_CACHE: 'false',
    REACT_APP_ENABLE_REVIEWS: 'true',
    REACT_APP_ENABLE_QUESTIONS: 'true',
    REACT_APP_ENABLE_RECOMMENDATIONS: 'true',
    REACT_APP_USE_MOCK_DATA: 'true',
    REACT_APP_MOCK_DELAY: '1000',
    REACT_APP_CACHE_TTL: '0'
  }
};

// Regiones válidas
const validRegions = ['colombia', 'argentina', 'mexico', 'brazil', 'chile'];
const validEnvironments = ['development', 'production', 'testing', 'local'];

function showHelp() {
  console.log(`${colors.blue}MercadoLibre Product Page - Environment Switcher${colors.reset}`);
  console.log('');
  console.log(`Uso: node ${process.argv[1]} [ambiente] [region]`);
  console.log('');
  console.log(`${colors.yellow}Ambientes disponibles:${colors.reset}`);
  console.log('  development  - Desarrollo local con todas las features');
  console.log('  production   - Configuración optimizada para producción');
  console.log('  testing      - Ambiente para testing con mocks');
  console.log('  local        - Desarrollo local con servidor mock');
  console.log('');
  console.log(`${colors.yellow}Regiones disponibles:${colors.reset}`);
  console.log('  colombia     - MercadoLibre Colombia (MCO)');
  console.log('  argentina    - MercadoLibre Argentina (MLA)');
  console.log('  mexico       - MercadoLibre México (MLM)');
  console.log('  brazil       - MercadoLibre Brasil (MLB)');
  console.log('  chile        - MercadoLibre Chile (MLC)');
  console.log('');
  console.log(`${colors.yellow}Ejemplos:${colors.reset}`);
  console.log(`  node ${process.argv[1]} development colombia`);
  console.log(`  node ${process.argv[1]} testing argentina`);
  console.log(`  node ${process.argv[1]} production mexico`);
  console.log('');
}

function showCurrentStatus() {
  const envPath = path.join(process.cwd(), '.env');
  
  if (fs.existsSync(envPath)) {
    console.log(`${colors.blue}Configuración actual:${colors.reset}`);
    
    try {
      const envContent = fs.readFileSync(envPath, 'utf8');
      const envLines = envContent.split('\n');
      
      const getEnvValue = (key) => {
        const line = envLines.find(l => l.startsWith(`${key}=`));
        return line ? line.split('=')[1] : 'No definido';
      };
      
      console.log(`${colors.yellow}NODE_ENV:${colors.reset} ${getEnvValue('NODE_ENV')}`);
      console.log(`${colors.yellow}REGION:${colors.reset} ${getEnvValue('REACT_APP_REGION')}`);
      console.log(`${colors.yellow}MOCK_DATA:${colors.reset} ${getEnvValue('REACT_APP_USE_MOCK_DATA')}`);
      console.log(`${colors.yellow}LOGGING:${colors.reset} ${getEnvValue('REACT_APP_ENABLE_LOGGING')}`);
      console.log('');
    } catch (error) {
      console.log(`${colors.red}Error leyendo archivo .env${colors.reset}`);
    }
  } else {
    console.log(`${colors.yellow}No existe archivo .env${colors.reset}`);
    console.log('');
  }
}

function createEnvFile(environment, region) {
  console.log(`${colors.blue}Creando archivo .env para ambiente: ${colors.yellow}${environment}${colors.blue}, región: ${colors.yellow}${region}${colors.reset}`);
  
  if (!environmentConfigs[environment]) {
    console.log(`${colors.red}Error: Ambiente '${environment}' no reconocido${colors.reset}`);
    return false;
  }
  
  const config = { ...environmentConfigs[environment] };
  config.REACT_APP_REGION = region;
  
  // Crear contenido del archivo .env
  let envContent = `# Ambiente: ${environment.charAt(0).toUpperCase() + environment.slice(1)}\n`;
  envContent += `# Región: ${region.charAt(0).toUpperCase() + region.slice(1)}\n`;
  envContent += `# Generado automáticamente el ${new Date().toLocaleString()}\n\n`;
  
  // Agrupar variables por categoría
  const categories = {
    'Ambiente': ['NODE_ENV', 'REACT_APP_REGION'],
    'API Configuration': ['REACT_APP_API_BASE_URL', 'REACT_APP_API_TIMEOUT'],
    'Features': [
      'REACT_APP_ENABLE_LOGGING',
      'REACT_APP_ENABLE_CACHE', 
      'REACT_APP_ENABLE_REVIEWS',
      'REACT_APP_ENABLE_QUESTIONS',
      'REACT_APP_ENABLE_RECOMMENDATIONS'
    ],
    'Development': ['REACT_APP_USE_MOCK_DATA', 'REACT_APP_MOCK_DELAY', 'REACT_APP_CACHE_TTL']
  };
  
  Object.entries(categories).forEach(([category, keys]) => {
    envContent += `# ${category}\n`;
    keys.forEach(key => {
      if (config[key] !== undefined) {
        envContent += `${key}=${config[key]}\n`;
      }
    });
    envContent += '\n';
  });
  
  // Agregar variables opcionales vacías
  envContent += '# Analytics (opcional)\n';
  envContent += 'REACT_APP_GA_ID=\n';
  envContent += 'REACT_APP_HOTJAR_ID=\n';
  envContent += 'REACT_APP_SENTRY_DSN=\n';
  
  try {
    const envPath = path.join(process.cwd(), '.env');
    
    // Crear backup si existe
    if (fs.existsSync(envPath)) {
      const backupPath = path.join(process.cwd(), '.env.backup');
      fs.copyFileSync(envPath, backupPath);
      console.log(`${colors.yellow}Backup creado: .env.backup${colors.reset}`);
    }
    
    fs.writeFileSync(envPath, envContent);
    console.log(`${colors.green}✓ Archivo .env creado exitosamente${colors.reset}`);
    return true;
  } catch (error) {
    console.log(`${colors.red}Error creando archivo .env: ${error.message}${colors.reset}`);
    return false;
  }
}

function main() {
  const args = process.argv.slice(2);
  
  // Mostrar estado actual
  showCurrentStatus();
  
  // Verificar argumentos
  if (args.length === 0 || args[0] === '--help' || args[0] === '-h') {
    showHelp();
    return;
  }
  
  const environment = args[0] || 'development';
  const region = args[1] || 'colombia';
  
  // Validar ambiente
  if (!validEnvironments.includes(environment)) {
    console.log(`${colors.red}Error: Ambiente '${environment}' no válido${colors.reset}`);
    console.log(`${colors.yellow}Ambientes válidos: ${validEnvironments.join(', ')}${colors.reset}`);
    return;
  }
  
  // Validar región
  if (!validRegions.includes(region)) {
    console.log(`${colors.red}Error: Región '${region}' no válida${colors.reset}`);
    console.log(`${colors.yellow}Regiones válidas: ${validRegions.join(', ')}${colors.reset}`);
    return;
  }
  
  // Crear archivo .env
  if (createEnvFile(environment, region)) {
    console.log('');
    console.log(`${colors.green}🚀 Configuración lista!${colors.reset}`);
    console.log(`${colors.blue}Para iniciar el desarrollo:${colors.reset}`);
    console.log('  npm run dev');
    console.log('');
    console.log(`${colors.blue}Para ver la configuración actual:${colors.reset}`);
    console.log('  cat .env    (Linux/macOS)');
    console.log('  type .env   (Windows)');
    console.log('');
  }
}

// Ejecutar si se llama directamente
if (require.main === module) {
  main();
}

module.exports = {
  createEnvFile,
  showCurrentStatus,
  showHelp,
  environmentConfigs,
  validRegions,
  validEnvironments
};
