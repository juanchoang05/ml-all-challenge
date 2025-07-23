// Adaptador para métodos de pago
class PaymentAdapter {
  constructor(paymentService) {
    this.paymentService = paymentService;
  }

  async getPaymentMethods(sellerId) {
    try {
      const rawPaymentMethods = await this.paymentService.getSellerPaymentMethods(sellerId);
      return this.transformPaymentMethods(rawPaymentMethods);
    } catch (error) {
      throw new Error(`Error fetching payment methods: ${error.message}`);
    }
  }

  transformPaymentMethods(rawPaymentMethods) {
    return {
      creditCards: this.transformCreditCards(rawPaymentMethods.credit_cards || []),
      debitCards: this.transformDebitCards(rawPaymentMethods.debit_cards || []),
      digitalWallets: this.transformDigitalWallets(rawPaymentMethods.digital_wallets || []),
      bankTransfers: this.transformBankTransfers(rawPaymentMethods.bank_transfers || []),
      cash: this.transformCashPayments(rawPaymentMethods.cash || []),
      installments: this.transformInstallments(rawPaymentMethods.installments || [])
    };
  }

  transformCreditCards(creditCards) {
    return creditCards.map(card => ({
      id: card.id,
      name: card.name,
      logo: card.secure_thumbnail || card.thumbnail,
      maxInstallments: card.max_installments,
      minInstallments: card.min_installments,
      processing_modes: card.processing_modes
    }));
  }

  transformDebitCards(debitCards) {
    return debitCards.map(card => ({
      id: card.id,
      name: card.name,
      logo: card.secure_thumbnail || card.thumbnail
    }));
  }

  transformDigitalWallets(digitalWallets) {
    return digitalWallets.map(wallet => ({
      id: wallet.id,
      name: wallet.name,
      logo: wallet.secure_thumbnail || wallet.thumbnail
    }));
  }

  transformBankTransfers(bankTransfers) {
    return bankTransfers.map(transfer => ({
      id: transfer.id,
      name: transfer.name,
      processingTime: transfer.processing_time
    }));
  }

  transformCashPayments(cashPayments) {
    return cashPayments.map(cash => ({
      id: cash.id,
      name: cash.name,
      logo: cash.secure_thumbnail || cash.thumbnail
    }));
  }

  transformInstallments(installments) {
    return installments.map(installment => ({
      paymentMethodId: installment.payment_method_id,
      installments: installment.payer_costs?.map(cost => ({
        installments: cost.installments,
        installmentRate: cost.installment_rate,
        discountRate: cost.discount_rate,
        minAllowedAmount: cost.min_allowed_amount,
        maxAllowedAmount: cost.max_allowed_amount,
        labels: cost.labels,
        installmentAmount: cost.installment_amount,
        totalAmount: cost.total_amount
      })) || []
    }));
  }

  async getShippingMethods(sellerId, zipCode = null) {
    try {
      const rawShippingMethods = await this.paymentService.getShippingMethods(sellerId, zipCode);
      return this.transformShippingMethods(rawShippingMethods);
    } catch (error) {
      throw new Error(`Error fetching shipping methods: ${error.message}`);
    }
  }

  transformShippingMethods(rawShippingMethods) {
    return rawShippingMethods.map(method => ({
      id: method.id,
      name: method.name,
      cost: method.cost,
      estimatedDelivery: method.estimated_delivery,
      freeShipping: method.free_shipping
    }));
  }
}

export default PaymentAdapter;
