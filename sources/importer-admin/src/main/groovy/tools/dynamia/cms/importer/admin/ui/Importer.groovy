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
package tools.dynamia.cms.importer.admin.ui

import org.zkoss.zk.ui.Component
import org.zkoss.zk.ui.event.Events
import org.zkoss.zk.ui.util.Clients
import org.zkoss.zul.*
import tools.dynamia.actions.ActionEvent
import tools.dynamia.actions.ActionEventBuilder
import tools.dynamia.cms.importer.admin.ImportAction
import tools.dynamia.cms.importer.admin.ImportOperation
import tools.dynamia.commons.StringUtils
import tools.dynamia.integration.ProgressMonitor
import tools.dynamia.ui.MessageType
import tools.dynamia.ui.UIMessages
import tools.dynamia.viewers.Field
import tools.dynamia.viewers.ViewDescriptor
import tools.dynamia.viewers.impl.DefaultViewDescriptor
import tools.dynamia.viewers.util.Viewers
import tools.dynamia.zk.actions.ActionToolbar
import tools.dynamia.zk.util.ZKUtil
import tools.dynamia.zk.viewers.table.TableView

/**
 *
 * @author Mario Serrano Leones
 */
class Importer extends Window implements ActionEventBuilder {

    private ActionToolbar toolbar = new ActionToolbar(this)

    private Progressmeter progress = new Progressmeter()
    private Label progressLabel = new Label()

    private Borderlayout layout = new Borderlayout()
    private ImportAction currentAction
    private ImportOperation currentOperation

    private Button btnProcesar
    private Button btnCancelar

    private ViewDescriptor tableDescriptor = new DefaultViewDescriptor(null, "table")

    private boolean operationRunning

    private TableView table

    Importer() {
        buildLayout()
    }

    @Override
    ActionEvent buildActionEvent(Object source, Map<String, Object> params) {

        resetProgress()
        return new ActionEvent(null, this, params)
    }

    private void resetProgress() {
        progress.visible = false
        progress.value = 0
        progressLabel.value = ""
        Clients.clearBusy(layout.center.firstChild)
    }

    void updateProgress(ProgressMonitor monitor) {

        if (monitor.current > 0) {
            try {

                int value = (int) (monitor.current * 100 / monitor.max)
                if (!progress.visible) {
                    progress.visible = true
                }
                progress.value = value
                progressLabel.value = monitor.message

                Clients.showBusy(layout.center.firstChild, monitor.message)
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

    }

    private void buildLayout() {
        hflex = "1"

        vflex = "1"
        appendChild(layout)
        layout.hflex = "1"
        layout.vflex = "1"

        layout.appendChild(new Center())
        layout.appendChild(new North())
        layout.appendChild(new South())
        layout.appendChild(new East())

        layout.north.appendChild(toolbar)
        Hlayout controls = new Hlayout()
        layout.south.appendChild(controls)

        this.btnProcesar = new Button("Procesar datos importados")
        btnProcesar.style = "margin:4px"
        btnProcesar.addEventListener(Events.ON_CLICK, { processImportedData() })

        this.btnCancelar = new Button("Cancelar")
        btnCancelar.style = "margin:4px"
        btnCancelar.addEventListener(Events.ON_CLICK, {
            if (currentOperation == null) {
                return
            }

            UIMessages.showQuestion("Esta seguro que desea cancelar: " + currentOperation.name + "?", {
                cancel()
            })

        })

        progress.hflex = "1"
        controls.appendChild(btnProcesar)
        controls.appendChild(btnCancelar)

        operationStatus = false
    }

    private void processImportedData() {
        if (currentAction != null) {

            if (currentOperation != null) {
                UIMessages.showMessage("Existe un proceso de importacion ejecuntandose en este momento",
                        MessageType.WARNING)
                return
            }

            UIMessages.showQuestion(
                    "Esta seguro que desea procesar los datos importados? Esta accion puede tardar varios minutos.",
                    {
                        resetProgress()
                        currentAction.processImportedData(this)
                    })

        } else {
            UIMessages.showMessage("NO HAY DATOS QUE PROCESAR", MessageType.WARNING)
        }
    }

    void cancel() {
        if (currentOperation != null) {
            currentOperation.cancelGracefully()
            currentOperation = null

        }
    }

    void addAction(ImportAction action) {
        toolbar.addAction(action)
    }

    void setCurrentAction(ImportAction importAction) {
        this.currentAction = importAction
    }

    void setCurrentOperation(ImportOperation currentProcess) {
        this.currentOperation = currentProcess
    }

    ImportOperation getCurrentOperation() {
        return currentOperation
    }

    void setOperationStatus(boolean running) {
        this.operationRunning = running
        checkRunning()
        progress.visible = running
    }

    @Override
    void onClose() {
        cancel()
    }

    private void checkRunning() {
        btnCancelar.disabled = !operationRunning
        btnProcesar.disabled = operationRunning
        closable = !operationRunning

        for (Component child : (toolbar.children)) {
            if (child instanceof Button) {
                Button button = (Button) child
                button.disabled = operationRunning
            }
        }

        if (!operationRunning) {
            resetProgress()
        }
    }

    boolean isOperationRunning() {
        return operationRunning
    }

    void addColumn(String name) {
        String firstChar = StringUtils.getFirstCharacter(name).toLowerCase()
        name = firstChar + name.substring(1)

        tableDescriptor.addField(new Field(name))
    }

    void addColumn(String name, String path) {
        Field field = new Field(path)
        field.label = name
        tableDescriptor.addField(field)
    }

    void addColumn(String name, String path, String reference) {
        Field field = new Field(path)
        field.label = name
        field.component = "entityrefpicker"
        field.addParam("entityAlias", reference)
        tableDescriptor.addField(field)

    }

    void initTable(List data) {
        this.table = (TableView) Viewers.getView(tableDescriptor)

        table.sizedByContent = true

        if (data != null && !data.empty) {
            table.value = data
        }

        layout.center.children.clear()
        layout.center.appendChild(table)
    }

    void clearTable() {
        initTable(Collections.EMPTY_LIST)

    }

    void show(String title) {
        ZKUtil.showDialog(title, this, "90%", "90%")

    }

    Object getSelected() {
        if (table != null) {
            return table.selected
        } else {
            return null
        }
    }

}
