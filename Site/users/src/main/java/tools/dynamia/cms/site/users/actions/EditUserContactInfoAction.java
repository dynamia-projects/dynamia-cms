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
package tools.dynamia.cms.site.users.actions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import tools.dynamia.cms.site.core.CMSUtil;
import tools.dynamia.cms.site.core.actions.ActionEvent;
import tools.dynamia.cms.site.core.actions.SiteAction;
import tools.dynamia.cms.site.core.api.CMSAction;
import tools.dynamia.cms.site.users.UserHolder;
import tools.dynamia.cms.site.users.domain.UserContactInfo;

import tools.dynamia.domain.services.CrudService;

/**
 *
 * @author Mario Serrano Leones
 */
@CMSAction
public class EditUserContactInfoAction implements SiteAction {

	@Autowired
	private CrudService crudService;

	@Override
	public String getName() {
		return "editUserContactInfo";
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		ModelAndView mv = evt.getModelAndView();
		Long id = (Long) evt.getData();
		UserContactInfo userContactInfo = crudService.find(UserContactInfo.class, id);
		if (userContactInfo.getUser().equals(UserHolder.get().getCurrent())) {
			mv.addObject("title", "Editar Direccion de Contacto");
			mv.addObject("userContactInfo", userContactInfo);

			CMSUtil.buildContactInfoOptions(evt.getSite(), mv, "uci", userContactInfo.getInfo());
		} else {
			CMSUtil.addErrorMessage("Direccion de contacto NO pertenece a este usuario -.-", evt.getRedirectAttributes());

		}
	}

}