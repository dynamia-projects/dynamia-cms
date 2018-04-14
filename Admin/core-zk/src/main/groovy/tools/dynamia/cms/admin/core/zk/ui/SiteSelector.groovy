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
package tools.dynamia.cms.admin.core.zk.ui

import org.zkoss.zul.Combobox
import tools.dynamia.cms.site.core.domain.Site
import tools.dynamia.domain.services.CrudService
import tools.dynamia.integration.Containers
import tools.dynamia.zk.ComponentAliasIndex
import tools.dynamia.zk.util.ZKUtil

/**
 *
 * @author Mario Serrano Leones
 */
public class SiteSelector extends Combobox {

    static {
        ComponentAliasIndex.getInstance().add(SiteSelector.class);
    }

    public SiteSelector() {
        init();
    }

    public void init() {
        getChildren().clear();
        setReadonly(true);
        CrudService crudService = Containers.get().findObject(CrudService.class);
        Collection<Site> sites = crudService.findAll(Site.class, "name");
        ZKUtil.fillCombobox(this, sites, true);

    }

}
