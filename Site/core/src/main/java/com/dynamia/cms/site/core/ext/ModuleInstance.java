/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core.ext;

/**
 *
 * @author mario
 */
public interface ModuleInstance {

    Module getContext();

    ModuleInstanceConfig getConfiguration();

    String getPosition();

    String render();
}
