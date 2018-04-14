/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.cms.admin.importer

import tools.dynamia.actions.AbstractAction
import tools.dynamia.actions.ActionEvent
import tools.dynamia.actions.ActionRenderer
import tools.dynamia.cms.admin.importer.ui.Importer
import tools.dynamia.integration.ProgressMonitor
import tools.dynamia.zk.actions.ToolbarbuttonActionRenderer

/**
 *
 * @author mario_2
 */
abstract class ImportAction extends AbstractAction {

    private ProgressMonitor monitor
    private boolean procesable = true

    @Override
    void actionPerformed(ActionEvent evt) {
        Importer win = (Importer) evt.getSource()
        actionPerformed(win)
        if (isProcesable()) {
            win.setCurrentAction(this)
        }
    }

    abstract void actionPerformed(Importer importer)

    abstract void processImportedData(Importer importer)

    @Override
    ActionRenderer getRenderer() {
        return new ToolbarbuttonActionRenderer(true)
    }

    void setMonitor(ProgressMonitor monitor) {
        this.monitor = monitor
    }

    ProgressMonitor getMonitor() {
        return monitor
    }

    boolean isProcesable() {
        return procesable
    }

}
