package com.dynamia.cms.admin.core.zk.customizer;

import com.dynamia.cms.site.core.SiteContext;
import com.dynamia.cms.site.core.domain.Site;

import tools.dynamia.viewers.ViewCustomizer;
import tools.dynamia.zk.crud.ui.EntityPickerBox;
import tools.dynamia.zk.viewers.form.FormView;

public class SiteFormCustomizer implements ViewCustomizer<FormView<Site>> {

	@Override
	public void customize(FormView<Site> view) {
		EntityPickerBox parentPicker = (EntityPickerBox) view.getFieldComponent("parent").getInputComponent();
		parentPicker.setDisabled(!SiteContext.get().isSuperAdmin());

	}

}
