// Mock Service para QuestionsService
import { 
  getQuestionsByItemId, 
  getQuestionById, 
  createQuestion, 
  answerQuestion, 
  simulateNetworkDelay, 
  simulateRandomError, 
  mockLogger 
} from '../data/index.js';

export class MockQuestionsService {
  constructor(config = {}) {
    this.config = config;
    this.serviceName = 'QuestionsService';
  }

  async getQuestionsByItem(itemId, options = {}) {
    mockLogger.log(this.serviceName, 'getQuestionsByItem', { itemId, options });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 600);
      simulateRandomError(0.03);
      
      const result = getQuestionsByItemId(itemId, options);
      
      return {
        success: true,
        data: result
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'getQuestionsByItem', error);
      throw error;
    }
  }

  async getQuestionById(questionId) {
    mockLogger.log(this.serviceName, 'getQuestionById', { questionId });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 400);
      simulateRandomError(0.02);
      
      const question = getQuestionById(parseInt(questionId));
      
      if (!question) {
        throw new Error(`Question with id ${questionId} not found`);
      }
      
      return {
        success: true,
        data: question
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'getQuestionById', error);
      throw error;
    }
  }

  async createQuestion(itemId, questionText, userId) {
    mockLogger.log(this.serviceName, 'createQuestion', { itemId, questionText, userId });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 800);
      simulateRandomError(0.04);
      
      const newQuestion = createQuestion(itemId, questionText, userId);
      
      return {
        success: true,
        data: newQuestion
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'createQuestion', error);
      throw error;
    }
  }

  async answerQuestion(questionId, answerText) {
    mockLogger.log(this.serviceName, 'answerQuestion', { questionId, answerText });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 700);
      simulateRandomError(0.03);
      
      const answeredQuestion = answerQuestion(parseInt(questionId), answerText);
      
      if (!answeredQuestion) {
        throw new Error(`Question with id ${questionId} not found`);
      }
      
      return {
        success: true,
        data: answeredQuestion
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'answerQuestion', error);
      throw error;
    }
  }

  async getAnsweredQuestions(itemId, options = {}) {
    mockLogger.log(this.serviceName, 'getAnsweredQuestions', { itemId, options });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 500);
      simulateRandomError(0.02);
      
      const result = getQuestionsByItemId(itemId, { 
        ...options, 
        status: 'ANSWERED' 
      });
      
      return {
        success: true,
        data: result
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'getAnsweredQuestions', error);
      throw error;
    }
  }

  async getUnansweredQuestions(itemId, options = {}) {
    mockLogger.log(this.serviceName, 'getUnansweredQuestions', { itemId, options });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 500);
      simulateRandomError(0.02);
      
      const result = getQuestionsByItemId(itemId, { 
        ...options, 
        status: 'UNANSWERED' 
      });
      
      return {
        success: true,
        data: result
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'getUnansweredQuestions', error);
      throw error;
    }
  }

  async searchQuestions(itemId, searchText, options = {}) {
    mockLogger.log(this.serviceName, 'searchQuestions', { itemId, searchText, options });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 600);
      simulateRandomError(0.03);
      
      const allQuestions = getQuestionsByItemId(itemId, { 
        limit: 1000, 
        offset: 0 
      });
      
      // Filtrar preguntas por texto de búsqueda
      const filteredQuestions = allQuestions.questions.filter(question => 
        question.text.toLowerCase().includes(searchText.toLowerCase()) ||
        question.answer?.text.toLowerCase().includes(searchText.toLowerCase())
      );
      
      const { limit = 10, offset = 0 } = options;
      const results = filteredQuestions.slice(offset, offset + limit);
      
      return {
        success: true,
        data: {
          total: filteredQuestions.length,
          limit,
          offset,
          questions: results
        }
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'searchQuestions', error);
      throw error;
    }
  }

  async deleteQuestion(questionId) {
    mockLogger.log(this.serviceName, 'deleteQuestion', { questionId });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 500);
      simulateRandomError(0.02);
      
      // En un mock, simplemente retornamos éxito
      // En una implementación real, se eliminaría de la base de datos
      
      return {
        success: true,
        data: {
          id: questionId,
          deleted: true,
          deleted_at: new Date().toISOString()
        }
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'deleteQuestion', error);
      throw error;
    }
  }

  async getQuestionStatistics(itemId) {
    mockLogger.log(this.serviceName, 'getQuestionStatistics', { itemId });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 400);
      simulateRandomError(0.02);
      
      const allQuestions = getQuestionsByItemId(itemId, { 
        limit: 1000, 
        offset: 0 
      });
      
      const answered = allQuestions.questions.filter(q => q.status === 'ANSWERED').length;
      const unanswered = allQuestions.questions.filter(q => q.status === 'UNANSWERED').length;
      
      const statistics = {
        total_questions: allQuestions.total,
        answered_questions: answered,
        unanswered_questions: unanswered,
        answer_rate: allQuestions.total > 0 ? (answered / allQuestions.total) * 100 : 0,
        avg_response_time_hours: 24, // Mock de tiempo promedio de respuesta
        last_question_date: allQuestions.questions[0]?.date_created || null
      };
      
      return {
        success: true,
        data: statistics
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'getQuestionStatistics', error);
      throw error;
    }
  }
}
