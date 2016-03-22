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
package com.dynamia.cms.admin.products.actions;

import org.springframework.beans.factory.annotation.Autowired;

import com.dynamia.cms.site.products.domain.ProductsSiteConfig;
import com.dynamia.cms.site.products.services.ProductsService;

import tools.dynamia.actions.ActionGroup;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.commons.ApplicableClass;
import tools.dynamia.crud.AbstractCrudAction;
import tools.dynamia.crud.CrudActionEvent;
import tools.dynamia.crud.CrudState;
import tools.dynamia.domain.services.CrudService;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;

/**
 *
 * @author Mario Serrano Leones
 */
@InstallAction
public class GenerateTokenAction extends AbstractCrudAction {

    @Autowired
    private ProductsService service;

    @Autowired
    private CrudService crudService;

    public GenerateTokenAction() {
        setName("Generate Token");
        setImage("token");
        setGroup(ActionGroup.get("products"));
        setMenuSupported(true);
    }

    @Override
    public void actionPerformed(final CrudActionEvent evt) {
        final ProductsSiteConfig cfg = (ProductsSiteConfig) evt.getData();
        if (cfg != null) {
            UIMessages.showQuestion("¿Are you sure want generate a new toke for this site's configuration?", () -> {
			    service.generateToken(cfg);
			    crudService.save(cfg);
			    evt.getController().doQuery();
			    UIMessages.showMessage("Token generated succesfully!!");
			});

        } else {
            UIMessages.showMessage("Select product site configuration to generate new token.", MessageType.WARNING);
        }

    }

    @Override
    public CrudState[] getApplicableStates() {
        return CrudState.get(CrudState.READ);
    }

    @Override
    public ApplicableClass[] getApplicableClasses() {
        return ApplicableClass.get(ProductsSiteConfig.class);
    }

}
