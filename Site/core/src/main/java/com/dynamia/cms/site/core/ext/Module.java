/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core.ext;

import com.dynamia.tools.viewers.ViewDescriptor;

/**
 *
 * @author mario
 */
public interface Module {

    String getName();

    String getDescription();

    String getVersion();

    ViewDescriptor getConfigDescriptor();

    ModuleInstance newInstance();
    
    void init();
}
