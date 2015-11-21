package com.dynamia.cms.site.core.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dynamia.cms.site.core.JavaScriptResource;
import com.dynamia.cms.site.core.StyleSheetResource;

public abstract class AbstractModule implements Module {

	private String id;
	private String name;
	private String description;
	private String templateViewName;

	private Map<String, Object> metadata = new HashMap<>();
	private List<JavaScriptResource> javaScriptResources = new ArrayList<>();
	private List<StyleSheetResource> styleSheetResources = new ArrayList<>();

	public AbstractModule(String id, String name, String templateViewName) {
		super();
		this.id = id;
		this.name = name;
		this.templateViewName = templateViewName;
	}

	public void putMetadata(String key, Object value) {
		metadata.put(key, value);
	}

	@Override
	public Map<String, Object> getMetadata() {
		return metadata;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTemplateViewName() {
		return templateViewName;
	}

	public void setTemplateViewName(String templateViewName) {
		this.templateViewName = templateViewName;
	}

	@Override
	public List<JavaScriptResource> getJavaScriptResources() {
		return javaScriptResources;
	}

	@Override
	public List<StyleSheetResource> getStyleSheetResources() {
		return styleSheetResources;
	}

	public void addResource(JavaScriptResource resource) {
		javaScriptResources.add(resource);
	}

	public void addResource(StyleSheetResource resource) {
		styleSheetResources.add(resource);
	}

	@Override
	public String toString() {
		return getName() + "(" + getTemplateViewName() + ")";
	}
}
