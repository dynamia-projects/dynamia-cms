/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core.api;

import com.dynamia.cms.site.core.domain.ModuleInstance;

/**
 *
 * @author mario
 */
public interface Module {
    
    String getID();

    String getName();

    String getDescription();

    String getVersion();

    ModuleInstance newInstance();

    void init();
}
