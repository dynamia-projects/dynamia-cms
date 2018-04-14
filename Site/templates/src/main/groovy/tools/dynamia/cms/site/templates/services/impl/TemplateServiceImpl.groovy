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
package tools.dynamia.cms.site.templates.services.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tools.dynamia.cms.site.core.DynamiaCMS
import tools.dynamia.cms.site.core.domain.Site
import tools.dynamia.cms.site.templates.Template
import tools.dynamia.cms.site.templates.TemplateNotFoundException
import tools.dynamia.cms.site.templates.services.TemplateService
import tools.dynamia.domain.query.Parameter
import tools.dynamia.domain.query.Parameters

import java.nio.charset.Charset
import java.nio.file.DirectoryStream
import java.nio.file.Files
import java.nio.file.Path
import java.util.logging.Level
import java.util.logging.Logger

/**
 *
 * @author Mario Serrano Leones
 */
@Service
class TemplateServiceImpl implements TemplateService {

	@Autowired
	private Parameters appParams

    private List<Template> loadTemplates() {
		final List<Template> templates = new ArrayList<>()
        Path templateDir = DynamiaCMS.templatesLocation
        try {
			DirectoryStream<Path> stream = Files.newDirectoryStream(templateDir)
            for (Path subdir : stream) {
				if (Files.isDirectory(subdir)) {
					Template template = loadTemplate(subdir)
                    if (template != null) {
						templates.add(template)
                    }
				}
			}
		} catch (IOException ex) {
			Logger.getLogger(TemplateServiceImpl.class.name).log(Level.SEVERE, null, ex)
        }

		return templates
    }

	private Template loadTemplate(Path templateDir) {
		Path templateProperties = templateDir.resolve("template.properties")
        if (Files.exists(templateDir)) {
			Properties props = new Properties()
            try {
				props.load(Files.newBufferedReader(templateProperties, Charset.defaultCharset()))
                Template template = Template.build(props, templateDir)
                return template
            } catch (Exception ex) {
				Logger.getLogger(TemplateServiceImpl.class.name).log(Level.SEVERE, null, ex)
            }
		}
		return null
    }

	@Override
    List<Template> getInstalledTemplates() {
		return loadTemplates()
    }

	@Override
    Template getTemplate(String templateName) {
		for (Template template : loadTemplates()) {
			if (template.directoryName.equals(templateName)) {
				return template
            }
		}
		return null
    }

	@Override
    Template getTemplate(Site site) {
		String templateName = site.template
        Path localTemplateDir = DynamiaCMS.getSitesResourceLocation(site)
				.resolve(DynamiaCMS.TEMPLATES + File.separator + templateName)
        Template template = loadTemplate(localTemplateDir)
        if (template != null) {
			return template
        } else {
			return getTemplate(templateName)
        }
	}

	@Override
    Template getDefaultTemplate() {
		String paramName = "CMSConfig_DefaultTemplate"
        Parameter defaultTemplate = appParams.getParameter(paramName)
        if (defaultTemplate == null) {
			List<Template> installedTemplates = installedTemplates
            if (installedTemplates.empty) {
				throw new TemplateNotFoundException("Cannot get default template: No templates installed")
            }
			defaultTemplate = new Parameter(paramName, installedTemplates.get(0).name, "Default Template Name")
            appParams.save(defaultTemplate)
        }

		Template t = getTemplate(defaultTemplate.value)
        if (t == null) {
			throw new TemplateNotFoundException("Default Template not found. Name: " + defaultTemplate.name)
        }
		return t
    }

}
