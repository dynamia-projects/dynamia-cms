/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.users.actions;

/**
 *
 * @author mario_2
 */
public interface UserMenuAction {

    public String getLabel();

    public String getDescription();

    public String getIcon();

    public int getOrder();

    public String action();

}
