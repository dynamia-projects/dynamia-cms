/*
 * Copyright (C) 2009 - 2020 Dynamia Soluciones IT S.A.S - NIT 900302344-1
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
package tools.dynamia.cms.core

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
    private NumberFormat nf = NumberFormat.instance
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
        if (classname != null && !classname.empty) {
            return String.format(" class='%s'", classname)
        } else {
            return ""
        }
    }

    private String style(String style) {
        if (style != null && !style.empty) {
            return String.format(" style='%s'", style)
        } else {
            return ""
        }
    }

}
