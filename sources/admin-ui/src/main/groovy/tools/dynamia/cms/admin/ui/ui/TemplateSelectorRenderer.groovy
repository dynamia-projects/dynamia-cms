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
package tools.dynamia.cms.admin.ui.ui

import org.zkoss.zul.Comboitem
import org.zkoss.zul.ComboitemRenderer
import tools.dynamia.cms.templates.Template

/**
 *
 * @author Mario Serrano Leones
 */
class TemplateSelectorRenderer implements ComboitemRenderer<Template> {

    @Override
    void render(Comboitem cmbtm, Template t, int i) throws Exception {
        cmbtm.value = t.directoryName
        cmbtm.label = String.format("%s (%s)", t.name, t.version)
        cmbtm.description = t.description
    }

}
