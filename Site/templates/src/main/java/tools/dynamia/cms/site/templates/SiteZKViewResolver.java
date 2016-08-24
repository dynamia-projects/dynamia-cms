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
package tools.dynamia.cms.site.templates;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.AbstractUrlBasedView;

import tools.dynamia.zk.spring.ZKView;
import tools.dynamia.zk.spring.ZKViewResolver;

/**
 *
 * @author Mario A. Serrano Leones
 */
public class SiteZKViewResolver extends ZKViewResolver {

	private static final String PREFIX = "/cms-zk";
	private boolean zulCacheable;

	public SiteZKViewResolver() {
		setPrefix(PREFIX);
		setSuffix("");
	}

	@Override
	protected AbstractUrlBasedView buildView(String viewName) throws Exception {

		SiteTemplateResource siteResource = new SiteTemplateResource(viewName + ".zul", null);
		String realPath = null;
		ZKView view = null;
		if (siteResource.exists()) {
			realPath = siteResource.getTemplatePath().toAbsolutePath().toString();
			view = (ZKView) super.buildView("");

		} else {
			siteResource = new SiteTemplateResource(viewName + ".zhtml", null);

			if (siteResource.exists()) {
				realPath = siteResource.getTemplatePath().toAbsolutePath().toString();
				view = (ZKView) super.buildView("");

			}
		}
		if (view != null) {
			view.setRealPath(realPath);
			view.setCacheable(isZulCacheable());

			return view;
		} else {
			return new NonExistentView();
		}

	}

	private static class NonExistentView extends AbstractUrlBasedView {

		@Override
		protected boolean isUrlRequired() {

			return false;
		}

		@Override
		public boolean checkResource(Locale locale) throws Exception {

			return false;
		}

		@Override
		protected void renderMergedOutputModel(Map<String, Object> model,
				HttpServletRequest request,
				HttpServletResponse response) throws Exception {

			// Purposely empty, it should never get called
		}
	}

	public boolean isZulCacheable() {
		return zulCacheable;
	}

	public void setZulCacheable(boolean zulCacheable) {
		this.zulCacheable = zulCacheable;
	}

}
