/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.cms.admin.importer

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
			if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				DataFormatter df = new DataFormatter()
				value = df.formatCellValue(cell)
			} else {
				value = cell.getStringCellValue()
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
			switch (cell.getCellTypeEnum()) {
			case CellType.BLANK:
				return null
				case CellType.BOOLEAN:
				return cell.getBooleanCellValue()
				case CellType.STRING:
				return cell.getStringCellValue()
				case CellType.NUMERIC:
				return cell.getNumericCellValue()
				default:
				return cell.getStringCellValue()
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
		monitor.setMax(sheet.getLastRowNum())
		List<T> lineas = new ArrayList<>()
		int filasOK = 0
		for (Row row : sheet) {
			if (row.getRowNum() == 0) {
				monitor.setMessage("Procesando Encabezados")
			} else {
				try {
					T bean = parser.parse(row)
					if (bean != null) {
						if (!lineas.contains(bean)) {
							lineas.add(bean)
						}
						filasOK++
						monitor.setMessage("Fila " + row.getRowNum() + " importada Ok")
					}
				} catch (ValidationError validationError) {
					monitor.setMessage(
							"Error importando fila " + row.getRowNum() + ". " + validationError.getMessage())
				}
			}
			monitor.setCurrent(row.getRowNum())
		}

		return lineas
	}

	static void tryToParse(Row row, Object bean, String... fields) {
		if (fields != null && fields.length > 0) {
			for (int i = 0; i < fields.length; i++) {
				try {
					String fieldName = fields[i]
					if (fieldName != null && !fieldName.isEmpty()) {
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
