package tools.dynamia.cms.site.users

import org.springframework.web.WebApplicationInitializer
import org.springframework.web.filter.DelegatingFilterProxy

import javax.servlet.FilterRegistration
import javax.servlet.ServletContext
import javax.servlet.ServletException

class SecurityWebAppInitializer implements WebApplicationInitializer {

    @Override
    void onStartup(ServletContext servletContext) throws ServletException {
        FilterRegistration filter = servletContext.addFilter("springSecurityFilterChain", DelegatingFilterProxy.class)
        filter.addMappingForUrlPatterns(null, false, "/*")
    }

}
