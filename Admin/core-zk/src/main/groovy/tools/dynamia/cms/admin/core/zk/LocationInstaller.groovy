package tools.dynamia.cms.admin.core.zk;

import tools.dynamia.cms.site.core.domain.City;
import tools.dynamia.cms.site.core.domain.Country;
import tools.dynamia.cms.site.core.domain.Region;
import tools.dynamia.crud.CrudPage;
import tools.dynamia.integration.sterotypes.Provider;
import tools.dynamia.navigation.Module;
import tools.dynamia.navigation.ModuleProvider;

@Provider
public class LocationInstaller implements ModuleProvider {


    @Override
    public Module getModule() {
        Module m = new Module("location", "Locations");
        m.addPage(new CrudPage("countries", "Countries", Country.class));
        m.addPage(new CrudPage("regions", "Regions", Region.class));
        m.addPage(new CrudPage("cities", "Countries", City.class));
        m.setIcon("fa-map-marker");
        m.setPosition(3);
        return m;
    }
}
