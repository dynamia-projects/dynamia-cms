/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.cms.admin.ui

import org.zkoss.zk.ui.Component
import tools.dynamia.cms.core.DynamiaCMS
import tools.dynamia.modules.filemanager.FileManager
import tools.dynamia.zk.navigation.ComponentPage

/**
 *
 * @author mario
 */
class GlobalResourcesPage extends ComponentPage {

    GlobalResourcesPage(String id, String name) {
        super(id, name, null)
    }

    @Override
    Component renderPage() {
        FileManager fileManager = new FileManager(DynamiaCMS.homePath)
        return fileManager
    }

}
