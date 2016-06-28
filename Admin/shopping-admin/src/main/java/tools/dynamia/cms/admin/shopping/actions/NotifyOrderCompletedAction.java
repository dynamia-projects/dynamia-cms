package tools.dynamia.cms.admin.shopping.actions;

import org.springframework.beans.factory.annotation.Autowired;

import tools.dynamia.cms.site.shoppingcart.domain.ShoppingOrder;
import tools.dynamia.cms.site.shoppingcart.services.ShoppingCartService;

import tools.dynamia.actions.InstallAction;
import tools.dynamia.commons.ApplicableClass;
import tools.dynamia.crud.AbstractCrudAction;
import tools.dynamia.crud.CrudActionEvent;
import tools.dynamia.crud.CrudState;
import tools.dynamia.domain.services.CrudService;
import tools.dynamia.ui.UIMessages;

@InstallAction
public class NotifyOrderCompletedAction extends AbstractCrudAction {

    @Autowired
    private ShoppingCartService service;

    @Autowired
    private CrudService crudService;

    public NotifyOrderCompletedAction() {
        setName("Notify Order");
        setMenuSupported(true);

    }

    @Override
    public CrudState[] getApplicableStates() {
        return CrudState.get(CrudState.READ);
    }

    @Override
    public ApplicableClass[] getApplicableClasses() {
        return ApplicableClass.get(ShoppingOrder.class);
    }

    @Override
    public void actionPerformed(CrudActionEvent evt) {
        final ShoppingOrder shoppingOrder = (ShoppingOrder) evt.getData();
        if (shoppingOrder != null) {

            UIMessages.showQuestion("Are you sure?", () -> {
                service.notifyOrderCompleted(crudService.reload(shoppingOrder));
                UIMessages.showMessage("Notification sended ");

            });
        }
    }

}
