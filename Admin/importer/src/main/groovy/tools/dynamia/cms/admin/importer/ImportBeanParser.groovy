package tools.dynamia.cms.admin.importer

import org.apache.poi.ss.usermodel.Row

@FunctionalInterface
interface ImportBeanParser<T> {

	T parse(Row row)

}
