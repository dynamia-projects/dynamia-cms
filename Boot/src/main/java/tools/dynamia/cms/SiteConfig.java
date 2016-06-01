/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.cms;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import tools.dynamia.cms.site.core.SiteHandleInterceptor;

/**
 *
 * @author mario
 */
@Configuration
public class SiteConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LocaleChangeInterceptor());
        registry.addInterceptor(siteHandlerInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/resources/**",
                        "/css/**",
                        "/styles/**",
                        "/js/**",
                        "/fonts/**",
                        "/font/**",
                        "/assets/**",
                        "/images/**",
                        "/img/**"
                );
    }

    @Bean
    public HandlerInterceptor siteHandlerInterceptor() {
        return new SiteHandleInterceptor();
    }

}
