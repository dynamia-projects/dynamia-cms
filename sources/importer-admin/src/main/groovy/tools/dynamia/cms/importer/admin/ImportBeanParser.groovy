package tools.dynamia.cms.importer.admin

import org.apache.poi.ss.usermodel.Row

@FunctionalInterface
interface ImportBeanParser<T> {

	T parse(Row row)

}
