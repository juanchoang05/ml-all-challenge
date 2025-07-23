// Configuración principal que maneja los diferentes ambientes
import developmentConfig from './development.js';
import productionConfig from './production.js';
import testingConfig from './testing.js';
import localConfig from './local.js';
import { getRegionConfig, detectRegionFromUrl } from './regions.js';
import { getEnvConfig } from './environment.js';

class ConfigManager {
  constructor() {
    this.environment = this.getEnvironment();
    this.region = this.getRegion();
    this.envVars = getEnvConfig(this.environment);
    this.regionConfig = getRegionConfig(this.region);
    this.config = this.loadConfig();
  }

  getEnvironment() {
    // Detectar ambiente desde variables de entorno o parámetros
    if (typeof window !== 'undefined') {
      // En el browser
      return window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1' 
        ? 'local' 
        : 'production';
    } else {
      // En Node.js
      return process.env.NODE_ENV || 'development';
    }
  }

  getRegion() {
    // Detectar región desde URL o variables de entorno
    if (typeof window !== 'undefined') {
      return detectRegionFromUrl();
    }
    return process.env.REACT_APP_REGION || 'colombia';
  }

  loadConfig() {
    let baseConfig;
    
    switch (this.environment) {
      case 'production':
        baseConfig = productionConfig;
        break;
      case 'testing':
      case 'test':
        baseConfig = testingConfig;
        break;
      case 'local':
        baseConfig = localConfig;
        break;
      case 'development':
      default:
        baseConfig = developmentConfig;
        break;
    }

    // Merge con configuraciones específicas
    return {
      ...baseConfig,
      api: {
        ...baseConfig.api,
        baseUrl: this.envVars.API_BASE_URL || baseConfig.api.baseUrl,
        timeout: this.envVars.API_TIMEOUT || baseConfig.api.timeout
      },
      defaults: {
        ...baseConfig.defaults,
        ...this.regionConfig
      },
      features: {
        ...baseConfig.features,
        enableLogging: this.envVars.ENABLE_LOGGING,
        enableCaching: this.envVars.ENABLE_CACHE,
        enableReviews: this.envVars.ENABLE_REVIEWS,
        enableQuestions: this.envVars.ENABLE_QUESTIONS,
        enableRecommendations: this.envVars.ENABLE_RECOMMENDATIONS,
        useMockData: this.envVars.USE_MOCK_DATA
      },
      analytics: {
        googleAnalyticsId: this.envVars.GOOGLE_ANALYTICS_ID,
        hotjarId: this.envVars.HOTJAR_ID,
        sentryDsn: this.envVars.SENTRY_DSN
      },
      region: this.regionConfig,
      environment: this.environment
    };
  }

  get(path) {
    return this.getNestedProperty(this.config, path);
  }

  getNestedProperty(obj, path) {
    return path.split('.').reduce((current, key) => current?.[key], obj);
  }

  getApiConfig() {
    return this.config.api;
  }

  getEndpoint(endpointName) {
    return this.config.endpoints[endpointName];
  }

  getBaseUrl() {
    return this.config.api.baseUrl;
  }

  buildUrl(endpointName, params = {}) {
    const endpoint = this.getEndpoint(endpointName);
    if (!endpoint) {
      throw new Error(`Endpoint '${endpointName}' not found`);
    }

    let url = this.getBaseUrl() + endpoint;
    
    // Reemplazar parámetros en la URL
    Object.entries(params).forEach(([key, value]) => {
      url = url.replace(`{${key}}`, encodeURIComponent(value));
    });

    return url;
  }

  getFeature(featureName) {
    return this.config.features[featureName];
  }

  getDefault(defaultName) {
    return this.config.defaults[defaultName];
  }

  isFeatureEnabled(featureName) {
    return this.getFeature(featureName) === true;
  }

  // Método para configuración personalizada
  override(customConfig) {
    this.config = { ...this.config, ...customConfig };
    return this;
  }

  // Método para agregar headers personalizados
  getHeaders(additionalHeaders = {}) {
    const defaultHeaders = {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
      'User-Agent': 'ml-product-app/1.0.0'
    };

    return { ...defaultHeaders, ...additionalHeaders };
  }

  // Método para logging en desarrollo
  log(message, data = null) {
    if (this.isFeatureEnabled('enableLogging')) {
      console.log(`[${this.environment.toUpperCase()}] ${message}`, data || '');
    }
  }

  // Método para warnings
  warn(message, data = null) {
    if (this.isFeatureEnabled('enableLogging')) {
      console.warn(`[${this.environment.toUpperCase()}] ${message}`, data || '');
    }
  }
}

// Instancia singleton
let configInstance = null;

export const getConfig = () => {
  if (!configInstance) {
    configInstance = new ConfigManager();
  }
  return configInstance;
};

export const initConfig = (customConfig = {}) => {
  configInstance = new ConfigManager().override(customConfig);
  return configInstance;
};

export default getConfig;
