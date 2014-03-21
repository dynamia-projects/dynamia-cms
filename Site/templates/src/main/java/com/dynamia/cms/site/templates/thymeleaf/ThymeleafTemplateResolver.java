/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.templates.thymeleaf;

import org.thymeleaf.resourceresolver.IResourceResolver;

/**
 *
 * @author mario
 */
public class ThymeleafTemplateResolver extends org.thymeleaf.templateresolver.TemplateResolver {

    public ThymeleafTemplateResolver() {
        System.out.println("Initializing DynamiaCMS ThymeleafTemplateResolver");
        super.setResourceResolver(new ThymeleafTemplateResourceResolver());
        
    }

    @Override
    public void setResourceResolver(IResourceResolver resourceResolver) {
        throw new IllegalArgumentException("Dont use this");
    }
    
    

}
