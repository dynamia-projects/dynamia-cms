/*
 * Copyright (C) 2009 - 2020 Dynamia Soluciones IT S.A.S - NIT 900302344-1
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
