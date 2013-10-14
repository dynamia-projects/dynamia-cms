/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.templates;

import com.dynamia.tools.domain.query.ApplicationParameters;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

/**
 *
 * @author mario
 */
public class TemplateResourceHandler extends ResourceHttpRequestHandler {

    @Override
    protected Resource getResource(HttpServletRequest request) {
        String dir = ApplicationParameters.get().getValue(Config.TEMPLATES_LOCATION, "");
        String currentTemplate = ApplicationParameters.get().getValue(Config.CURRENT_TEMPLATE, "dynamical");
        String name = request.getPathInfo();
        String fileName = dir + currentTemplate + name;

        return new FileSystemResource(fileName);
    }
}
