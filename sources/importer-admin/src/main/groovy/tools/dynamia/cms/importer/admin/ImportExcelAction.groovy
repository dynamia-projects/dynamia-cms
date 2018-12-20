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
