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
