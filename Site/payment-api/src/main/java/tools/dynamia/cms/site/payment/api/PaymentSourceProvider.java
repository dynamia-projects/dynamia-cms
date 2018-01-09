package tools.dynamia.cms.site.payment.api;

public interface PaymentSourceProvider {

    PaymentSource findSource(Object request);


}
