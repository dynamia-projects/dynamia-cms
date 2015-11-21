/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core.api;

import java.util.List;
import java.util.Map;

import com.dynamia.cms.site.core.JavaScriptResource;
import com.dynamia.cms.site.core.StyleSheetResource;

/**
 *
 * @author mario
 */
public interface Module extends TypeExtension {

	String getTemplateViewName();

	Map<String, Object> getMetadata();
	
	public List<StyleSheetResource> getStyleSheetResources();
	
	public List<JavaScriptResource> getJavaScriptResources();

	void init(ModuleContext context);

}
