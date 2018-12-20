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

import tools.dynamia.cms.core.JavaScriptResource
import tools.dynamia.cms.core.StyleSheetResource

abstract class AbstractModule implements Module {

    private String id
    private String name
    private String description
    private String templateViewName

    private Map<String, Object> metadata = new HashMap<>()
    private List<JavaScriptResource> javaScriptResources = new ArrayList<>()
    private List<StyleSheetResource> styleSheetResources = new ArrayList<>()
    private String[] variablesNames = {}
    private boolean cacheable = true

    AbstractModule(String id, String name, String templateViewName) {
        super()
        this.id = id
        this.name = name
        this.templateViewName = templateViewName
    }

    void setVariablesNames(String... variablesNames) {
        this.variablesNames = variablesNames
    }

    @Override
    String[] getVariablesNames() {
        return variablesNames
    }

    void putMetadata(String key, Object value) {
        metadata.put(key, value)
    }

    @Override
    Map<String, Object> getMetadata() {
        return metadata
    }

    @Override
    String getId() {
        return id
    }

    void setId(String id) {
        this.id = id
    }

    @Override
    String getName() {
        return name
    }

    void setName(String name) {
        this.name = name
    }

    @Override
    String getDescription() {
        return description
    }

    void setDescription(String description) {
        this.description = description
    }

    @Override
    String getTemplateViewName() {
        return templateViewName
    }

    void setTemplateViewName(String templateViewName) {
        this.templateViewName = templateViewName
    }

    @Override
    List<JavaScriptResource> getJavaScriptResources() {
        return javaScriptResources
    }

    @Override
    List<StyleSheetResource> getStyleSheetResources() {
        return styleSheetResources
    }

    void addResource(JavaScriptResource resource) {
        javaScriptResources.add(resource)
    }

    void addResource(StyleSheetResource resource) {
        styleSheetResources.add(resource)
    }

    @Override
    String toString() {
        return name + "(" + templateViewName + ")"
    }

    @Override
    boolean isCacheable() {
        return cacheable
    }

    void setCacheable(boolean cacheable) {
        this.cacheable = cacheable
    }
}
