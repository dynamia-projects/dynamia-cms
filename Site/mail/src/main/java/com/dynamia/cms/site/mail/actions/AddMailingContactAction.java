package com.dynamia.cms.site.mail.actions;

import org.springframework.beans.factory.annotation.Autowired;

import com.dynamia.cms.site.core.CMSUtil;
import com.dynamia.cms.site.core.actions.ActionEvent;
import com.dynamia.cms.site.core.actions.SiteAction;
import com.dynamia.cms.site.core.api.CMSAction;
import com.dynamia.cms.site.mail.domain.MailingContact;
import com.dynamia.cms.site.mail.services.MailService;
import com.dynamia.tools.domain.services.CrudService;

@CMSAction
public class AddMailingContactAction implements SiteAction {

	@Autowired
	private MailService service;
	
	@Autowired
	private CrudService crudService;
	
	@Override
	public String getName() {
		return "addMailingContact";
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		
		
		MailingContact contact = (MailingContact) evt.getData();
		
		if(contact!=null){
			if(!service.existsMailingContact(contact)){
				try {
					contact.setSite(evt.getSite());
					crudService.save(contact);
					CMSUtil.addSuccessMessage(contact.getEmailAddress()+" Inscrito exitosamente, recibira nuestras ofertas, productos nuevos y demas en su correo.", evt.getRedirectAttributes());
				} catch (Exception e) {
					CMSUtil.addErrorMessage("Error al inscribir contacto", evt.getRedirectAttributes());
					e.printStackTrace();
				}
			}else{
				CMSUtil.addWarningMessage("La direccion de email ingresada ya se encuentrada inscrita", evt.getRedirectAttributes());
			}		
		
		}

	}

}
