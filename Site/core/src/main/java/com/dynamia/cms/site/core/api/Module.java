/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core.api;

import java.util.Map;

/**
 *
 * @author mario
 */
public interface Module extends TypeExtension {

	String getTemplateViewName();

	Map<String, Object> getMetadata();

	void init(ModuleContext context);

}
