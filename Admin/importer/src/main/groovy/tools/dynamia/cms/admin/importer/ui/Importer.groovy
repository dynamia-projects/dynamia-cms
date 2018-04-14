/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.cms.admin.importer.ui

import java.io.InputStream
import java.util.Collections
import java.util.List
import java.util.Map
import java.util.function.BiConsumer

import org.zkoss.zk.ui.Component
import org.zkoss.zk.ui.event.Events
import org.zkoss.zk.ui.util.Clients
import org.zkoss.zul.Borderlayout
import org.zkoss.zul.Button
import org.zkoss.zul.Center
import org.zkoss.zul.East
import org.zkoss.zul.Hlayout
import org.zkoss.zul.Label
import org.zkoss.zul.ListModel
import org.zkoss.zul.North
import org.zkoss.zul.Progressmeter
import org.zkoss.zul.South
import org.zkoss.zul.Window

import tools.dynamia.actions.ActionEvent
import tools.dynamia.actions.ActionEventBuilder
import tools.dynamia.cms.admin.importer.ImportAction
import tools.dynamia.cms.admin.importer.ImportExcelAction
import tools.dynamia.cms.admin.importer.ImportOperation
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
 * @author mario_2
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
        progress.setVisible(false)
        progress.setValue(0)
        progressLabel.setValue("")
        Clients.clearBusy(layout.getCenter().getFirstChild())
    }

    void updateProgress(ProgressMonitor monitor) {

        if (monitor.getCurrent() > 0) {
            try {

                int value = (int) (monitor.getCurrent() * 100 / monitor.getMax())
                if (!progress.isVisible()) {
                    progress.setVisible(true)
                }
                progress.setValue(value)
                progressLabel.setValue(monitor.getMessage())

                Clients.showBusy(layout.getCenter().getFirstChild(), monitor.getMessage())
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

    }

    private void buildLayout() {
        setHflex("1")

        setVflex("1")
        appendChild(layout)
        layout.setHflex("1")
        layout.setVflex("1")

        layout.appendChild(new Center())
        layout.appendChild(new North())
        layout.appendChild(new South())
        layout.appendChild(new East())

        layout.getNorth().appendChild(toolbar)
        Hlayout controls = new Hlayout()
        layout.getSouth().appendChild(controls)

        this.btnProcesar = new Button("Procesar datos importados")
        btnProcesar.setStyle("margin:4px")
        btnProcesar.addEventListener(Events.ON_CLICK, { processImportedData() })

        this.btnCancelar = new Button("Cancelar")
        btnCancelar.setStyle("margin:4px")
        btnCancelar.addEventListener(Events.ON_CLICK, {
            if (currentOperation == null) {
                return
            }

            UIMessages.showQuestion("Esta seguro que desea cancelar: " + currentOperation.getName() + "?", {
                cancel()
            })

        })

        progress.setHflex("1")
        controls.appendChild(btnProcesar)
        controls.appendChild(btnCancelar)

        setOperationStatus(false)
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
        progress.setVisible(running)
    }

    @Override
    void onClose() {
        cancel()
    }

    private void checkRunning() {
        btnCancelar.setDisabled(!operationRunning)
        btnProcesar.setDisabled(operationRunning)
        setClosable(!operationRunning)

        for (Component child : toolbar.getChildren()) {
            if (child instanceof Button) {
                Button button = (Button) child
                button.setDisabled(operationRunning)
            }
        }

        if (!isOperationRunning()) {
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
        field.setLabel(name)
        tableDescriptor.addField(field)
    }

    void addColumn(String name, String path, String reference) {
        Field field = new Field(path)
        field.setLabel(name)
        field.setComponent("entityrefpicker")
        field.addParam("entityAlias", reference)
        tableDescriptor.addField(field)

    }

    void initTable(List data) {
        this.table = (TableView) Viewers.getView(tableDescriptor)

        table.setSizedByContent(true)

        if (data != null && !data.isEmpty()) {
            table.setValue(data)
        }

        layout.getCenter().getChildren().clear()
        layout.getCenter().appendChild(table)
    }

    void clearTable() {
        initTable(Collections.EMPTY_LIST)

    }

    void show(String title) {
        ZKUtil.showDialog(title, this, "90%", "90%")

    }

    Object getSelected() {
        if (table != null) {
            return table.getSelected()
        } else {
            return null
        }
    }

}
