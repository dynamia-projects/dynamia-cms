/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.templates.services;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.templates.Template;
import java.util.List;

/**
 *
 * @author mario
 */
public interface TemplateService {

    List<Template> getInstalledTemplates();

    Template getTemplate(Site site);

    Template getDefaultTemplate();

    Template getTemplate(String templateName);

}
