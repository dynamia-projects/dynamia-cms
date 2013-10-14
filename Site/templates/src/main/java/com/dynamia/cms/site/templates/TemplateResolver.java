/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.templates;

import org.thymeleaf.resourceresolver.IResourceResolver;

/**
 *
 * @author mario
 */
public class TemplateResolver extends org.thymeleaf.templateresolver.TemplateResolver {

    public TemplateResolver() {
        super();
        super.setResourceResolver(new DynamicFileResourceResolver());
    }

    @Override
    public void setResourceResolver(IResourceResolver resourceResolver) {
        throw new IllegalArgumentException("Dont use this");
    }
}
