// Cliente HTTP para realizar llamadas a APIs
class ApiClient {
  constructor(config = {}) {
    this.baseURL = config.baseURL || 'https://api.mercadolibre.com';
    this.timeout = config.timeout || 10000;
    this.headers = {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
      ...config.headers
    };
  }

  async request(method, url, data = null, options = {}) {
    const requestConfig = {
      method: method.toUpperCase(),
      headers: { ...this.headers, ...options.headers },
      signal: AbortSignal.timeout(this.timeout)
    };

    if (data && ['POST', 'PUT', 'PATCH'].includes(requestConfig.method)) {
      requestConfig.body = JSON.stringify(data);
    }

    try {
      const response = await fetch(url, requestConfig);
      
      if (!response.ok) {
        throw new Error(`HTTP Error: ${response.status} ${response.statusText}`);
      }

      const contentType = response.headers.get('content-type');
      if (contentType && contentType.includes('application/json')) {
        const jsonData = await response.json();
        return { data: jsonData, status: response.status };
      }

      const textData = await response.text();
      return { data: textData, status: response.status };
    } catch (error) {
      if (error.name === 'TimeoutError') {
        throw new Error('Request timeout');
      }
      if (error.name === 'AbortError') {
        throw new Error('Request aborted');
      }
      throw error;
    }
  }

  async get(url, options = {}) {
    return this.request('GET', url, null, options);
  }

  async post(url, data, options = {}) {
    return this.request('POST', url, data, options);
  }

  async put(url, data, options = {}) {
    return this.request('PUT', url, data, options);
  }

  async patch(url, data, options = {}) {
    return this.request('PATCH', url, data, options);
  }

  async delete(url, options = {}) {
    return this.request('DELETE', url, null, options);
  }

  // Configurar interceptors para manejar tokens, logging, etc.
  setAuthToken(token) {
    this.headers['Authorization'] = `Bearer ${token}`;
  }

  removeAuthToken() {
    delete this.headers['Authorization'];
  }

  // Método para manejar rate limiting
  async retryRequest(fn, maxRetries = 3, delay = 1000) {
    for (let i = 0; i < maxRetries; i++) {
      try {
        return await fn();
      } catch (error) {
        if (i === maxRetries - 1) throw error;
        
        // Si es rate limiting, esperar antes de reintentar
        if (error.message.includes('429') || error.message.includes('Too Many Requests')) {
          await new Promise(resolve => setTimeout(resolve, delay * Math.pow(2, i)));
        } else {
          throw error;
        }
      }
    }
  }
}

export default ApiClient;
