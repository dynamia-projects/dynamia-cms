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

import org.zkoss.util.media.Media
import org.zkoss.zul.Fileupload
import tools.dynamia.actions.ActionRenderer
import tools.dynamia.cms.importer.admin.ui.Importer
import tools.dynamia.domain.services.CrudService
import tools.dynamia.integration.Containers
import tools.dynamia.integration.ProgressMonitor
import tools.dynamia.ui.MessageType
import tools.dynamia.ui.UIMessages
import tools.dynamia.zk.actions.ToolbarbuttonActionRenderer

abstract class ImportExcelAction<T> extends ImportAction {

    private List<T> data

    ImportExcelAction() {
        name = "Import"
        image = "export-xlsx"
        setAttribute("background", "#5cb85c")
        setAttribute("color", "white")
    }

    ImportExcelAction(String name) {
        this()
        setName(name)
    }

    @Override
    void actionPerformed(Importer win) {
        Fileupload.get { event ->
            final Media media = event.media
            if (media != null) {
                String format = media.format

                if (format.endsWith("xls") || format.endsWith("xlsx")) {
                    try {
                        data = importFromExcel(media.streamData, monitor)
                        win.initTable(data)
                    } catch (Exception e) {
                        e.printStackTrace()
                    }
                } else {
                    UIMessages.showMessage("El archivo debe ser en formato excel", MessageType.ERROR)
                }

            } else {
                UIMessages.showMessage("Seleccione archivo de excel para importar", MessageType.ERROR)
            }
        }
    }

    abstract List<T> importFromExcel(InputStream excelFile, ProgressMonitor monitor) throws Exception

    @Override
    void processImportedData(Importer importer) {
        CrudService crudService = Containers.get().findObject(CrudService.class)

        crudService.executeWithinTransaction {
            data.forEach { d -> crudService.save(d) }
        }
        UIMessages.showMessage("Import OK")
        importer.clearTable()
    }

    List<T> getData() {
        if (data == null) {
            data = Collections.EMPTY_LIST
        }
        return data
    }

    @Override
    ActionRenderer getRenderer() {
        return new ToolbarbuttonActionRenderer(true)
    }

}
