/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.templates.services.impl;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.templates.Config;
import com.dynamia.cms.site.templates.Template;
import com.dynamia.cms.site.templates.TemplateNotFoundException;
import com.dynamia.cms.site.templates.services.TemplateService;
import com.dynamia.tools.domain.query.Parameters;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
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
        String dir = appParams.getValue(Config.TEMPLATES_LOCATION, "");
        File templateDir = new File(dir);
        if (templateDir.exists() && templateDir.isDirectory()) {
            templateDir.listFiles(new FileFilter() {

                @Override
                public boolean accept(File subdir) {
                    if (subdir.isDirectory()) {
                        File templateProperties = new File(subdir, "template.properties");
                        if (templateProperties.exists()) {
                            Properties props = new Properties();
                            try {
                                props.load(new FileReader(templateProperties));
                                Template template = Template.build(props, subdir);
                                templates.add(template);
                            } catch (Exception ex) {
                                Logger.getLogger(TemplateServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            return true;
                        }
                    }
                    return false;
                }
            });
        }
        return templates;
    }

    @Override
    public List<Template> getInstalledTemplates() {
        return loadTemplates();
    }

    @Override
    public Template getTemplate(Site site) {
        for (Template template : loadTemplates()) {
            if (template.getDirectoryName().equals(site.getTemplate())) {
                return template;
            }
        }
        throw new TemplateNotFoundException("Template not found for site " + site.getName() + ". Template Name: " + site.getName());
    }

}
