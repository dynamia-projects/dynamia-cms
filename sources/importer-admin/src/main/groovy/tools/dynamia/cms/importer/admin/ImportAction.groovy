/*
 * Copyright (C) 2009 - 2019 Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia - South America
 * All Rights Reserved.
 *
 * DynamiaCMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License (LGPL v3) as
 * published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamiaCMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamiaCMS.  If not, see <https://www.gnu.org/licenses/>.
 *
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
 * @author Mario Serrano Leones
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
