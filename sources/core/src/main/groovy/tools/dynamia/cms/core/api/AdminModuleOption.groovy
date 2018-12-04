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
package tools.dynamia.cms.core.api
/**
 *
 * @author Mario Serrano Leones
 */
class AdminModuleOption implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -8763631027547751496L
    private String id
    private String name
    private Class coreClass
    private boolean editorAllowed = true
    private boolean adminAllowed = true
    private String image
    private boolean shortcut
    private String path

    AdminModuleOption() {

    }

    AdminModuleOption(String id, String name) {
        this.id = id
        this.name = name
    }

    AdminModuleOption(String id, String name, Class coreClass) {
        this.id = id
        this.name = name
        this.coreClass = coreClass
    }

    AdminModuleOption(String id, String name, Class coreClass, boolean editorAllowed, boolean adminAllowed) {
        super()
        this.id = id
        this.name = name
        this.coreClass = coreClass
        this.editorAllowed = editorAllowed
        this.adminAllowed = adminAllowed
    }

    AdminModuleOption(String id, String name, Class coreClass, boolean editorAllowed, boolean adminAllowed, String image,
                      boolean shortcut) {
        super()
        this.id = id
        this.name = name
        this.coreClass = coreClass
        this.editorAllowed = editorAllowed
        this.adminAllowed = adminAllowed
        this.image = image
        this.shortcut = shortcut
    }

    AdminModuleOption(String id, String name, String path, boolean editorAllowed, boolean adminAllowed, String image, boolean shortcut) {
        super()
        this.id = id
        this.name = name
        this.path = path
        this.editorAllowed = editorAllowed
        this.adminAllowed = adminAllowed
        this.image = image
        this.shortcut = shortcut
    }

    String getId() {
        return id
    }

    void setId(String id) {
        this.id = id
    }

    String getName() {
        return name
    }

    void setName(String name) {
        this.name = name
    }

    Class getCoreClass() {
        return coreClass
    }

    void setCoreClass(Class coreClass) {
        this.coreClass = coreClass
    }

    boolean isEditorAllowed() {
        return editorAllowed
    }

    void setEditorAllowed(boolean editorAllowed) {
        this.editorAllowed = editorAllowed
    }

    boolean isAdminAllowed() {
        return adminAllowed
    }

    void setAdminAllowed(boolean adminAllowed) {
        this.adminAllowed = adminAllowed
    }

    String getImage() {
        return image
    }

    void setImage(String image) {
        this.image = image
    }

    boolean isShortcut() {
        return shortcut
    }

    void setShortcut(boolean shortcut) {
        this.shortcut = shortcut
    }

    String getPath() {
        return path
    }

    void setPath(String path) {
        this.path = path
    }

}
