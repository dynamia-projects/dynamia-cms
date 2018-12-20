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

package tools.dynamia.cms.admin.ui

import tools.dynamia.cms.core.domain.City
import tools.dynamia.cms.core.domain.Country
import tools.dynamia.cms.core.domain.Region
import tools.dynamia.crud.CrudPage
import tools.dynamia.integration.sterotypes.Provider
import tools.dynamia.navigation.Module
import tools.dynamia.navigation.ModuleProvider

@Provider
class LocationInstaller implements ModuleProvider {


    @Override
    Module getModule() {
        Module m = new Module("location", "Locations")
        m.addPage(new CrudPage("countries", "Countries", Country.class))
        m.addPage(new CrudPage("regions", "Regions", Region.class))
        m.addPage(new CrudPage("cities", "Countries", City.class))
        m.icon = "fa-map-marker"
        m.position = 3
        return m
    }
}
