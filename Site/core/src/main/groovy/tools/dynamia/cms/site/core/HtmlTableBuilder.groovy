/* 
 * Copyright 2016 Dynamia Soluciones IT SAS and the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tools.dynamia.cms.site.core

import java.text.DateFormat
import java.text.NumberFormat

/**
 *
 * @author maserrano
 */
class HtmlTableBuilder implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -1781231805446011612L

    enum Align {
        LEFT, CENTER, RIGHT
    }

    private StringBuilder sb = new StringBuilder()
    private NumberFormat nf = NumberFormat.getInstance()
    private DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT)
    private boolean rowCreated
    private int rowCount = 0
    private String title
    private int titleLevel = 1
    private Align defaultCurrencyAlign = Align.RIGHT
    private Align defaultDateAlign = Align.CENTER
    private Align defaultIntegerAlign = Align.CENTER

    private String headerStyle = "background:silver"

    private String rowStyle = ""
    private String headerClass = ""
    private String rowClass = ""

    HtmlTableBuilder() {
        this("100%", "8pt")
    }

    HtmlTableBuilder(String width, String fontSize) {
        sb.append("<table width='" + width + "' style='font-size:" + fontSize + "'>")
    }

    Align getDefaultCurrencyAlign() {
        return defaultCurrencyAlign
    }

    void setDefaultCurrencyAlign(Align defaultCurrencyAlign) {
        this.defaultCurrencyAlign = defaultCurrencyAlign
    }

    Align getDefaultDateAlign() {
        return defaultDateAlign
    }

    void setDefaultDateAlign(Align defaultDateAlign) {
        this.defaultDateAlign = defaultDateAlign
    }

    Align getDefaultIntegerAlign() {
        return defaultIntegerAlign
    }

    void setDefaultIntegerAlign(Align defaultIntegerAlign) {
        this.defaultIntegerAlign = defaultIntegerAlign
    }

    String getHeaderStyle() {
        return headerStyle
    }

    void setHeaderStyle(String headerStyle) {
        this.headerStyle = headerStyle
    }

    String getRowStyle() {
        return rowStyle
    }

    void setRowStyle(String rowStyle) {
        this.rowStyle = rowStyle
    }

    String getHeaderClass() {
        return headerClass
    }

    void setHeaderClass(String headerClass) {
        this.headerClass = headerClass
    }

    String getRowClass() {
        return rowClass
    }

    void setRowClass(String rowClass) {
        this.rowClass = rowClass
    }

    void setTitle(String title, int level) {
        if (titleLevel <= 0) {
            titleLevel = 1
        }
        this.title = title
        this.titleLevel = level
    }

    void addRow() {
        if (rowCreated) {
            sb.append("</tr>")
        }
        rowCreated = true
        sb.append(String.format("<tr%s%s>", styleClass(rowClass), style(rowStyle)))
        rowCount++

    }

    void addColumnHeader(String... name) {
        for (String text : name) {
            sb.append(String.format("<th%s%s>", styleClass(headerClass), style(headerStyle)))
            sb.append(text)
            sb.append("</th>")
        }

    }

    void addData(Object... data) {
        for (Object obj : data) {
            if (obj instanceof BigDecimal) {
                addCurrency((BigDecimal) obj)
            } else if (obj instanceof Integer) {
                addInteger((Integer) obj)
            } else if (obj instanceof Date) {
                addDate((Date) obj)
            } else {
                addText(obj)
            }
        }
    }

    void addText(Object text) {
        sb.append("<td>")
        sb.append(text)
        sb.append("</td>")
    }

    void addCurrency(BigDecimal number) {
        String align = defaultCurrencyAlign.toString().toLowerCase()

        sb.append("<td style='text-align:" + align + "'>")
        sb.append('$' + nf.format(number))
        sb.append("</td>")
    }

    void addInteger(Integer integer) {
        String align = defaultIntegerAlign.toString().toLowerCase()

        sb.append("<td style='text-align:" + align + "'>")
        sb.append(integer + "")
        sb.append("</td>")
    }

    void addDate(Date fecha) {
        String align = defaultDateAlign.toString().toLowerCase()

        sb.append("<td style='text-align:" + align + "'>")
        sb.append(df.format(fecha))
        sb.append("</td>")
    }

    int getRowCount() {
        return rowCount
    }

    String render() {
        if (rowCreated) {
            sb.append("</tr>")
        }
        sb.append("</table>")

        String result = sb.toString()
        if (title != null) {
            title = "<h" + titleLevel + ">" + title + "</h" + titleLevel + ">"
            result = title + result
        }
        return result
    }

    private String styleClass(String classname) {
        if (classname != null && !classname.isEmpty()) {
            return String.format(" class='%s'", classname)
        } else {
            return ""
        }
    }

    private String style(String style) {
        if (style != null && !style.isEmpty()) {
            return String.format(" style='%s'", style)
        } else {
            return ""
        }
    }

}