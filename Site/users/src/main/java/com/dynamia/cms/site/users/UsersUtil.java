/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.users;

import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author mario_2
 */
public class UsersUtil {

    public static void setupUserFormVar(ModelAndView mv, UserForm userForm) {
        mv.addObject("userForm", userForm);
    }

}
