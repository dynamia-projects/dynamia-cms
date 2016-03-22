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
package com.dynamia.cms.admin.core.zk;

import org.zkforge.ckez.CKeditor;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.InputEvent;

import com.dynamia.cms.site.pages.domain.Page;

import tools.dynamia.viewers.ViewCustomizer;
import tools.dynamia.zk.viewers.form.FormView;

public class PageFormCustomizer implements ViewCustomizer<FormView<Page>> {

	@Override
	public void customize(final FormView<Page> view) {

		CKeditor editor = (CKeditor) view.getFieldComponent("content").getInputComponent();
		editor.addEventListener("onChange", new EventListener<InputEvent>() {

			@Override
			public void onEvent(InputEvent event) throws Exception {
				view.getValue().setContent(event.getValue());

			}
		});

	}

}
