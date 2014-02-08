/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core.api;

/**
 *
 * @author mario
 */
public interface Module {

    String getName();

    String getDescription();

    void init(ModuleContext context);
}
