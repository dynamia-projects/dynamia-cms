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
public class UserProfileMenuAction implements UserMenuAction {

    @Override
    public String getLabel() {
        return "Mi informacion personal";
    }

    @Override
    public String getDescription() {
        return "Actualizar datos personales";
    }

    @Override
    public String getIcon() {
        return null;
    }

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String action() {
        return "users/profile";
    }

}
