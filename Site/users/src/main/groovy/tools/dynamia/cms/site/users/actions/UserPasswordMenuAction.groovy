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
package tools.dynamia.cms.site.users.actions

import tools.dynamia.cms.site.core.api.CMSAction

/**
 *
 * @author Mario Serrano Leones
 */
@CMSAction
class UserPasswordMenuAction implements UserMenuAction {

    @Override
    String getLabel() {
        return "Cambiar Password"
    }

    @Override
    String getDescription() {
        return "Modificar password para acceder al sitio"
    }

    @Override
    String getIcon() {
        return null
    }

    @Override
    int getOrder() {
        return 1
    }

    @Override
    String action() {
        return "users/changepassword"
    }

}
