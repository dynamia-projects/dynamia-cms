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
package tools.dynamia.cms.site.templates.services.impl;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tools.dynamia.cms.site.core.DynamiaCMS;
import tools.dynamia.cms.site.core.domain.Site;
import tools.dynamia.cms.site.templates.Template;
import tools.dynamia.cms.site.templates.TemplateNotFoundException;
import tools.dynamia.cms.site.templates.services.TemplateService;

import tools.dynamia.domain.query.Parameter;
import tools.dynamia.domain.query.Parameters;

/**
 *
 * @author Mario Serrano Leones
 */
@Service
public class TemplateServiceImpl implements TemplateService {

    @Autowired
    private Parameters appParams;

    private List<Template> loadTemplates() {
        final List<Template> templates = new ArrayList<>();
        Path templateDir = DynamiaCMS.getTemplatesLocation();
        try {
            DirectoryStream<Path> stream = Files.newDirectoryStream(templateDir);
            for (Path subdir : stream) {
                if (Files.isDirectory(subdir)) {
                    Path templateProperties = subdir.resolve("template.properties");
                    if (Files.exists(subdir)) {
                        Properties props = new Properties();
                        try {
                            props.load(Files.newBufferedReader(templateProperties, Charset.defaultCharset()));
                            Template template = Template.build(props, subdir);
                            templates.add(template);
                        } catch (Exception ex) {
                            Logger.getLogger(TemplateServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(TemplateServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return templates;
    }

    @Override
    public List<Template> getInstalledTemplates() {
        return loadTemplates();
    }

    @Override
    public Template getTemplate(String templateName) {
        for (Template template : loadTemplates()) {
            if (template.getDirectoryName().equals(templateName)) {
                return template;
            }
        }
        return null;
    }

    @Override
    public Template getTemplate(Site site) {
        return getTemplate(site.getTemplate());
    }

    @Override
    public Template getDefaultTemplate() {
        String paramName = "CMSConfig_DefaultTemplate";
        Parameter defaultTemplate = appParams.getParameter(paramName);
        if (defaultTemplate == null) {
            List<Template> installedTemplates = getInstalledTemplates();
            if (installedTemplates.isEmpty()) {
                throw new TemplateNotFoundException("Cannot get default template: No templates installed");
            }
            defaultTemplate = new Parameter(paramName, installedTemplates.get(0).getName(), "Default Template Name");
            appParams.save(defaultTemplate);
        }

        Template t = getTemplate(defaultTemplate.getValue());
        if (t == null) {
            throw new TemplateNotFoundException("Default Template not found. Name: " + defaultTemplate.getName());
        }
        return t;
    }

}
