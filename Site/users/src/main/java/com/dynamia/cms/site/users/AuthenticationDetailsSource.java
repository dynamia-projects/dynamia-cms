/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.users;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

/**
 *
 * @author mario_2
 */

public class AuthenticationDetailsSource extends WebAuthenticationDetailsSource{

    

    @Override
    public WebAuthenticationDetails buildDetails(HttpServletRequest context) {
    return new AuthenticationDetails(context);
    }

    


   

}
