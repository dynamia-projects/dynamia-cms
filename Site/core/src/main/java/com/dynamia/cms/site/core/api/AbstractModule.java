package com.dynamia.cms.site.core.api;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractModule implements Module {

	private String id;
	private String name;
	private String description;
	private String templateViewName;
	private Map<String, Object> metadata = new HashMap<>();

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

}
