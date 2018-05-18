/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.cms.core
/**
 *
 * @author mario
 */
interface SiteInitializer {

    void init(tools.dynamia.cms.core.domain.Site site)

    void postInit(tools.dynamia.cms.core.domain.Site site)
}
