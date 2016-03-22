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
package com.dynamia.cms.site.users.actions;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import com.dynamia.cms.site.core.actions.ActionEvent;
import com.dynamia.cms.site.core.actions.SiteAction;
import com.dynamia.cms.site.core.api.CMSAction;
import com.dynamia.cms.site.users.UserHolder;
import com.dynamia.cms.site.users.domain.UserContactInfo;
import com.dynamia.cms.site.users.services.UserService;

/**
 *
 * @author Mario Serrano Leones
 */
@CMSAction
public class ShowUserContactInfosAction implements SiteAction {

	@Autowired
	private UserService userService;

	@Override
	public String getName() {
		return "showUserContactInfos";
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		ModelAndView mv = evt.getModelAndView();

		List<UserContactInfo> userContactInfos = userService.getContactInfos(UserHolder.get().getCurrent());
		mv.addObject("userContactInfos", userContactInfos);
		
		mv.addObject("title", "Direcciones de Contacto");
		if (userContactInfos == null || userContactInfos.isEmpty()) {
			mv.addObject("subtitle", "No tiene direcciones registradas");
		} else {
			mv.addObject("subtitle", userContactInfos.size() + " direcciones de contactos registradas");

		}
		

	}

}
