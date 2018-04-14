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
package tools.dynamia.cms.site.mail.actions

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.cms.site.core.CMSUtil
import tools.dynamia.cms.site.core.actions.ActionEvent
import tools.dynamia.cms.site.core.actions.SiteAction
import tools.dynamia.cms.site.core.api.CMSAction
import tools.dynamia.cms.site.mail.domain.MailingContact
import tools.dynamia.cms.site.mail.services.MailService
import tools.dynamia.domain.services.CrudService

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
