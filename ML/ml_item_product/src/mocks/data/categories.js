// Mock data para categorías
export const mockCategories = {
  'MCO1055': {
    id: 'MCO1055',
    name: 'Celulares y Teléfonos',
    picture: 'https://http2.mlstatic.com/resources/frontend/statics/categories/cellphones.png',
    permalink: 'https://listado.mercadolibre.com.co/celulares-telefonia',
    total_items_in_this_category: 125420,
    path_from_root: [
      {
        id: 'MCO1000',
        name: 'Tecnología'
      },
      {
        id: 'MCO1055',
        name: 'Celulares y Teléfonos'
      }
    ],
    children_categories: [
      {
        id: 'MCO1055001',
        name: 'Celulares y Smartphones',
        total_items_in_this_category: 89567
      },
      {
        id: 'MCO1055002',
        name: 'Accesorios para Celulares',
        total_items_in_this_category: 25863
      },
      {
        id: 'MCO1055003',
        name: 'Teléfonos Fijos',
        total_items_in_this_category: 9990
      }
    ],
    attribute_types: 'REQUIRED',
    predictor: {
      id: 'brand',
      name: 'Marca'
    },
    attributes: [
      {
        id: 'BRAND',
        name: 'Marca',
        type: 'list',
        required: true,
        values: [
          { id: 'APPLE', name: 'Apple' },
          { id: 'SAMSUNG', name: 'Samsung' },
          { id: 'XIAOMI', name: 'Xiaomi' },
          { id: 'HUAWEI', name: 'Huawei' },
          { id: 'MOTOROLA', name: 'Motorola' }
        ]
      },
      {
        id: 'MODEL',
        name: 'Modelo',
        type: 'string',
        required: true
      },
      {
        id: 'STORAGE_CAPACITY',
        name: 'Capacidad de almacenamiento',
        type: 'list',
        required: false,
        values: [
          { id: '64GB', name: '64 GB' },
          { id: '128GB', name: '128 GB' },
          { id: '256GB', name: '256 GB' },
          { id: '512GB', name: '512 GB' },
          { id: '1TB', name: '1 TB' }
        ]
      }
    ]
  },
  'MLA1652': {
    id: 'MLA1652',
    name: 'Computación',
    picture: 'https://http2.mlstatic.com/resources/frontend/statics/categories/computing.png',
    permalink: 'https://listado.mercadolibre.com.ar/computacion',
    total_items_in_this_category: 78950,
    path_from_root: [
      {
        id: 'MLA1000',
        name: 'Tecnología'
      },
      {
        id: 'MLA1652',
        name: 'Computación'
      }
    ],
    children_categories: [
      {
        id: 'MLA1652001',
        name: 'Notebooks',
        total_items_in_this_category: 34567
      },
      {
        id: 'MLA1652002',
        name: 'PC de Escritorio',
        total_items_in_this_category: 15890
      },
      {
        id: 'MLA1652003',
        name: 'Tablets',
        total_items_in_this_category: 12493
      },
      {
        id: 'MLA1652004',
        name: 'Accesorios para PC',
        total_items_in_this_category: 16000
      }
    ],
    attributes: [
      {
        id: 'BRAND',
        name: 'Marca',
        type: 'list',
        required: true,
        values: [
          { id: 'LENOVO', name: 'Lenovo' },
          { id: 'HP', name: 'HP' },
          { id: 'DELL', name: 'Dell' },
          { id: 'ASUS', name: 'Asus' },
          { id: 'ACER', name: 'Acer' }
        ]
      },
      {
        id: 'RAM_MEMORY',
        name: 'Memoria RAM',
        type: 'list',
        required: false,
        values: [
          { id: '4GB', name: '4 GB' },
          { id: '8GB', name: '8 GB' },
          { id: '16GB', name: '16 GB' },
          { id: '32GB', name: '32 GB' }
        ]
      }
    ]
  }
};

export const mockSiteCategories = {
  'MCO': [
    {
      id: 'MCO1000',
      name: 'Tecnología',
      children: [
        { id: 'MCO1055', name: 'Celulares y Teléfonos' },
        { id: 'MCO1652', name: 'Computación' },
        { id: 'MCO1144', name: 'Consolas y Videojuegos' }
      ]
    },
    {
      id: 'MCO1430',
      name: 'Ropa y Accesorios',
      children: [
        { id: 'MCO1430001', name: 'Ropa para Hombre' },
        { id: 'MCO1430002', name: 'Ropa para Mujer' },
        { id: 'MCO1430003', name: 'Calzado' }
      ]
    },
    {
      id: 'MCO1499',
      name: 'Industrias y Oficinas',
      children: [
        { id: 'MCO1499001', name: 'Oficina' },
        { id: 'MCO1499002', name: 'Industria Gastronómica' }
      ]
    }
  ],
  'MLA': [
    {
      id: 'MLA1000',
      name: 'Tecnología',
      children: [
        { id: 'MLA1055', name: 'Celulares y Teléfonos' },
        { id: 'MLA1652', name: 'Computación' },
        { id: 'MLA1144', name: 'Consolas y Videojuegos' }
      ]
    },
    {
      id: 'MLA1430',
      name: 'Ropa y Accesorios',
      children: [
        { id: 'MLA1430001', name: 'Ropa para Hombre' },
        { id: 'MLA1430002', name: 'Ropa para Mujer' },
        { id: 'MLA1430003', name: 'Calzado' }
      ]
    }
  ]
};

export const getCategoryById = (categoryId) => {
  return mockCategories[categoryId] || null;
};

export const getCategoriesBySite = (siteId) => {
  return mockSiteCategories[siteId] || [];
};

export const searchCategories = (query, siteId = 'MCO') => {
  const siteCategories = getCategoriesBySite(siteId);
  const allCategories = siteCategories.flatMap(parent => 
    [parent, ...parent.children]
  );
  
  if (!query) {
    return allCategories;
  }
  
  return allCategories.filter(category => 
    category.name.toLowerCase().includes(query.toLowerCase())
  );
};

export const getCategoryBreadcrumb = (categoryId) => {
  const category = getCategoryById(categoryId);
  return category?.path_from_root || [];
};

export const getTopCategories = (siteId = 'MCO') => {
  const categories = getCategoriesBySite(siteId);
  return categories.slice(0, 6); // Top 6 categorías principales
};
