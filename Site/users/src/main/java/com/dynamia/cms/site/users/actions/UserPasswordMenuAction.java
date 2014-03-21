/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.users.actions;

import com.dynamia.cms.site.core.api.CMSAction;

/**
 *
 * @author mario_2
 */
@CMSAction
public class UserPasswordMenuAction implements UserMenuAction {

    @Override
    public String getLabel() {
        return "Cambiar Password";
    }

    @Override
    public String getDescription() {
        return "Modificar password para acceder al sitio";
    }

    @Override
    public String getIcon() {
        return null;
    }

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public String action() {
        return "users/changepassword";
    }

}
