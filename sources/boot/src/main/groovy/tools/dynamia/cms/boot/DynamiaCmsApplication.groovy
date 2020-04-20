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

package tools.dynamia.cms.boot

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.web.servlet.ServletRegistrationBean
import org.springframework.context.annotation.*
import org.springframework.web.context.request.RequestContextListener
import org.springframework.web.servlet.ViewResolver
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping
import org.springframework.web.servlet.view.InternalResourceViewResolver
import org.springframework.web.servlet.view.UrlBasedViewResolver
import org.zkoss.zk.au.http.DHtmlUpdateServlet
import org.zkoss.zk.ui.http.DHtmlLayoutServlet
import org.zkoss.zk.ui.http.HttpSessionListener
import tools.dynamia.app.ApplicationInfo
import tools.dynamia.app.RootAppConfiguration
import tools.dynamia.app.controllers.PageNavigationController
import tools.dynamia.app.controllers.RestNavigationController
import tools.dynamia.app.template.TemplateResourceHandler
import tools.dynamia.app.template.TemplateViewResolver
import tools.dynamia.cms.core.SiteMainConfig
import tools.dynamia.domain.jpa.JpaCrudService
import tools.dynamia.domain.services.CrudService

@SpringBootApplication
@EntityScan("tools.dynamia")
@ComponentScan(basePackages = "tools.dynamia", excludeFilters = [
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = [PageNavigationController, RestNavigationController])
])
@Import([RootAppConfiguration.class, SiteMainConfig.class])
class DynamiaCmsApplication {


    static void main(String[] args) {
        SpringApplication.run DynamiaCmsApplication, args
    }

    @Bean
    @Primary
    CrudService jpaCrudService() {
        return new JpaCrudService()
    }

    @Autowired
    private ApplicationInfo applicationInfo

    @Autowired
    private TemplateResourceHandler handler

    /*
 * ZK servlets
 */

    @Bean
    ServletRegistrationBean dHtmlLayoutServlet() {
        Map<String, String> params = new HashMap<String, String>()
        params.put("update-uri", "/zkau")
        DHtmlLayoutServlet dHtmlLayoutServlet = new DHtmlLayoutServlet()
        ServletRegistrationBean reg = new ServletRegistrationBean(dHtmlLayoutServlet, "*.zul", "*.zhtml")
        reg.setLoadOnStartup(1)
        reg.setInitParameters(params)
        return reg
    }

    @Bean
    ServletRegistrationBean dHtmlUpdateServlet() {
        Map<String, String> params = new HashMap<String, String>()
        params.put("update-uri", "/zkau/*")
        ServletRegistrationBean reg = new ServletRegistrationBean(new DHtmlUpdateServlet(), "/zkau/*", "/cms-admin/zkau/*")
        reg.setLoadOnStartup(2)
        reg.setInitParameters(params)
        return reg
    }

    @Bean
    HttpSessionListener httpSessionListener() {
        return new HttpSessionListener()
    }

    @Bean
    RequestContextListener requestContextListener() {
        return new RequestContextListener()
    }

    @Bean
    SimpleUrlHandlerMapping adminTemplateResourcesMapping() {
        handler.relativeContext = "/cms-admin"
        Map<String, Object> map = new HashMap<>()
        map.put("/cms-admin/css/**", handler)
        map.put("/cms-admin/styles/**", handler)
        map.put("/cms-admin/img/**", handler)
        map.put("/cms-admin/images/**", handler)
        map.put("/cms-admin/assets/**", handler)
        map.put("/cms-admin/js/**", handler)
        map.put("/cms-admin/fonts/**", handler)
        map.put("/cms-admin/font/**", handler)
        map.put("/cms-admin/plugins/**", handler)
        map.put("/cms-admin/vendor/**", handler)

        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping()
        mapping.setUrlMap(map)

        return mapping
    }

    ViewResolver zkViewResolver() {
        UrlBasedViewResolver vr = new InternalResourceViewResolver()
        vr.cache = false
        vr.order = 1
        vr.prefix = "/zkau/web/views/"
        vr.suffix = ".zul"
        return vr
    }


    ViewResolver themeZulViewResolver() {
        UrlBasedViewResolver vr = new InternalResourceViewResolver()
        vr.cache = false
        vr.order = 2
        vr.prefix = "/zkau/web/templates/${applicationInfo.getTemplate().toLowerCase()}/views/"
        vr.suffix = ".zul"
        return vr
    }

    /**
     * Resolve ZHTML templates
     *
     * @return
     */

    ViewResolver adminTemplateViewResolver() {
        TemplateViewResolver vr = new TemplateViewResolver(applicationInfo)
        vr.order = Integer.MAX_VALUE
        return vr
    }
}
