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
package tools.dynamia.cms.menus.admin

import tools.dynamia.cms.core.api.AdminModule
import tools.dynamia.cms.core.api.AdminModuleOption
import tools.dynamia.cms.core.api.CMSModule
import tools.dynamia.cms.menus.domain.Menu

/**
 *
 * @author Mario Serrano Leones
 */
@CMSModule
class MenuAdminModule implements AdminModule {

    @Override
    String getGroup() {
        return "content"
    }

    @Override
    String getName() {
        return "Content"
    }

    @Override
    String getImage() {
        return null
    }

    @Override
    AdminModuleOption[] getOptions() {
        return [
            new AdminModuleOption("menus", "Menus", Menu.class,true,true,"indent",true)
        ]
    }

}
