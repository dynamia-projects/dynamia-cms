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
package tools.dynamia.cms.admin.ui.customizer

import tools.dynamia.cms.core.SiteContext
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.viewers.ViewCustomizer
import tools.dynamia.zk.crud.ui.EntityPickerBox
import tools.dynamia.zk.viewers.form.FormView

class SiteFormCustomizer implements ViewCustomizer<FormView<Site>> {

	@Override
    void customize(FormView<Site> view) {
		EntityPickerBox parentPicker = (EntityPickerBox) view.getFieldComponent("parent").inputComponent
        parentPicker.disabled = !SiteContext.get().superAdmin

    }

}
