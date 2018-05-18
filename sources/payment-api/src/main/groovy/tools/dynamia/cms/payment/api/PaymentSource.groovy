package tools.dynamia.cms.payment.api

class PaymentSource implements Serializable {
    private String name
    private String baseURL
    private String description
    private String currency

    PaymentSource(String name, String baseURL, String description, String currency) {
        this.name = name
        this.baseURL = baseURL
        this.description = description
        this.currency = currency
    }

    String getCurrency() {
        return currency
    }

    String getName() {
        return name
    }

    String getBaseURL() {
        return baseURL
    }

    String getDescription() {
        return description
    }
}
