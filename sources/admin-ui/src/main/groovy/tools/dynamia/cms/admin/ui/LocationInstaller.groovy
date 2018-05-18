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
