package tools.dynamia.cms.admin.shopping.controllers;

import tools.dynamia.cms.site.core.SiteContext;
import tools.dynamia.cms.site.payment.PaymentGateway;
import tools.dynamia.cms.site.payment.domain.PaymentGatewayConfig;
import tools.dynamia.cms.site.payment.services.PaymentService;

import tools.dynamia.commons.StringUtils;
import tools.dynamia.integration.Containers;
import tools.dynamia.zk.crud.CrudController;

public class PaymentGatewayConfigController extends CrudController<PaymentGatewayConfig> {

    @Override
    protected void beforeQuery() {
        PaymentService service = Containers.get().findObject(PaymentService.class);
        PaymentGateway gateway = service.getDefaultGateway();
        String source = SiteContext.get().getCurrent().getKey();
        if (gateway != null) {

            for (String param : gateway.getRequiredParams()) {
                PaymentGatewayConfig config = service.getConfig(gateway, param, source);
                if (config == null) {
                    String label = StringUtils.capitalize(param);
                    label = StringUtils.addSpaceBetweenWords(label);
                    config = new PaymentGatewayConfig(gateway, param, "", source, label);
                    crudService.create(config);
                }
            }
        }

    }

}
