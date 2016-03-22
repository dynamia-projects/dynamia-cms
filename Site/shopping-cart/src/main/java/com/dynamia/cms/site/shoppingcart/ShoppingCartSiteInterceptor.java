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
package com.dynamia.cms.site.shoppingcart;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.dynamia.cms.site.core.api.CMSExtension;
import com.dynamia.cms.site.core.api.SiteRequestInterceptor;
import com.dynamia.cms.site.core.domain.Site;

@CMSExtension
public class ShoppingCartSiteInterceptor implements SiteRequestInterceptor {

	@Override
	public void beforeRequest(Site site, HttpServletRequest request, HttpServletResponse response) {

	}

	@Override
	public void afterRequest(Site site, HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {

		ShoppingCartUtils.loadConfig(site, modelAndView);

	}

}
