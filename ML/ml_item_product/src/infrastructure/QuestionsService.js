// Servicio para preguntas y respuestas
import { getConfig } from '../config/index.js';

class QuestionsService {
  constructor(apiClient) {
    this.apiClient = apiClient;
    this.config = getConfig();
  }

  async getProductQuestions(productId, limit = 10, offset = 0) {
    try {
      const url = this.config.buildUrl('questionsSearch', { id: productId });
      const response = await this.apiClient.get(`${url}&limit=${limit}&offset=${offset}&api_version=4`);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to fetch product questions: ${error.message}`);
    }
  }

  async getQuestionById(questionId) {
    try {
      const url = this.config.buildUrl('questionById', { id: questionId });
      const response = await this.apiClient.get(url);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to fetch question ${questionId}: ${error.message}`);
    }
  }

  async askQuestion(productId, questionText, userId) {
    try {
      const questionData = {
        item_id: productId,
        text: questionText,
        from: {
          id: userId
        }
      };
      
      const url = this.config.buildUrl('questions');
      const response = await this.apiClient.post(url, questionData);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to ask question: ${error.message}`);
    }
  }

  async answerQuestion(questionId, answerText, sellerId) {
    try {
      const answerData = {
        question_id: questionId,
        text: answerText,
        from: {
          id: sellerId
        }
      };
      
      const url = this.config.buildUrl('answers');
      const response = await this.apiClient.post(url, answerData);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to answer question: ${error.message}`);
    }
  }

  async getQuestionStats(productId) {
    try {
      const defaultLimit = this.config.getDefault('limit') || 1000;
      const questions = await this.getProductQuestions(productId, defaultLimit, 0);
      const totalQuestions = questions.total;
      const answeredQuestions = questions.questions?.filter(q => q.answer).length || 0;
      const pendingQuestions = totalQuestions - answeredQuestions;
      
      return {
        total_questions: totalQuestions,
        answered_questions: answeredQuestions,
        pending_questions: pendingQuestions,
        response_rate: totalQuestions > 0 ? (answeredQuestions / totalQuestions) * 100 : 0,
        average_response_time: await this.calculateAverageResponseTime(questions.questions || [])
      };
    } catch (error) {
      throw new Error(`Failed to fetch question stats: ${error.message}`);
    }
  }

  async calculateAverageResponseTime(questions) {
    try {
      const answeredQuestions = questions.filter(q => q.answer && q.answer.date_created);
      
      if (answeredQuestions.length === 0) return null;
      
      const responseTimes = answeredQuestions.map(q => {
        const questionDate = new Date(q.date_created);
        const answerDate = new Date(q.answer.date_created);
        return answerDate - questionDate;
      });
      
      const averageMs = responseTimes.reduce((sum, time) => sum + time, 0) / responseTimes.length;
      
      // Convertir a horas
      return Math.round(averageMs / (1000 * 60 * 60));
    } catch (error) {
      this.config.warn('Could not calculate average response time:', error);
      return null;
    }
  }

  async deleteQuestion(questionId, userId) {
    try {
      const url = this.config.buildUrl('questionById', { id: questionId });
      const response = await this.apiClient.delete(`${url}?user_id=${userId}`);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to delete question: ${error.message}`);
    }
  }

  async getSellerQuestions(sellerId, status = 'all', limit = 50, offset = 0) {
    try {
      const baseUrl = this.config.getBaseUrl();
      let url = `${baseUrl}/questions/search?seller=${sellerId}&limit=${limit}&offset=${offset}`;
      
      if (status !== 'all') {
        url += `&status=${status}`;
      }
      
      const response = await this.apiClient.get(url);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to fetch seller questions: ${error.message}`);
    }
  }
}

export default QuestionsService;
