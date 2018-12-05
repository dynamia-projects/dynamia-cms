/*
 *  Copyright (c) 2018 Dynamia Soluciones IT SAS and the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.cms.importer.admin

import tools.dynamia.actions.AbstractAction
import tools.dynamia.actions.ActionEvent
import tools.dynamia.actions.ActionRenderer
import tools.dynamia.cms.importer.admin.ui.Importer
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
        Importer win = (Importer) evt.source
        actionPerformed(win)
        if (procesable) {
            win.currentAction = this
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
