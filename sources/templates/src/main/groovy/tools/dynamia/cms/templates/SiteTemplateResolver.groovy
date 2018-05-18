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
package tools.dynamia.cms.templates

import org.thymeleaf.IEngineConfiguration
import org.thymeleaf.templateresolver.AbstractConfigurableTemplateResolver
import org.thymeleaf.templateresource.ITemplateResource

/**
 *
 * @author Mario Serrano Leones
 */
class SiteTemplateResolver extends AbstractConfigurableTemplateResolver {

	SiteTemplateResolver() {
		System.out.println("Initializing boot " + getClass().simpleName)

    }

	@Override
	protected ITemplateResource computeTemplateResource(IEngineConfiguration configuration, String ownerTemplate,
			String template, String resourceName, String characterEncoding,
			Map<String, Object> templateResolutionAttributes) {

		if (resourceName == null || resourceName.startsWith("<")) {
			return null
        }

		return new SiteTemplateResource(resourceName, characterEncoding)
    }

}
