/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.templates.services.impl;

import com.dynamia.cms.site.core.DynamiaCMS;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.templates.Template;
import com.dynamia.cms.site.templates.TemplateNotFoundException;
import com.dynamia.cms.site.templates.services.TemplateService;
import com.dynamia.tools.domain.query.Parameter;
import com.dynamia.tools.domain.query.Parameters;
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

/**
 *
 * @author mario
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
            defaultTemplate = new Parameter(paramName, "dynamical", "Default Template Name");
            appParams.save(defaultTemplate);
        }

        Template t = getTemplate(defaultTemplate.getValue());
        if (t == null) {
            throw new TemplateNotFoundException("Default Template not found. Name: " + defaultTemplate.getName());
        }
        return t;
    }

}
