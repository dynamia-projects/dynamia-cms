package tools.dynamia.cms.core.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import tools.dynamia.cms.core.SiteContext
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.core.sitemap.SiteMap
import tools.dynamia.cms.core.sitemap.SiteMapProvider
import tools.dynamia.integration.Containers

@Controller
class SiteMapController {

    @RequestMapping("/sitemap.xml")
    @ResponseBody
    SiteMap getSiteMapXML() {
        Site site = SiteContext.get().current

        SiteMap map = new SiteMap()
        Containers.get().findObjects(SiteMapProvider.class).forEach { pv ->
            try {
                pv.get(site).forEach { url -> map.addUrl(url) }
            } catch (Exception e) {
                e.printStackTrace()
            }
        }

        return map

    }
}
