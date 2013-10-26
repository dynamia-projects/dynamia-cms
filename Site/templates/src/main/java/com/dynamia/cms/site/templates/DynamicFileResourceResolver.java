/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.templates;

import com.dynamia.tools.domain.query.ApplicationParameters;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import org.thymeleaf.TemplateProcessingParameters;
import org.thymeleaf.resourceresolver.IResourceResolver;

/**
 *
 * @author mario
 */
public class DynamicFileResourceResolver implements IResourceResolver {

    @Override
    public String getName() {
        return DynamicFileResourceResolver.class.getName();
    }
    

    @Override
    public InputStream getResourceAsStream(TemplateProcessingParameters tpp, String name) {
        String dir = ApplicationParameters.get().getValue(Config.TEMPLATES_LOCATION, "");
        String currentTemplate = ApplicationParameters.get().getValue(Config.CURRENT_TEMPLATE, "dynamical");
        String fileName = dir + currentTemplate + "/" + name;
        File file = new File(fileName);
        if (!file.exists()) {
            fileName = dir + currentTemplate + "/views/" + name;
            file = new File(fileName);
        }

        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            return null;
        }
    }
}
