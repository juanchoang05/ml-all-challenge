// Adaptador para preguntas y respuestas del producto
class QuestionsAdapter {
  constructor(questionsService) {
    this.questionsService = questionsService;
  }

  async getProductQuestions(productId, limit = 10, offset = 0) {
    try {
      const rawQuestions = await this.questionsService.getProductQuestions(productId, limit, offset);
      return this.transformQuestions(rawQuestions);
    } catch (error) {
      throw new Error(`Error fetching product questions: ${error.message}`);
    }
  }

  transformQuestions(rawQuestions) {
    return {
      total: rawQuestions.total,
      limit: rawQuestions.limit,
      offset: rawQuestions.offset,
      questions: rawQuestions.questions?.map(question => this.transformQuestion(question)) || []
    };
  }

  transformQuestion(question) {
    return {
      id: question.id,
      text: question.text,
      status: question.status,
      dateCreated: question.date_created,
      itemId: question.item_id,
      sellerId: question.seller_id,
      answer: question.answer ? this.transformAnswer(question.answer) : null,
      from: {
        id: question.from?.id,
        answeredQuestions: question.from?.answered_questions
      },
      deletedFromListing: question.deleted_from_listing,
      hold: question.hold
    };
  }

  transformAnswer(answer) {
    return {
      text: answer.text,
      status: answer.status,
      dateCreated: answer.date_created
    };
  }

  async askQuestion(productId, questionText, userId) {
    try {
      const response = await this.questionsService.askQuestion(productId, questionText, userId);
      return this.transformQuestion(response);
    } catch (error) {
      throw new Error(`Error asking question: ${error.message}`);
    }
  }

  async answerQuestion(questionId, answerText, sellerId) {
    try {
      const response = await this.questionsService.answerQuestion(questionId, answerText, sellerId);
      return this.transformAnswer(response);
    } catch (error) {
      throw new Error(`Error answering question: ${error.message}`);
    }
  }

  async getQuestionStats(productId) {
    try {
      const stats = await this.questionsService.getQuestionStats(productId);
      return {
        totalQuestions: stats.total_questions,
        answeredQuestions: stats.answered_questions,
        pendingQuestions: stats.pending_questions,
        responseRate: stats.response_rate,
        averageResponseTime: stats.average_response_time
      };
    } catch (error) {
      throw new Error(`Error fetching question stats: ${error.message}`);
    }
  }
}

export default QuestionsAdapter;
