/*
 * Copyright (C) 2009 - 2020 Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia - South America
 * All Rights Reserved.
 *
 * DynamiaCMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License (LGPL v3) as
 * published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamiaCMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamiaCMS.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package tools.dynamia.cms.templates.services.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tools.dynamia.cms.core.DynamiaCMS
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.templates.Template
import tools.dynamia.cms.templates.TemplateNotFoundException
import tools.dynamia.cms.templates.services.TemplateService
import tools.dynamia.domain.jpa.JpaParameter
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
			if (template.directoryName == templateName) {
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
			defaultTemplate = new JpaParameter(paramName, installedTemplates.get(0).name, "Default Template Name")
            appParams.save(defaultTemplate)
        }

		Template t = getTemplate(defaultTemplate.value)
        if (t == null) {
			throw new TemplateNotFoundException("Default Template not found. Name: " + defaultTemplate.name)
        }
		return t
    }

}
