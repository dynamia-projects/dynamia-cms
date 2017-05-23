/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.cms.admin.users;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import tools.dynamia.cms.site.users.AuthenticationDetailsSource;
import tools.dynamia.cms.site.users.UsersConfig;

/**
 *
 * @author Mario Serrano Leones
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true, prePostEnabled = true, securedEnabled = true)
@Order(0)
public class AdminUsersConfig extends UsersConfig {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests().anyRequest().hasAnyRole("ADMIN", "EDITOR");
        http.csrf().disable();
        http
                .formLogin()
                .authenticationDetailsSource(new AuthenticationDetailsSource())
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/", false)
                .loginPage("/login")
                .permitAll()
                .and()              
                .httpBasic();

    }

}
