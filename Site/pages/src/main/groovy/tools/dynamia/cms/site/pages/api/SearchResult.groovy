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
package tools.dynamia.cms.site.pages.api
/**
 *
 * @author Mario Serrano Leones
 */
class SearchResult implements Serializable {

    private String viewName = "site/page"
    private final Map<String, Object> entries = new HashMap<>()
    private boolean keepSearching

    SearchResult() {
    }

    SearchResult(String viewName, boolean keepSearching) {
        this.viewName = viewName
        this.keepSearching = keepSearching
    }

    String getViewName() {
        return viewName
    }

    void setViewName(String viewName) {
        this.viewName = viewName
    }

    boolean isKeepSearching() {
        return keepSearching
    }

    void setKeepSearching(boolean keepSearching) {
        this.keepSearching = keepSearching

    }

    void addEntry(String name, Object value) {
        entries.put(name, value)
    }

    Map<String, Object> getEntries() {
        return entries
    }

}
