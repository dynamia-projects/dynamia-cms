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

import org.apache.poi.ss.usermodel.*
import tools.dynamia.commons.BeanUtils
import tools.dynamia.domain.ValidationError
import tools.dynamia.integration.ProgressMonitor

/**
 *
 * @author mario_2
 */
class ImportUtils {

	static String getCellValue(Row row, int cellIndex) {
		String value = null
		Cell cell = row.getCell(cellIndex)
		if (cell != null) {
			if (cell.cellType == Cell.CELL_TYPE_NUMERIC) {
				DataFormatter df = new DataFormatter()
				value = df.formatCellValue(cell)
			} else {
				value = cell.stringCellValue
			}
		}
		if (value != null) {
			value = value.trim()
		}
		return value
	}

	static Object getCellValueObject(Row row, int cellIndex) {
		Cell cell = row.getCell(cellIndex)
		if (cell != null) {
			switch (cell.cellTypeEnum) {
			case CellType.BLANK:
				return null
				case CellType.BOOLEAN:
				return cell.booleanCellValue
				case CellType.STRING:
				return cell.stringCellValue
				case CellType.NUMERIC:
				return cell.numericCellValue
				default:
				return cell.stringCellValue
			}
		}
		return null
	}

	static <T> List<T> importExcel(Class<T> clazz, InputStream excelFile, ProgressMonitor monitor,
								   ImportBeanParser<T> parser) throws Exception {
		if (monitor == null) {
			monitor = new ProgressMonitor()
		}

		Workbook workbook = WorkbookFactory.create(excelFile)
		Sheet sheet = workbook.getSheetAt(0)
        monitor.max = sheet.lastRowNum
		List<T> lineas = new ArrayList<>()
		int filasOK = 0
		for (Row row : sheet) {
			if (row.rowNum == 0) {
                monitor.message = "Procesando Encabezados"
            } else {
				try {
					T bean = parser.parse(row)
					if (bean != null) {
						if (!lineas.contains(bean)) {
							lineas.add(bean)
						}
						filasOK++
                        monitor.message = "Fila " + row.rowNum + " importada Ok"
                    }
				} catch (ValidationError validationError) {
                    monitor.message = "Error importando fila " + row.rowNum + ". " + validationError.message
                }
			}
            monitor.current = row.rowNum
		}

		return lineas
	}

	static void tryToParse(Row row, Object bean, String... fields) {
		if (fields != null && fields.length > 0) {
			for (int i = 0; i < fields.length; i++) {
				try {
					String fieldName = fields[i]
					if (fieldName != null && !fieldName.empty) {
						Object value = getCellValueObject(row, i)
						if (value != null) {
							BeanUtils.setFieldValue(fieldName, bean, value)
						}
					}
				} catch (Throwable e) {
					// TODO: handle exception
				}
			}
		}
	}

}
