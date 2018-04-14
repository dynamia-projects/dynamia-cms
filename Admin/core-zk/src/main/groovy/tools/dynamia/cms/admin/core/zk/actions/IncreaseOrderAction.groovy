/* 
 * Copyright 2016 Dynamia Soluciones IT SAS and the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tools.dynamia.cms.admin.core.zk.actions

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.site.core.Orderable
import tools.dynamia.commons.ApplicableClass
import tools.dynamia.crud.AbstractCrudAction
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.crud.CrudState
import tools.dynamia.domain.AbstractEntity
import tools.dynamia.domain.services.CrudService
import tools.dynamia.ui.MessageType
import tools.dynamia.ui.UIMessages

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
