// Caso de uso para obtener preguntas y respuestas
class GetProductQuestionsUseCase {
  constructor(questionsAdapter) {
    this.questionsAdapter = questionsAdapter;
  }

  async execute(productId, filters = {}) {
    try {
      const limit = filters.limit || 10;
      const offset = filters.offset || 0;
      
      const questions = await this.questionsAdapter.getProductQuestions(productId, limit, offset);
      const stats = await this.questionsAdapter.getQuestionStats(productId);
      
      return {
        ...questions,
        stats,
        hasQuestions: questions.total > 0,
        categorized: this.categorizeQuestions(questions.questions || []),
        summary: this.generateQuestionsSummary(questions.questions || [], stats)
      };
    } catch (error) {
      throw new Error(`Failed to get product questions: ${error.message}`);
    }
  }

  categorizeQuestions(questions) {
    const categories = {
      answered: [],
      pending: [],
      recent: []
    };
    
    const now = new Date();
    const oneDayAgo = new Date(now.getTime() - 24 * 60 * 60 * 1000);
    
    questions.forEach(question => {
      if (question.answer) {
        categories.answered.push(question);
      } else {
        categories.pending.push(question);
      }
      
      const questionDate = new Date(question.dateCreated);
      if (questionDate > oneDayAgo) {
        categories.recent.push(question);
      }
    });
    
    return categories;
  }

  generateQuestionsSummary(questions, stats) {
    return {
      totalQuestions: stats.totalQuestions,
      answeredQuestions: stats.answeredQuestions,
      pendingQuestions: stats.pendingQuestions,
      responseRate: stats.responseRate,
      averageResponseTime: stats.averageResponseTime,
      mostCommonTopics: this.extractCommonTopics(questions),
      responsiveness: this.calculateResponsiveness(stats)
    };
  }

  extractCommonTopics(questions) {
    const topics = {};
    const commonKeywords = [
      'envío', 'entrega', 'shipping', 'delivery',
      'precio', 'price', 'costo', 'cost',
      'garantía', 'warranty', 'guarantee',
      'color', 'tamaño', 'size', 'medidas',
      'stock', 'disponible', 'available',
      'pago', 'payment', 'cuotas', 'installments'
    ];
    
    questions.forEach(question => {
      const text = question.text.toLowerCase();
      commonKeywords.forEach(keyword => {
        if (text.includes(keyword)) {
          topics[keyword] = (topics[keyword] || 0) + 1;
        }
      });
    });
    
    return Object.entries(topics)
      .sort(([,a], [,b]) => b - a)
      .slice(0, 5)
      .map(([topic, count]) => ({ topic, count }));
  }

  calculateResponsiveness(stats) {
    if (stats.totalQuestions === 0) return 'no_data';
    
    const responseRate = stats.responseRate;
    const avgResponseTime = stats.averageResponseTime;
    
    if (responseRate >= 95 && avgResponseTime && avgResponseTime <= 2) return 'excellent';
    if (responseRate >= 85 && avgResponseTime && avgResponseTime <= 6) return 'very_good';
    if (responseRate >= 70 && avgResponseTime && avgResponseTime <= 24) return 'good';
    if (responseRate >= 50) return 'average';
    return 'poor';
  }

  async askQuestion(productId, questionText, userId) {
    try {
      const question = await this.questionsAdapter.askQuestion(productId, questionText, userId);
      
      return {
        success: true,
        question,
        message: 'Pregunta enviada correctamente'
      };
    } catch (error) {
      throw new Error(`Failed to ask question: ${error.message}`);
    }
  }
}

export default GetProductQuestionsUseCase;
