/* 
 * Copyright 2016 Dynamia Soluciones IT SAS and the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tools.dynamia.cms.site.users

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
