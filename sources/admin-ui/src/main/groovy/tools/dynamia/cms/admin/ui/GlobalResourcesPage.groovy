/*
 *  Copyright (c) 2018 Dynamia Soluciones IT SAS and the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.cms.admin.ui

import org.zkoss.zk.ui.Component
import tools.dynamia.cms.core.DynamiaCMS
import tools.dynamia.modules.filemanager.FileManager
import tools.dynamia.zk.navigation.ComponentPage

/**
 *
 * @author Mario Serrano Leones
 */
class GlobalResourcesPage extends ComponentPage {

    GlobalResourcesPage(String id, String name) {
        super(id, name, null)
    }

    @Override
    Component renderPage() {
        FileManager fileManager = new FileManager(DynamiaCMS.homePath)
        return fileManager
    }

}
