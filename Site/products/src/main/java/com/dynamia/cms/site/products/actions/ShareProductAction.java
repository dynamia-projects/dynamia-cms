/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.actions;

import org.springframework.beans.factory.annotation.Autowired;

import com.dynamia.cms.site.core.CMSUtil;
import com.dynamia.cms.site.core.actions.ActionEvent;
import com.dynamia.cms.site.core.actions.SiteAction;
import com.dynamia.cms.site.core.api.CMSAction;
import com.dynamia.cms.site.products.ProductShareForm;
import com.dynamia.cms.site.products.services.ProductsService;

import tools.dynamia.domain.services.CrudService;

/**
 *
 * @author mario
 */
@CMSAction
public class ShareProductAction implements SiteAction {

	@Autowired
	private ProductsService service;

	@Autowired
	private CrudService crudService;

	@Override
	public String getName() {
		return "shareProduct";
	}

	@Override
	public void actionPerformed(ActionEvent evt) {

		ProductShareForm form = (ProductShareForm) evt.getData();
		service.shareProduct(form);

		CMSUtil.addSuccessMessage("Producto compartido exitosamente", evt.getRedirectAttributes());

	}

}
