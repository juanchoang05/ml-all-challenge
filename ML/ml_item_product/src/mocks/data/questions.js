// Mock data para preguntas y respuestas
export const mockQuestions = {
  'MCO123456789': [
    {
      id: 12345001,
      text: '¿El iPhone viene con cargador incluido?',
      status: 'ANSWERED',
      date_created: '2024-01-18T10:30:00.000Z',
      item_id: 'MCO123456789',
      seller_id: 123456,
      from: {
        id: 555666777,
        answered_questions: 45
      },
      answer: {
        text: 'Sí, el iPhone 14 Pro Max viene con cable USB-C a Lightning y adaptador de corriente de 20W incluidos en la caja.',
        status: 'ACTIVE',
        date_created: '2024-01-18T14:22:00.000Z'
      },
      deleted_from_listing: false,
      hold: false,
      tags: []
    },
    {
      id: 12345002,
      text: '¿Cuánto tiempo de garantía tiene?',
      status: 'ANSWERED',
      date_created: '2024-01-19T09:15:00.000Z',
      item_id: 'MCO123456789',
      seller_id: 123456,
      from: {
        id: 444555666,
        answered_questions: 23
      },
      answer: {
        text: 'El iPhone cuenta con 12 meses de garantía oficial de Apple más 6 meses adicionales de garantía extendida que ofrecemos nosotros.',
        status: 'ACTIVE',
        date_created: '2024-01-19T11:30:00.000Z'
      },
      deleted_from_listing: false,
      hold: false,
      tags: []
    },
    {
      id: 12345003,
      text: '¿Está liberado para todas las operadoras?',
      status: 'ANSWERED',
      date_created: '2024-01-20T16:45:00.000Z',
      item_id: 'MCO123456789',
      seller_id: 123456,
      from: {
        id: 333444555,
        answered_questions: 12
      },
      answer: {
        text: 'Sí, completamente liberado para usar con Claro, Movistar, Tigo y cualquier otra operadora en Colombia y el exterior.',
        status: 'ACTIVE',
        date_created: '2024-01-20T18:20:00.000Z'
      },
      deleted_from_listing: false,
      hold: false,
      tags: []
    },
    {
      id: 12345004,
      text: '¿Hacen envíos a Medellín?',
      status: 'ANSWERED',
      date_created: '2024-01-21T08:30:00.000Z',
      item_id: 'MCO123456789',
      seller_id: 123456,
      from: {
        id: 222333444,
        answered_questions: 67
      },
      answer: {
        text: 'Sí, realizamos envíos a toda Colombia. Para Medellín el envío es gratis por Mercado Envíos y llega en 2-3 días hábiles.',
        status: 'ACTIVE',
        date_created: '2024-01-21T10:15:00.000Z'
      },
      deleted_from_listing: false,
      hold: false,
      tags: []
    },
    {
      id: 12345005,
      text: '¿El color morado es exactamente como en las fotos?',
      status: 'UNANSWERED',
      date_created: '2024-01-22T13:45:00.000Z',
      item_id: 'MCO123456789',
      seller_id: 123456,
      from: {
        id: 111222333,
        answered_questions: 8
      },
      answer: null,
      deleted_from_listing: false,
      hold: false,
      tags: []
    }
  ],
  'MLA987654321': [
    {
      id: 98765001,
      text: '¿La notebook viene con Windows instalado?',
      status: 'ANSWERED',
      date_created: '2024-01-15T11:20:00.000Z',
      item_id: 'MLA987654321',
      seller_id: 987654,
      from: {
        id: 777888999,
        answered_questions: 34
      },
      answer: {
        text: 'Sí, viene con Windows 11 Home original ya instalado y activado.',
        status: 'ACTIVE',
        date_created: '2024-01-15T15:45:00.000Z'
      },
      deleted_from_listing: false,
      hold: false,
      tags: []
    },
    {
      id: 98765002,
      text: '¿Se puede ampliar la memoria RAM?',
      status: 'ANSWERED',
      date_created: '2024-01-16T14:30:00.000Z',
      item_id: 'MLA987654321',
      seller_id: 987654,
      from: {
        id: 666777888,
        answered_questions: 56
      },
      answer: {
        text: 'Sí, tiene 1 slot libre para expandir hasta 32GB en total. Actualmente viene con 8GB soldados + 1 slot vacío.',
        status: 'ACTIVE',
        date_created: '2024-01-16T16:10:00.000Z'
      },
      deleted_from_listing: false,
      hold: false,
      tags: []
    }
  ]
};

export const getQuestionsByItemId = (itemId, options = {}) => {
  const { limit = 50, offset = 0, status = 'ALL' } = options;
  const questions = mockQuestions[itemId] || [];
  
  let filteredQuestions = questions;
  
  if (status !== 'ALL') {
    filteredQuestions = questions.filter(q => q.status === status);
  }
  
  const results = filteredQuestions.slice(offset, offset + limit);
  
  return {
    total: filteredQuestions.length,
    limit,
    offset,
    questions: results
  };
};

export const getQuestionById = (questionId) => {
  const allQuestions = Object.values(mockQuestions).flat();
  return allQuestions.find(q => q.id === questionId) || null;
};

export const createQuestion = (itemId, text, userId) => {
  const newQuestion = {
    id: Date.now(),
    text,
    status: 'UNANSWERED',
    date_created: new Date().toISOString(),
    item_id: itemId,
    seller_id: null, // Se asignaría basado en el item
    from: {
      id: userId,
      answered_questions: 0
    },
    answer: null,
    deleted_from_listing: false,
    hold: false,
    tags: []
  };
  
  if (!mockQuestions[itemId]) {
    mockQuestions[itemId] = [];
  }
  
  mockQuestions[itemId].unshift(newQuestion);
  return newQuestion;
};

export const answerQuestion = (questionId, answerText) => {
  const allQuestions = Object.values(mockQuestions).flat();
  const question = allQuestions.find(q => q.id === questionId);
  
  if (question) {
    question.status = 'ANSWERED';
    question.answer = {
      text: answerText,
      status: 'ACTIVE',
      date_created: new Date().toISOString()
    };
    return question;
  }
  
  return null;
};
