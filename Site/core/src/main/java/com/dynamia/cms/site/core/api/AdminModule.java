/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core.api;

import java.util.List;

/**
 *
 * @author mario
 */
public interface AdminModule {

    String getGroup();

    String getName();
    
    AdminModuleOption[] getOptions();
}
