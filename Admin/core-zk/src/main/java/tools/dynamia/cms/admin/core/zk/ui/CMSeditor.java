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
package tools.dynamia.cms.admin.core.zk.ui;

import java.io.UnsupportedEncodingException;

import org.zkoss.zhtml.Textarea;
import org.zkoss.zk.ui.WrongValueException;

import tools.dynamia.cms.site.core.CMSUtil;
import tools.dynamia.zk.BindingComponentIndex;
import tools.dynamia.zk.ComponentAliasIndex;

public class CMSeditor extends Textarea {

	private String width = "100%";
	private String height = "300px";
	private String calcStyle;
	private String customStyle;
	private String chartset = "UTF-8";

	/**
	 *
	 */
	private static final long serialVersionUID = -4372806043096340598L;

	static {
		ComponentAliasIndex.getInstance().add(CMSeditor.class);
		BindingComponentIndex.getInstance().put("value", CMSeditor.class);

	}

	public CMSeditor() {
		setSclass("cms-editor");
		setDynamicProperty("spellcheck", false);
		setDynamicProperty("onkeydown",
				"if(event.keyCode===9){var v=this.value,s=this.selectionStart,e=this.selectionEnd;this.value=v.substring(0, s)+'\\t'+v.substring(e);this.selectionStart=this.selectionEnd=s+1;return false;}");

	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
		calcStyle();
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
		calcStyle();
	}

	private void calcStyle() {
		if (customStyle == null) {
			customStyle = "";
		}
		calcStyle = String.format("width: %s; height: %s; %s", width, height, customStyle);
		super.setStyle(calcStyle);
	}

	@Override
	public String getStyle() {
		return customStyle;
	}

	@Override
	public void setStyle(String style) {
		this.customStyle = style;
		calcStyle();
	}

	@Override
	public void setValue(String value) throws WrongValueException {
		value = CMSUtil.escapeHtmlContent(value);
		super.setValue(value);
	}

	public String getChartset() {
		return chartset;
	}

	public void setChartset(String chartset) {
		this.chartset = chartset;
	}

}
