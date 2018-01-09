package tools.dynamia.cms.site.payment.api;

import java.io.Serializable;

public class PaymentSource implements Serializable {
    private String name;
    private String baseURL;
    private String description;
    private String currency;

    public PaymentSource(String name, String baseURL, String description, String currency) {
        this.name = name;
        this.baseURL = baseURL;
        this.description = description;
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

    public String getName() {
        return name;
    }

    public String getBaseURL() {
        return baseURL;
    }

    public String getDescription() {
        return description;
    }
}
