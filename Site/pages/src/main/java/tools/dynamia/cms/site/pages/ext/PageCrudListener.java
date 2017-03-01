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
package tools.dynamia.cms.site.pages.ext;

import java.util.Date;

import tools.dynamia.cms.site.core.api.CMSListener;
import tools.dynamia.cms.site.pages.domain.Page;
import tools.dynamia.cms.site.pages.services.PageService;
import java.io.UnsupportedEncodingException;
import org.springframework.beans.factory.annotation.Autowired;

import tools.dynamia.domain.util.CrudServiceListenerAdapter;

/**
 *
 * @author Mario Serrano Leones
 */
@CMSListener
public class PageCrudListener extends CrudServiceListenerAdapter<Page> {

	@Autowired
	private PageService service;

	@Override
	public void beforeCreate(Page page) {
		if (page.getId() == null) {
			page.setCreationDate(new Date());
		}

		if (page.getSummary() == null || page.getSummary().isEmpty()) {
			service.generateSummary(page);
		}

		if (page.getImageURL() == null || page.getImageURL().isEmpty()) {
			service.generateImageURL(page);
		}

	}

	@Override
	public void beforeUpdate(Page page) {
		beforeCreate(page);
		page.setLastUpdate(new Date());

	}

}
