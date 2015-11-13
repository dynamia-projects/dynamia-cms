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
