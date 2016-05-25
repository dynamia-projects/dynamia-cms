/* 
 * Copyright 2016 Dynamia Soluciones IT SAS and the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tools.dynamia.cms.site.core.api;

import java.io.Serializable;

/**
 *
 * @author Mario Serrano Leones
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
	private String path;

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

	public AdminModuleOption(String id, String name, Class coreClass, boolean editorAllowed, boolean adminAllowed, String image,
			boolean shortcut) {
		super();
		this.id = id;
		this.name = name;
		this.coreClass = coreClass;
		this.editorAllowed = editorAllowed;
		this.adminAllowed = adminAllowed;
		this.image = image;
		this.shortcut = shortcut;
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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
