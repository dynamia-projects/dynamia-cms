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
package tools.dynamia.cms.admin.ui.actions

import tools.dynamia.actions.ActionEvent
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.core.DynamiaCMS
import tools.dynamia.ui.UIMessages
import tools.dynamia.zk.crud.cfg.SaveConfigAction
import tools.dynamia.zk.navigation.ZKNavigationManager

@InstallAction
class ApplySystemConfigAction extends SaveConfigAction {

    ApplySystemConfigAction() {
        name = "Apply"
        image = "refresh"
        position = 1
    }

    @Override
    void actionPerformed(ActionEvent evt) {
        super.actionPerformed(evt)
        DynamiaCMS.reloadHomePath()
        ZKNavigationManager.instance.refresh()
        UIMessages.showMessage("Done")

    }

}
