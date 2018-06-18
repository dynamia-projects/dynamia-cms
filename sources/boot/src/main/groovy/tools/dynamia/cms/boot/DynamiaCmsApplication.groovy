package tools.dynamia.cms.boot

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.web.servlet.ServletRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary
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
import tools.dynamia.app.template.ChainableUrlBasedViewResolver
import tools.dynamia.app.template.TemplateResourceHandler
import tools.dynamia.app.template.TemplateViewResolver
import tools.dynamia.cms.core.SiteMainConfig
import tools.dynamia.domain.jpa.JpaCrudService
import tools.dynamia.domain.services.CrudService
import tools.dynamia.zk.navigation.ZKNavigationController

@SpringBootApplication
@EntityScan("tools.dynamia")
@ComponentScan(basePackages = "tools.dynamia", excludeFilters = [
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = ZKNavigationController.class)
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
        return new RequestContextListener();
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