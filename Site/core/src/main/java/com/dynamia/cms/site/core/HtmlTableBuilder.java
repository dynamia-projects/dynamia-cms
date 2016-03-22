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
package com.dynamia.cms.site.core;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;

/**
 *
 * @author maserrano
 */
public class HtmlTableBuilder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1781231805446011612L;

	enum Align {
		LEFT, CENTER, RIGHT;
	}

	private StringBuilder sb = new StringBuilder();
	private NumberFormat nf = NumberFormat.getInstance();
	private DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
	private boolean rowCreated;
	private int rowCount = 0;
	private String title;
	private int titleLevel = 1;
	private Align defaultCurrencyAlign = Align.RIGHT;
	private Align defaultDateAlign = Align.CENTER;
	private Align defaultIntegerAlign = Align.CENTER;

	private String headerStyle = "background:silver";

	private String rowStyle = "";
	private String headerClass = "";
	private String rowClass = "";

	public HtmlTableBuilder() {
		this("100%", "8pt");
	}

	public HtmlTableBuilder(String width, String fontSize) {
		sb.append("<table width='" + width + "' style='font-size:" + fontSize + "'>");
	}

	public Align getDefaultCurrencyAlign() {
		return defaultCurrencyAlign;
	}

	public void setDefaultCurrencyAlign(Align defaultCurrencyAlign) {
		this.defaultCurrencyAlign = defaultCurrencyAlign;
	}

	public Align getDefaultDateAlign() {
		return defaultDateAlign;
	}

	public void setDefaultDateAlign(Align defaultDateAlign) {
		this.defaultDateAlign = defaultDateAlign;
	}

	public Align getDefaultIntegerAlign() {
		return defaultIntegerAlign;
	}

	public void setDefaultIntegerAlign(Align defaultIntegerAlign) {
		this.defaultIntegerAlign = defaultIntegerAlign;
	}

	public String getHeaderStyle() {
		return headerStyle;
	}

	public void setHeaderStyle(String headerStyle) {
		this.headerStyle = headerStyle;
	}

	public String getRowStyle() {
		return rowStyle;
	}

	public void setRowStyle(String rowStyle) {
		this.rowStyle = rowStyle;
	}

	public String getHeaderClass() {
		return headerClass;
	}

	public void setHeaderClass(String headerClass) {
		this.headerClass = headerClass;
	}

	public String getRowClass() {
		return rowClass;
	}

	public void setRowClass(String rowClass) {
		this.rowClass = rowClass;
	}

	public void setTitle(String title, int level) {
		if (titleLevel <= 0) {
			titleLevel = 1;
		}
		this.title = title;
		this.titleLevel = level;
	}

	public void addRow() {
		if (rowCreated) {
			sb.append("</tr>");
		}
		rowCreated = true;
		sb.append(String.format("<tr%s%s>", styleClass(rowClass), style(rowStyle)));
		rowCount++;

	}

	public void addColumnHeader(String... name) {
		for (String text : name) {
			sb.append(String.format("<th%s%s>", styleClass(headerClass), style(headerStyle)));
			sb.append(text);
			sb.append("</th>");
		}

	}

	public void addData(Object... data) {
		for (Object obj : data) {
			if (obj instanceof BigDecimal) {
				addCurrency((BigDecimal) obj);
			} else if (obj instanceof Integer) {
				addInteger((Integer) obj);
			} else if (obj instanceof Date) {
				addDate((Date) obj);
			} else {
				addText(obj);
			}
		}
	}

	public void addText(Object text) {
		sb.append("<td>");
		sb.append(text);
		sb.append("</td>");
	}

	public void addCurrency(BigDecimal number) {
		String align = defaultCurrencyAlign.toString().toLowerCase();

		sb.append("<td style='text-align:" + align + "'>");
		sb.append("$" + nf.format(number));
		sb.append("</td>");
	}

	public void addInteger(Integer integer) {
		String align = defaultIntegerAlign.toString().toLowerCase();

		sb.append("<td style='text-align:" + align + "'>");
		sb.append(integer + "");
		sb.append("</td>");
	}

	public void addDate(Date fecha) {
		String align = defaultDateAlign.toString().toLowerCase();

		sb.append("<td style='text-align:" + align + "'>");
		sb.append(df.format(fecha));
		sb.append("</td>");
	}

	public int getRowCount() {
		return rowCount;
	}

	public String render() {
		if (rowCreated) {
			sb.append("</tr>");
		}
		sb.append("</table>");

		String result = sb.toString();
		if (title != null) {
			title = "<h" + titleLevel + ">" + title + "</h" + titleLevel + ">";
			result = title + result;
		}
		return result;
	}

	private String styleClass(String classname) {
		if (classname != null && !classname.isEmpty()) {
			return String.format(" class='%s'", classname);
		} else {
			return "";
		}
	}

	private String style(String style) {
		if (style != null && !style.isEmpty()) {
			return String.format(" style='%s'", style);
		} else {
			return "";
		}
	}

}
