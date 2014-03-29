/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.users;

import org.springframework.stereotype.Component;

/**
 *
 * @author mario
 */

public class DefaultIgnoringAntMatcher implements IgnoringAntMatcher {

    @Override
    public String[] matchers() {
        return new String[]{
            "/styles/**", "/css/**", "/images/**", "/img/**", "/js/**","/zkau/**","/resources/**","/fonts/**",
            "/font/**","/plugins/**"
                
        };
    }

}
