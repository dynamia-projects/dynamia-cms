/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core.api;

import java.io.Serializable;

/**
 *
 * @author mario
 */
public class AdminModuleOption implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8763631027547751496L;
	private String id;
	private String name;
	private Class coreClass;
	private boolean editorAllowed = true;
	private boolean adminAllowed = true;
	private String image;
	private boolean shortcut;

	public AdminModuleOption() {

	}

	public AdminModuleOption(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public AdminModuleOption(String id, String name, Class coreClass) {
		this.id = id;
		this.name = name;
		this.coreClass = coreClass;
	}

	public AdminModuleOption(String id, String name, Class coreClass, boolean editorAllowed, boolean adminAllowed) {
		super();
		this.id = id;
		this.name = name;
		this.coreClass = coreClass;
		this.editorAllowed = editorAllowed;
		this.adminAllowed = adminAllowed;
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

	public Class getCoreClass() {
		return coreClass;
	}

	public void setCoreClass(Class coreClass) {
		this.coreClass = coreClass;
	}

	public boolean isEditorAllowed() {
		return editorAllowed;
	}

	public void setEditorAllowed(boolean editorAllowed) {
		this.editorAllowed = editorAllowed;
	}

	public boolean isAdminAllowed() {
		return adminAllowed;
	}

	public void setAdminAllowed(boolean adminAllowed) {
		this.adminAllowed = adminAllowed;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public boolean isShortcut() {
		return shortcut;
	}

	public void setShortcut(boolean shortcut) {
		this.shortcut = shortcut;
	}

}
