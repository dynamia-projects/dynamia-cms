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
package tools.dynamia.cms.users

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import tools.dynamia.commons.StringUtils
import tools.dynamia.domain.util.CrudServiceListener

/**
 *
 * @author Mario Serrano Leones
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true, prePostEnabled = true, securedEnabled = true)
@Order(1)
class UsersConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private List<IgnoringAntMatcher> ignorings

    @Override
    @Bean
    AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean()
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.authenticationProvider(authenticationProvider())

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests().anyRequest().permitAll()
        http.sessionManagement().enableSessionUrlRewriting(false)
        http.csrf().disable()
        http        		
                .formLogin()
                .authenticationDetailsSource(new AuthenticationDetailsSource())
                .usernameParameter("username")
                .passwordParameter("password")
                .loginPage(loginPage())
                	.permitAll()
                .and()     
                .rememberMe().key(StringUtils.randomString())
                .and()
                .httpBasic()
                .and()
                .exceptionHandling().accessDeniedPage(loginPage())

        http.headers()        
                .cacheControl()                
                .and().xssProtection()
                .and().httpStrictTransportSecurity()
                .and().contentTypeOptions()
                .and().frameOptions().disable()


    }

    protected String logutURL() {
        return "/users/logout"
    }

    protected String loginPage() {
        return "/users/login"
    }

    @Override
    void configure(WebSecurity web) throws Exception {

        if (ignorings != null) {
            for (IgnoringAntMatcher ignoringAntMatcher : ignorings) {
                web.ignoring().antMatchers(ignoringAntMatcher.matchers())
            }
        }
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        return new UsersAuthenticationProvider()
    }

    @Bean
    CrudServiceListener securityCrudServiceListener() {
        return new SecurityCrudServiceListener()
    }

    @Bean
    ApplicationListener securityApplicationListener() {
        return new SpringSecurtyApplicationListener()
    }

}
