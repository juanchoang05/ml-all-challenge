#!/usr/bin/env node

/**
 * Script para verificar que la configuración de ambientes funcione correctamente
 * Uso: node scripts/verify-config.js
 */

const fs = require('fs');
const path = require('path');

const colors = {
  red: '\x1b[31m',
  green: '\x1b[32m',
  yellow: '\x1b[33m',
  blue: '\x1b[34m',
  cyan: '\x1b[36m',
  reset: '\x1b[0m'
};

function checkFile(filePath, description) {
  const exists = fs.existsSync(filePath);
  const status = exists ? `${colors.green}✓${colors.reset}` : `${colors.red}✗${colors.reset}`;
  console.log(`${status} ${description}: ${filePath}`);
  return exists;
}

function checkEnvFile() {
  console.log(`\n${colors.blue}=== Verificando archivo .env ===${colors.reset}`);
  
  const envPath = path.join(process.cwd(), '.env');
  const envExamplePath = path.join(process.cwd(), '.env.example');
  
  const hasEnv = checkFile(envPath, 'Archivo .env');
  const hasEnvExample = checkFile(envExamplePath, 'Archivo .env.example');
  
  if (hasEnv) {
    try {
      const envContent = fs.readFileSync(envPath, 'utf8');
      const envLines = envContent.split('\n').filter(line => 
        line.trim() && !line.startsWith('#')
      );
      
      console.log(`${colors.cyan}Variables encontradas en .env:${colors.reset}`);
      envLines.forEach(line => {
        const [key, value] = line.split('=');
        if (key && value !== undefined) {
          console.log(`  ${key} = ${value}`);
        }
      });
      
      // Verificar variables críticas
      const criticalVars = [
        'NODE_ENV',
        'REACT_APP_REGION',
        'REACT_APP_API_BASE_URL'
      ];
      
      console.log(`\n${colors.cyan}Verificando variables críticas:${colors.reset}`);
      criticalVars.forEach(varName => {
        const line = envLines.find(l => l.startsWith(`${varName}=`));
        const status = line ? `${colors.green}✓${colors.reset}` : `${colors.red}✗${colors.reset}`;
        const value = line ? line.split('=')[1] : 'No definida';
        console.log(`${status} ${varName}: ${value}`);
      });
      
    } catch (error) {
      console.log(`${colors.red}Error leyendo .env: ${error.message}${colors.reset}`);
    }
  }
  
  return hasEnv && hasEnvExample;
}

function checkConfigFiles() {
  console.log(`\n${colors.blue}=== Verificando archivos de configuración ===${colors.reset}`);
  
  const configFiles = [
    'src/config/index.js',
    'src/config/development.js',
    'src/config/production.js',
    'src/config/testing.js',
    'src/config/local.js',
    'src/config/regions.js',
    'src/config/environment.js'
  ];
  
  let allExists = true;
  configFiles.forEach(file => {
    const exists = checkFile(file, 'Config file');
    allExists = allExists && exists;
  });
  
  return allExists;
}

function checkScripts() {
  console.log(`\n${colors.blue}=== Verificando scripts ===${colors.reset}`);
  
  const scripts = [
    'scripts/env-switch.sh',
    'scripts/env-switch.js'
  ];
  
  let allExists = true;
  scripts.forEach(script => {
    const exists = checkFile(script, 'Script');
    allExists = allExists && exists;
    
    if (exists && script.endsWith('.sh')) {
      // Verificar permisos de ejecución en sistemas Unix
      try {
        const stats = fs.statSync(script);
        const isExecutable = !!(stats.mode & parseInt('111', 8));
        if (isExecutable) {
          console.log(`  ${colors.green}✓${colors.reset} Permisos de ejecución: OK`);
        } else {
          console.log(`  ${colors.yellow}!${colors.reset} Sin permisos de ejecución (ejecutar: chmod +x ${script})`);
        }
      } catch (error) {
        console.log(`  ${colors.red}✗${colors.reset} Error verificando permisos: ${error.message}`);
      }
    }
  });
  
  return allExists;
}

function checkPackageJsonScripts() {
  console.log(`\n${colors.blue}=== Verificando scripts en package.json ===${colors.reset}`);
  
  try {
    const packagePath = path.join(process.cwd(), 'package.json');
    const packageJson = JSON.parse(fs.readFileSync(packagePath, 'utf8'));
    
    const expectedScripts = [
      'dev',
      'dev:local',
      'dev:testing',
      'dev:colombia',
      'dev:argentina',
      'build:production',
      'env:switch',
      'env:status'
    ];
    
    expectedScripts.forEach(scriptName => {
      const exists = !!packageJson.scripts[scriptName];
      const status = exists ? `${colors.green}✓${colors.reset}` : `${colors.red}✗${colors.reset}`;
      console.log(`${status} npm run ${scriptName}`);
    });
    
    return true;
  } catch (error) {
    console.log(`${colors.red}Error leyendo package.json: ${error.message}${colors.reset}`);
    return false;
  }
}

function showQuickStart() {
  console.log(`\n${colors.blue}=== Guía de inicio rápido ===${colors.reset}`);
  console.log(`
${colors.cyan}1. Configurar ambiente:${colors.reset}
   npm run env:switch development colombia

${colors.cyan}2. Iniciar desarrollo:${colors.reset}
   npm run dev

${colors.cyan}3. Cambiar región:${colors.reset}
   npm run dev:argentina
   npm run dev:mexico
   npm run dev:brazil

${colors.cyan}4. Modo testing:${colors.reset}
   npm run test:colombia

${colors.cyan}5. Build para producción:${colors.reset}
   npm run build:production

${colors.cyan}6. Ver estado actual:${colors.reset}
   npm run env:status

${colors.cyan}7. Ayuda:${colors.reset}
   npm run env:help
`);
}

function main() {
  console.log(`${colors.blue}MercadoLibre Product Page - Verificación de Configuración${colors.reset}`);
  console.log(`${colors.cyan}Verificando configuración del proyecto...${colors.reset}`);
  
  const checks = [
    checkEnvFile(),
    checkConfigFiles(), 
    checkScripts(),
    checkPackageJsonScripts()
  ];
  
  const allPassed = checks.every(check => check);
  
  console.log(`\n${colors.blue}=== Resumen ===${colors.reset}`);
  if (allPassed) {
    console.log(`${colors.green}✓ Todas las verificaciones pasaron correctamente${colors.reset}`);
    console.log(`${colors.green}El proyecto está listo para desarrollo multi-ambiente${colors.reset}`);
  } else {
    console.log(`${colors.red}✗ Algunas verificaciones fallaron${colors.reset}`);
    console.log(`${colors.yellow}Revisa los errores anteriores y ejecuta los comandos sugeridos${colors.reset}`);
  }
  
  showQuickStart();
}

if (require.main === module) {
  main();
}
