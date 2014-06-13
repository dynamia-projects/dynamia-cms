/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.users;

import com.dynamia.tools.commons.StringUtils;
import com.dynamia.tools.domain.util.CrudServiceListener;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 *
 * @author mario
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true, prePostEnabled = true, securedEnabled = true)
@Order(1)
public class UsersConfig extends WebSecurityConfigurerAdapter {

    @Autowired(required = false)
    private List<IgnoringAntMatcher> ignorings;

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.authenticationProvider(authenticationProvider());

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests().anyRequest().permitAll();
        http.csrf().disable();
        http
                .formLogin()
                .authenticationDetailsSource(new AuthenticationDetailsSource())
                .usernameParameter("username")
                .passwordParameter("password")
                .loginPage(loginPage()).permitAll()
                .and()
                .logout()
                .logoutUrl(logutURL()).deleteCookies("JSESSIONID")
                .permitAll()
                .and()
                .rememberMe().key(StringUtils.randomString())
                .and()
                .httpBasic();

    }

    protected String logutURL() {
        return "/users/logout";
    }

    protected String loginPage() {
        return "/users/login";
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(defaultIgnoringAntMatcher().matchers());

        if (ignorings != null) {
            for (IgnoringAntMatcher ignoringAntMatcher : ignorings) {
                web.ignoring().antMatchers(ignoringAntMatcher.matchers());
            }
        }
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new UsersAuthenticationProvider();
    }

    @Bean
    public CrudServiceListener securityCrudServiceListener() {
        return new SecurityCrudServiceListener();
    }

    @Bean
    public ApplicationListener securityApplicationListener() {
        return new SpringSecurtyApplicationListener();
    }

    @Bean
    public IgnoringAntMatcher defaultIgnoringAntMatcher() {
        return new DefaultIgnoringAntMatcher();
    }

}
