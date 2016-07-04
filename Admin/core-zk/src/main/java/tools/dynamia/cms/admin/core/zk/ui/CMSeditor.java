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

import java.util.Map;

import org.zkoss.lang.Objects;
import org.zkoss.zhtml.Textarea;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Div;
import org.zkoss.zul.North;

import tools.dynamia.actions.ActionEvent;
import tools.dynamia.actions.ActionEventBuilder;
import tools.dynamia.actions.ActionLoader;
import tools.dynamia.cms.admin.core.zk.actions.AbstractCMSEditorAction;
import tools.dynamia.cms.site.core.CMSUtil;
import tools.dynamia.zk.BindingComponentIndex;
import tools.dynamia.zk.ComponentAliasIndex;
import tools.dynamia.zk.actions.ActionToolbar;

public class CMSeditor extends Div implements ActionEventBuilder {

	private boolean unescape;
	private boolean spellchek;
	private Textarea contentArea;
	private ActionToolbar toolbar;

	/**
	 *
	 */
	private static final long serialVersionUID = -4372806043096340598L;

	static {
		ComponentAliasIndex.getInstance().add(CMSeditor.class);
		BindingComponentIndex.getInstance().put("value", CMSeditor.class);

	}

	public CMSeditor() {
		contentArea = new Textarea();
		contentArea.setSclass("cms-editor");
		contentArea.setStyle("width: 100%; height: 100%");
		contentArea.setDynamicProperty("spellcheck", spellchek);
		contentArea.setDynamicProperty("onkeydown",
				"if(event.keyCode===9){var v=this.value,s=this.selectionStart,e=this.selectionEnd;this.value=v.substring(0, s)+'\\t'+v.substring(e);this.selectionStart=this.selectionEnd=s+1;return false;}");
		contentArea.addEventListener(Events.ON_CHANGE, e -> Events.postEvent(this, e));

		toolbar = new ActionToolbar(this);

		Borderlayout layout = new Borderlayout();
		layout.appendChild(new North());
		layout.appendChild(new Center());

		layout.getNorth().appendChild(toolbar);
		layout.getCenter().appendChild(contentArea);
		layout.setHflex("1");
		layout.setVflex("1");
		appendChild(layout);

		loadActions();
	}

	private void loadActions() {
		ActionLoader<AbstractCMSEditorAction> loader = new ActionLoader<>(AbstractCMSEditorAction.class);
		loader.load().forEach(a -> toolbar.addAction(a));

	}

	public void setValue(String value) throws WrongValueException {
		if (!Objects.equals(value, contentArea.getValue())) {
			contentArea.setValue(value);
			contentArea.invalidate();
			Events.postEvent(new Event(Events.ON_CHANGE, this, value));
		}

	}

	public String getValue() {
		if (!isUnescape()) {
			return CMSUtil.escapeHtmlContent(contentArea.getValue());
		} else {
			return contentArea.getValue();
		}
	}

	public boolean isUnescape() {
		return unescape;
	}

	public void setUnescape(boolean unescape) {
		this.unescape = unescape;
	}

	public boolean isSpellchek() {
		return spellchek;
	}

	public void setSpellchek(boolean spellchek) {
		this.spellchek = spellchek;
		contentArea.setDynamicProperty("spellcheck", spellchek);

	}

	public Textarea getContentArea() {
		return contentArea;
	}

	@Override
	public ActionEvent buildActionEvent(Object arg0, Map<String, Object> arg1) {
		return new ActionEvent(getValue(), this);
	}

}
