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
package tools.dynamia.cms.site.core.api

import tools.dynamia.cms.site.core.JavaScriptResource
import tools.dynamia.cms.site.core.StyleSheetResource

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
        return getName() + "(" + getTemplateViewName() + ")"
    }

    @Override
    boolean isCacheable() {
        return cacheable
    }

    void setCacheable(boolean cacheable) {
        this.cacheable = cacheable
    }
}
