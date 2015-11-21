package com.dynamia.cms.admin.core.zk.actions;

import org.springframework.beans.factory.annotation.Autowired;

import com.dynamia.cms.site.core.Orderable;

import tools.dynamia.actions.InstallAction;
import tools.dynamia.commons.ApplicableClass;
import tools.dynamia.crud.AbstractCrudAction;
import tools.dynamia.crud.CrudActionEvent;
import tools.dynamia.crud.CrudState;
import tools.dynamia.domain.AbstractEntity;
import tools.dynamia.domain.services.CrudService;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;

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
			if (evt.getController() != null) {
				evt.getController().doQuery();
				evt.getController().setSelected((AbstractEntity) orderable);
			}

			if (orderable.getOrder() <= 0) {
				UIMessages.showMessage("Done");
			}
		} else {
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
