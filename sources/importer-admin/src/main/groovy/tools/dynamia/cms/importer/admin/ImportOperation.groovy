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

package tools.dynamia.cms.importer.admin

import org.apache.commons.lang.time.DurationFormatUtils
import tools.dynamia.cms.importer.admin.ui.Importer
import tools.dynamia.domain.ValidationError
import tools.dynamia.integration.ProgressEvent
import tools.dynamia.integration.ProgressMonitor
import tools.dynamia.integration.ProgressMonitorListener
import tools.dynamia.ui.MessageType
import tools.dynamia.ui.UIMessages
import tools.dynamia.zk.util.LongOperation

abstract class ImportOperation extends LongOperation implements ProgressMonitorListener {

    private ProgressMonitor monitor
    private Importer importer
    private String name
    private long updateProgressRate = 3000
    private long lastCheckTime
    private long currentTime
    private boolean cancelledGracefully
    private long startTime = 0

    ImportOperation(String name, Importer importer) {
        super()
        this.name = name
        this.importer = importer
        init()
    }

    ImportOperation(String name, Importer importer, long updateProgressRate) {
        super()
        this.name = name
        this.importer = importer
        this.updateProgressRate = updateProgressRate
        init()
    }

    private void init() {
        execute { doImport() }
        onFinish { importFinish() }
    }

    @Override
    void progressChanged(ProgressEvent evt) {
        try {

            currentTime = System.currentTimeMillis()

            if (lastCheckTime == 0) {
                lastCheckTime = currentTime
            }

            long elapsedTime = currentTime - startTime
            if ((currentTime - lastCheckTime) >= updateProgressRate) {
                lastCheckTime = currentTime

                double position = monitor.current
                double total = monitor.max

                if (startTime == 0) {
                    startTime = currentTime
                }

                long estimatedRemaining = (long) (elapsedTime / position * (total - position))

                activate()
                monitor.message = monitor.message + " - Faltan <b>"
                        + DurationFormatUtils.formatDuration(estimatedRemaining, "HH:mm:ss") + "</b> (h:m:s)"
                importer.updateProgress(monitor)
                deactivate()
            }
        } catch (Exception e) {
            // Ignore

        }
    }

    private void doImport() {
        monitor = new ProgressMonitor(this)
        try {
            if (checkCurrentOperation()) {

                importer.currentOperation = this
                operationStatus = true

                execute(monitor)

                importer.currentOperation = null
                operationStatus = false
            } else {
                operationStatus = false
                cancel()
            }
        } catch (InterruptedException e) {
            throw new ImportOperationException(e)
        } catch (ValidationError e) {
            try {
                activate()
                UIMessages.showMessage(e.message, MessageType.ERROR)
                importer.operationStatus = false
                deactivate()
            } catch (Exception e2) {
                // TODO: handle exception
            }
        } catch (Exception e) {
            throw new ImportOperationException(e)
        }
    }

    void cancelGracefully() {
        cancelledGracefully = true
        monitor.max = -1
        monitor.current = -1
        monitor.stop()
    }

    private void importFinish() {
        if (!cancelledGracefully) {
            onFinish(monitor)
        } else {
            onCancel(monitor)
        }
    }

    protected void onFinish(ProgressMonitor monitor) {
        // TODO Auto-generated method stub
    }

    protected void onCancel(ProgressMonitor monitor) {
        // TODO Auto-generated method stub

    }

    private void setOperationStatus(boolean b) {
        try {
            activate()
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        importer.operationStatus = b
        deactivate()
    }

    String getName() {
        return name
    }

    private boolean checkCurrentOperation() {
        if (importer.currentOperation != null) {
            UIMessages.showMessage("No puede ejecutar este proceso, existe una operacion de importacion activa "
                    + importer.currentOperation.name, null)
            return false
        } else {
            return true
        }
    }

    abstract void execute(ProgressMonitor monitor) throws Exception

}
