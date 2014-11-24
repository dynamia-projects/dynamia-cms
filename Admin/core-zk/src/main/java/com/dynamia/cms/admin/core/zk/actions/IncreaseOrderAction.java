package com.dynamia.cms.admin.core.zk.actions;

import com.dynamia.cms.site.core.Orderable;
import com.dynamia.tools.commons.ApplicableClass;
import com.dynamia.tools.domain.AbstractEntity;
import com.dynamia.tools.domain.services.CrudService;
import com.dynamia.tools.web.actions.InstallAction;
import com.dynamia.tools.web.crud.AbstractCrudAction;
import com.dynamia.tools.web.crud.CrudActionEvent;
import com.dynamia.tools.web.crud.CrudState;
import com.dynamia.tools.web.ui.MessageType;
import com.dynamia.tools.web.ui.UIMessages;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Created by Mario on 18/11/2014.
 */
@InstallAction
public class IncreaseOrderAction extends AbstractCrudAction {


    @Autowired
    private CrudService crudService;

    public IncreaseOrderAction() {
        setName("Move up");
        setImage("up");
        setMenuSupported(true);
    }

    @Override
    public void actionPerformed(CrudActionEvent evt) {
        Orderable orderable = (Orderable) evt.getData();
        if (orderable != null) {
            orderable.setOrder(orderable.getOrder() - 1);
            crudService.update(orderable);
            evt.getController().doQuery();
            evt.getController().setSelected((AbstractEntity) orderable);
        }else{
            UIMessages.showMessage("Select row", MessageType.WARNING);
        }
    }

    @Override
    public CrudState[] getApplicableStates() {
        return CrudState.get(CrudState.READ);
    }

    @Override
    public ApplicableClass[] getApplicableClasses() {
        return ApplicableClass.get(Orderable.class);
    }
}
