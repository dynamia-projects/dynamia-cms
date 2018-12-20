/*
 * Copyright (C) 2009 - 2019 Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia - South America
 * All Rights Reserved.
 *
 * DynamiaCMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License (LGPL v3) as
 * published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamiaCMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamiaCMS.  If not, see <https://www.gnu.org/licenses/>.
 *
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
